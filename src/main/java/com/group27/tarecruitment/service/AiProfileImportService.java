package com.group27.tarecruitment.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.group27.tarecruitment.model.AiImportTask;
import com.group27.tarecruitment.model.AiProfileSuggestion;
import com.group27.tarecruitment.model.AiVacancyRecommendation;
import com.group27.tarecruitment.model.ApplicantProfile;
import com.group27.tarecruitment.model.Vacancy;
import com.group27.tarecruitment.repository.AiImportTaskRepository;
import com.group27.tarecruitment.util.ValidationUtil;
import jakarta.servlet.http.HttpServletRequest;

import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.function.Consumer;

public class AiProfileImportService {
    public static final String SCHEMA_VERSION = "profile-and-ranking-v1";
    private static final long TASK_TTL_MILLIS = 10 * 60 * 1000L;
    private static final long CV_DOWNLOAD_TTL_MILLIS = 10 * 60 * 1000L;
    private static final int CV_DOWNLOAD_MAX_ACCESS_COUNT = 5;
    private static final int MAX_CALLBACK_BODY_CHARS = 200_000;
    private static final int MAX_REASONS_PER_VACANCY = 3;
    private static final int MAX_REASON_LENGTH = 160;
    private static final int MAX_RANKING_ITEMS = 12;
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private static final Set<String> PROFILE_FIELD_WHITELIST = Set.of(
            "fullName",
            "studentId",
            "email",
            "phone",
            "degreeProgramme",
            "yearOfStudy",
            "relevantCourses",
            "skills",
            "taExperience",
            "projectOrLeadershipExperience",
            "availability"
    );

    private final AiImportTaskRepository aiImportTaskRepository = new AiImportTaskRepository();
    private final ApplicantProfileService applicantProfileService = new ApplicantProfileService();
    private final VacancyService vacancyService = new VacancyService();

    public TaskCreationResult createTask(String userId, HttpServletRequest request) {
        expireOldTasks();
        long now = Instant.now().toEpochMilli();

        AiImportTask task = new AiImportTask();
        task.setTaskId("ai-task-" + UUID.randomUUID().toString().replace("-", ""));
        task.setUserId(userId);
        task.setStatus(AiImportTask.STATUS_CREATED);
        task.setProfileStatus(AiImportTask.IMPORT_STATUS_PENDING);
        task.setRankingStatus(AiImportTask.IMPORT_STATUS_PENDING);
        task.setCallbackToken(UUID.randomUUID().toString().replace("-", ""));
        task.setSchemaVersion(SCHEMA_VERSION);
        task.setCreatedAtEpochMillis(now);
        task.setExpiresAtEpochMillis(now + TASK_TTL_MILLIS);
        task.setValidationErrors(new ArrayList<>());
        task.setProfileValidationErrors(new ArrayList<>());
        task.setRankingValidationErrors(new ArrayList<>());

        List<Vacancy> candidateVacancies = getBrowsableVacancies();
        task.setEligibleVacancyIds(candidateVacancies.stream()
                .map(Vacancy::getVacancyId)
                .filter(id -> !ValidationUtil.isBlank(id))
                .toList());

        String cvDownloadUrl = resolveCvDownloadUrl(task, userId, request, now);
        aiImportTaskRepository.save(task);

        String callbackUrl = buildAbsoluteUrl(request, request.getContextPath()
                + "/ai/callback?taskId=" + task.getTaskId());
        return new TaskCreationResult(
                task,
                callbackUrl,
                cvDownloadUrl,
                buildPromptTemplate(task, callbackUrl, cvDownloadUrl, candidateVacancies)
        );
    }

    public Optional<AiImportTask> findTaskForUser(String userId, String taskId) {
        expireOldTasks();
        return aiImportTaskRepository.findById(taskId)
                .filter(task -> userId.equals(task.getUserId()));
    }

    public Optional<AiImportTask> findLatestValidatedRankingTaskForUser(String userId) {
        expireOldTasks();
        return aiImportTaskRepository.findAll().stream()
                .filter(task -> userId.equals(task.getUserId()))
                .filter(task -> AiImportTask.IMPORT_STATUS_VALIDATED.equals(task.getRankingStatus()))
                .filter(task -> task.getRecommendations() != null && !task.getRecommendations().isEmpty())
                .max(Comparator.comparingLong(this::rankingSortKey));
    }

    private long rankingSortKey(AiImportTask task) {
        if (task.getValidatedAtEpochMillis() != null) {
            return task.getValidatedAtEpochMillis();
        }
        return task.getCreatedAtEpochMillis();
    }

    public CallbackResult acceptCallback(String taskId, String callbackToken, String payloadJson) {
        expireOldTasks();
        if (ValidationUtil.isBlank(taskId)) {
            return CallbackResult.error("INVALID_TASK", "Missing task ID.");
        }
        if (ValidationUtil.isBlank(callbackToken)) {
            return CallbackResult.error("INVALID_TOKEN", "Missing callback token.");
        }
        if (ValidationUtil.isBlank(payloadJson)) {
            return CallbackResult.error("INVALID_PAYLOAD", "Payload is empty.");
        }
        if (payloadJson.length() > MAX_CALLBACK_BODY_CHARS) {
            return CallbackResult.error("PAYLOAD_TOO_LARGE", "Payload exceeds the allowed size.");
        }

        Optional<AiImportTask> optionalTask = aiImportTaskRepository.findById(taskId);
        if (optionalTask.isEmpty()) {
            return CallbackResult.error("TASK_NOT_FOUND", "Task does not exist.");
        }

        AiImportTask task = optionalTask.get();
        if (!callbackToken.equals(task.getCallbackToken())) {
            return CallbackResult.error("INVALID_TOKEN", "Callback token does not match.");
        }
        if (AiImportTask.STATUS_EXPIRED.equals(task.getStatus())) {
            return CallbackResult.error("TASK_EXPIRED", "Task has expired.");
        }
        if (AiImportTask.STATUS_APPLIED.equals(task.getStatus())) {
            return CallbackResult.error("TASK_LOCKED", "Task has already been applied.");
        }

        task.setStatus(AiImportTask.STATUS_RECEIVED);
        task.setReceivedAtEpochMillis(Instant.now().toEpochMilli());
        task.setRawPayloadJson(payloadJson);

        List<String> profileErrors = new ArrayList<>();
        List<String> rankingErrors = new ArrayList<>();
        List<String> sharedErrors = new ArrayList<>();
        ParseResult parseResult = parsePayload(task, payloadJson, profileErrors, rankingErrors, sharedErrors);

        if (!sharedErrors.isEmpty()) {
            profileErrors.addAll(sharedErrors);
            rankingErrors.addAll(sharedErrors);
        }

        task.setSuggestion(parseResult.suggestion());
        task.setRecommendations(parseResult.recommendations());

        task.setProfileStatus(profileErrors.isEmpty()
                ? AiImportTask.IMPORT_STATUS_VALIDATED
                : AiImportTask.IMPORT_STATUS_FAILED);
        task.setRankingStatus(rankingErrors.isEmpty()
                ? AiImportTask.IMPORT_STATUS_VALIDATED
                : AiImportTask.IMPORT_STATUS_FAILED);
        task.setProfileValidationErrors(profileErrors);
        task.setRankingValidationErrors(rankingErrors);

        List<String> mergedErrors = new ArrayList<>();
        mergedErrors.addAll(profileErrors);
        mergedErrors.addAll(rankingErrors);
        task.setValidationErrors(mergedErrors);

        if (AiImportTask.IMPORT_STATUS_VALIDATED.equals(task.getProfileStatus())
                || AiImportTask.IMPORT_STATUS_VALIDATED.equals(task.getRankingStatus())) {
            task.setValidatedAtEpochMillis(Instant.now().toEpochMilli());
            if (AiImportTask.IMPORT_STATUS_VALIDATED.equals(task.getProfileStatus())) {
                task.setStatus(AiImportTask.STATUS_VALIDATED);
            } else {
                task.setStatus(AiImportTask.STATUS_PARTIAL);
            }
            aiImportTaskRepository.save(task);
            return CallbackResult.ok();
        }

        task.setStatus(AiImportTask.STATUS_FAILED);
        aiImportTaskRepository.save(task);
        return CallbackResult.error("VALIDATION_FAILED", "Payload failed schema validation.");
    }

    public void expireOldTasks() {
        long now = Instant.now().toEpochMilli();
        List<AiImportTask> tasks = new ArrayList<>(aiImportTaskRepository.findAll());
        boolean changed = false;
        for (AiImportTask task : tasks) {
            if (task.getExpiresAtEpochMillis() <= now
                    && !AiImportTask.STATUS_APPLIED.equals(task.getStatus())
                    && !AiImportTask.STATUS_EXPIRED.equals(task.getStatus())) {
                task.setStatus(AiImportTask.STATUS_EXPIRED);
                changed = true;
            }
        }
        if (changed) {
            aiImportTaskRepository.saveAll(tasks);
        }
    }

    public CvDownloadResult consumeCvDownload(String taskId, String cvToken) {
        expireOldTasks();
        if (ValidationUtil.isBlank(taskId)) {
            return CvDownloadResult.error("INVALID_TASK", "Missing task ID.");
        }
        if (ValidationUtil.isBlank(cvToken)) {
            return CvDownloadResult.error("INVALID_TOKEN", "Missing CV download token.");
        }

        Optional<AiImportTask> optionalTask = aiImportTaskRepository.findById(taskId);
        if (optionalTask.isEmpty()) {
            return CvDownloadResult.error("TASK_NOT_FOUND", "Task does not exist.");
        }

        AiImportTask task = optionalTask.get();
        if (!cvToken.equals(task.getCvDownloadToken())) {
            return CvDownloadResult.error("INVALID_TOKEN", "CV download token does not match.");
        }
        if (task.getCvDownloadExpiresAtEpochMillis() == null
                || task.getCvDownloadExpiresAtEpochMillis() < Instant.now().toEpochMilli()) {
            return CvDownloadResult.error("TOKEN_EXPIRED", "CV download token has expired.");
        }
        int accessCount = task.getCvDownloadAccessCount() == null ? 0 : task.getCvDownloadAccessCount();
        if (accessCount >= CV_DOWNLOAD_MAX_ACCESS_COUNT) {
            return CvDownloadResult.error("TOKEN_EXHAUSTED", "CV download token exceeded access limit.");
        }

        Optional<ApplicantProfile> optionalProfile = applicantProfileService.findByApplicantId(task.getUserId());
        if (optionalProfile.isEmpty()) {
            return CvDownloadResult.error("CV_NOT_FOUND", "Applicant profile is missing.");
        }

        ApplicantProfile profile = optionalProfile.get();
        String filePath = ValidationUtil.trimToEmpty(profile.getCvFilePath());
        if (ValidationUtil.isBlank(filePath)) {
            return CvDownloadResult.error("CV_NOT_FOUND", "No CV file is available for this task.");
        }

        Path cvPath = Path.of(filePath);
        if (!Files.exists(cvPath) || !Files.isRegularFile(cvPath)) {
            return CvDownloadResult.error("CV_NOT_FOUND", "CV file is not available on server.");
        }

        return CvDownloadResult.ok(cvPath, ValidationUtil.trimToEmpty(profile.getCvFileName()));
    }

    public void markCvDownloadConsumed(String taskId) {
        Optional<AiImportTask> optionalTask = aiImportTaskRepository.findById(taskId);
        if (optionalTask.isEmpty()) {
            return;
        }
        AiImportTask task = optionalTask.get();
        long now = Instant.now().toEpochMilli();
        if (task.getCvDownloadedAtEpochMillis() == null) {
            task.setCvDownloadedAtEpochMillis(now);
        }
        int accessCount = task.getCvDownloadAccessCount() == null ? 0 : task.getCvDownloadAccessCount();
        task.setCvDownloadAccessCount(accessCount + 1);
        aiImportTaskRepository.save(task);
    }

    public ApplyResult applySuggestionToProfile(String userId, String taskId) {
        expireOldTasks();
        if (ValidationUtil.isBlank(taskId)) {
            return ApplyResult.error("INVALID_TASK", "taskId is required.");
        }

        Optional<AiImportTask> optionalTask = aiImportTaskRepository.findById(taskId)
                .filter(task -> userId.equals(task.getUserId()));
        if (optionalTask.isEmpty()) {
            return ApplyResult.error("TASK_NOT_FOUND", "Task does not exist.");
        }

        AiImportTask task = optionalTask.get();
        if (AiImportTask.STATUS_EXPIRED.equals(task.getStatus())) {
            return ApplyResult.error("TASK_EXPIRED", "Task has expired.");
        }
        if (AiImportTask.STATUS_APPLIED.equals(task.getStatus())) {
            return ApplyResult.error("TASK_LOCKED", "Task has already been applied.");
        }
        if (!AiImportTask.IMPORT_STATUS_VALIDATED.equals(task.getProfileStatus())) {
            return ApplyResult.error("TASK_NOT_READY", "Profile fields are not validated yet.");
        }
        if (task.getSuggestion() == null) {
            return ApplyResult.error("TASK_NO_SUGGESTION", "Task has no validated suggestion.");
        }

        ApplicantProfile profile = applicantProfileService.findByApplicantId(userId)
                .orElseGet(() -> buildEmptyProfile(userId));
        mergeSuggestionIntoProfile(profile, task.getSuggestion());
        applicantProfileService.saveProfile(profile);

        task.setProfileStatus(AiImportTask.IMPORT_STATUS_APPLIED);
        task.setStatus(AiImportTask.STATUS_APPLIED);
        task.setAppliedAtEpochMillis(Instant.now().toEpochMilli());
        aiImportTaskRepository.save(task);
        return ApplyResult.ok(profile);
    }

    private ApplicantProfile buildEmptyProfile(String userId) {
        ApplicantProfile profile = new ApplicantProfile();
        profile.setApplicantId(userId);
        profile.setBlacklisted(false);
        return profile;
    }

    private void mergeSuggestionIntoProfile(ApplicantProfile profile, AiProfileSuggestion suggestion) {
        applyTextIfPresent(profile::setFullName, suggestion.getFullName());
        applyTextIfPresent(profile::setStudentId, suggestion.getStudentId());
        applyTextIfPresent(profile::setEmail, suggestion.getEmail());
        applyTextIfPresent(profile::setPhone, suggestion.getPhone());
        applyTextIfPresent(profile::setDegreeProgramme, suggestion.getDegreeProgramme());
        applyTextIfPresent(profile::setYearOfStudy, suggestion.getYearOfStudy());
        applyTextIfPresent(profile::setTaExperience, suggestion.getTaExperience());
        applyTextIfPresent(profile::setProjectOrLeadershipExperience, suggestion.getProjectOrLeadershipExperience());
        applyTextIfPresent(profile::setAvailability, suggestion.getAvailability());
        if (suggestion.getRelevantCourses() != null && !suggestion.getRelevantCourses().isEmpty()) {
            profile.setRelevantCourses(suggestion.getRelevantCourses());
        }
        if (suggestion.getSkills() != null && !suggestion.getSkills().isEmpty()) {
            profile.setSkills(suggestion.getSkills());
        }
    }

    private void applyTextIfPresent(Consumer<String> setter, String value) {
        String normalized = ValidationUtil.trimToEmpty(value);
        if (!normalized.isEmpty()) {
            setter.accept(normalized);
        }
    }

    private ParseResult parsePayload(AiImportTask task,
                                     String payloadJson,
                                     List<String> profileErrors,
                                     List<String> rankingErrors,
                                     List<String> sharedErrors) {
        try {
            JsonNode root = OBJECT_MAPPER.readTree(payloadJson);
            String schemaVersion = readText(root.get("schemaVersion"));
            if (!SCHEMA_VERSION.equals(schemaVersion)) {
                sharedErrors.add("schemaVersion must be " + SCHEMA_VERSION + ".");
            }

            AiProfileSuggestion suggestion = parseSuggestion(root.get("profile"), profileErrors);
            List<AiVacancyRecommendation> recommendations =
                    parseRecommendations(root.get("rankings"), task.getEligibleVacancyIds(), rankingErrors);
            return new ParseResult(suggestion, recommendations);
        } catch (Exception exception) {
            sharedErrors.add("payload must be valid JSON.");
            return new ParseResult(null, new ArrayList<>());
        }
    }

    private AiProfileSuggestion parseSuggestion(JsonNode profileNode, List<String> errors) {
        if (profileNode == null || !profileNode.isObject()) {
            errors.add("profile object is required.");
            return null;
        }

        Set<String> seenFields = new HashSet<>();
        profileNode.fieldNames().forEachRemaining(seenFields::add);
        for (String field : seenFields) {
            if (!PROFILE_FIELD_WHITELIST.contains(field)) {
                errors.add("Unsupported profile field: " + field);
            }
        }

        AiProfileSuggestion suggestion = new AiProfileSuggestion();
        suggestion.setFullName(readText(profileNode.get("fullName")));
        suggestion.setStudentId(readText(profileNode.get("studentId")));
        suggestion.setEmail(readText(profileNode.get("email")));
        suggestion.setPhone(readText(profileNode.get("phone")));
        suggestion.setDegreeProgramme(readText(profileNode.get("degreeProgramme")));
        suggestion.setYearOfStudy(readText(profileNode.get("yearOfStudy")));
        suggestion.setTaExperience(readText(profileNode.get("taExperience")));
        suggestion.setProjectOrLeadershipExperience(readText(profileNode.get("projectOrLeadershipExperience")));
        suggestion.setAvailability(readText(profileNode.get("availability")));
        suggestion.setRelevantCourses(readStringList(profileNode.get("relevantCourses"), "relevantCourses", errors));
        suggestion.setSkills(readStringList(profileNode.get("skills"), "skills", errors));

        if (!ValidationUtil.isBlank(suggestion.getEmail()) && !ValidationUtil.isValidEmail(suggestion.getEmail())) {
            errors.add("email must be a valid email address.");
        }
        if (!ValidationUtil.isBlank(suggestion.getYearOfStudy())
                && !ValidationUtil.isPositiveInteger(suggestion.getYearOfStudy())) {
            errors.add("yearOfStudy must be a positive integer.");
        }

        if (errors.isEmpty()) {
            return suggestion;
        }
        return null;
    }

    private List<AiVacancyRecommendation> parseRecommendations(JsonNode rankingsNode,
                                                               List<String> eligibleVacancyIds,
                                                               List<String> errors) {
        if (rankingsNode == null || !rankingsNode.isArray()) {
            errors.add("rankings array is required.");
            return new ArrayList<>();
        }

        Set<String> allowedIds = new LinkedHashSet<>(eligibleVacancyIds == null ? List.of() : eligibleVacancyIds);
        Set<String> seenIds = new HashSet<>();
        List<AiVacancyRecommendation> recommendations = new ArrayList<>();
        for (JsonNode itemNode : rankingsNode) {
            if (recommendations.size() >= MAX_RANKING_ITEMS) {
                break;
            }
            if (!itemNode.isObject()) {
                errors.add("Each rankings item must be an object.");
                return new ArrayList<>();
            }
            String vacancyId = readText(itemNode.get("vacancyId"));
            if (ValidationUtil.isBlank(vacancyId)) {
                errors.add("vacancyId is required for each rankings item.");
                continue;
            }
            if (!allowedIds.contains(vacancyId)) {
                errors.add("vacancyId not in current browse list: " + vacancyId);
                continue;
            }
            if (!seenIds.add(vacancyId)) {
                errors.add("Duplicate vacancyId in rankings: " + vacancyId);
                continue;
            }
            Integer score = readScore(itemNode.get("score"));
            if (score == null || score < 0 || score > 100) {
                errors.add("score must be an integer between 0 and 100 for " + vacancyId + ".");
                continue;
            }

            AiVacancyRecommendation recommendation = new AiVacancyRecommendation();
            recommendation.setVacancyId(vacancyId);
            recommendation.setScore(score);
            recommendation.setReasons(readReasons(itemNode.get("reasons"), errors, vacancyId));
            recommendations.add(recommendation);
        }

        if (recommendations.isEmpty()) {
            errors.add("rankings must contain at least one valid item.");
        }
        recommendations.sort(Comparator.comparingInt((AiVacancyRecommendation item) ->
                        item.getScore() == null ? -1 : item.getScore())
                .reversed());
        return recommendations;
    }

    private List<String> readReasons(JsonNode reasonsNode, List<String> errors, String vacancyId) {
        if (reasonsNode == null || reasonsNode.isNull()) {
            return new ArrayList<>();
        }
        if (!reasonsNode.isArray()) {
            errors.add("reasons must be an array for " + vacancyId + ".");
            return new ArrayList<>();
        }
        List<String> reasons = new ArrayList<>();
        for (JsonNode reasonNode : reasonsNode) {
            if (!reasonNode.isTextual()) {
                errors.add("reasons must contain only strings for " + vacancyId + ".");
                return new ArrayList<>();
            }
            String reason = ValidationUtil.trimToEmpty(reasonNode.asText());
            if (reason.isEmpty()) {
                continue;
            }
            if (reason.length() > MAX_REASON_LENGTH) {
                reason = reason.substring(0, MAX_REASON_LENGTH);
            }
            reasons.add(reason);
            if (reasons.size() >= MAX_REASONS_PER_VACANCY) {
                break;
            }
        }
        return reasons;
    }

    private Integer readScore(JsonNode scoreNode) {
        if (scoreNode == null || scoreNode.isNull() || !scoreNode.isNumber()) {
            return null;
        }
        return scoreNode.asInt();
    }

    private List<String> readStringList(JsonNode node, String fieldName, List<String> errors) {
        if (node == null || node.isNull()) {
            return new ArrayList<>();
        }
        if (!node.isArray()) {
            errors.add(fieldName + " must be an array of strings.");
            return new ArrayList<>();
        }
        List<String> values = new ArrayList<>();
        for (JsonNode child : node) {
            if (!child.isTextual()) {
                errors.add(fieldName + " must contain only strings.");
                return new ArrayList<>();
            }
            String value = ValidationUtil.trimToEmpty(child.asText());
            if (!value.isEmpty()) {
                values.add(value);
            }
        }
        return values;
    }

    private String readText(JsonNode node) {
        if (node == null || node.isNull()) {
            return "";
        }
        if (!node.isTextual()) {
            return "";
        }
        return ValidationUtil.trimToEmpty(node.asText());
    }

    private String buildAbsoluteUrl(HttpServletRequest request, String path) {
        String scheme = request.getScheme();
        String serverName = request.getServerName();
        int port = request.getServerPort();
        boolean defaultPort = ("http".equalsIgnoreCase(scheme) && port == 80)
                || ("https".equalsIgnoreCase(scheme) && port == 443);
        return scheme + "://" + serverName + (defaultPort ? "" : ":" + port) + path;
    }

    private String resolveCvDownloadUrl(AiImportTask task, String userId, HttpServletRequest request, long now) {
        Optional<ApplicantProfile> optionalProfile = applicantProfileService.findByApplicantId(userId);
        if (optionalProfile.isEmpty()) {
            return "";
        }

        ApplicantProfile profile = optionalProfile.get();
        String cvPath = ValidationUtil.trimToEmpty(profile.getCvFilePath());
        if (ValidationUtil.isBlank(cvPath)) {
            return "";
        }

        Path sourcePath = Path.of(cvPath);
        if (!Files.exists(sourcePath) || !Files.isRegularFile(sourcePath)) {
            return "";
        }

        String downloadToken = UUID.randomUUID().toString().replace("-", "");
        task.setCvDownloadToken(downloadToken);
        task.setCvDownloadExpiresAtEpochMillis(now + CV_DOWNLOAD_TTL_MILLIS);
        return buildAbsoluteUrl(request, request.getContextPath()
                + "/ai/cv-download?taskId=" + task.getTaskId() + "&token=" + downloadToken);
    }

    private String buildPromptTemplate(AiImportTask task,
                                       String callbackUrl,
                                       String cvDownloadUrl,
                                       List<Vacancy> candidateVacancies) {
        StringBuilder builder = new StringBuilder();
        builder.append("You are completing two outputs from one CV and one vacancy list:\n");
        builder.append("1) profile field extraction\n");
        builder.append("2) TA vacancy fit ranking\n");
        if (!ValidationUtil.isBlank(cvDownloadUrl)) {
            builder.append("First, download the CV file from this short-lived URL (it may be fetched more than once by your client):\n")
                    .append(cvDownloadUrl)
                    .append("\n");
        } else {
            builder.append("No CV download URL is available for this task. Ask the user to upload the CV file manually to this chat before extracting.\n");
        }

        builder.append("Return ONLY JSON with this exact top-level shape:\n")
                .append("{\n")
                .append("  \"schemaVersion\": \"").append(SCHEMA_VERSION).append("\",\n")
                .append("  \"profile\": {\n")
                .append("    \"fullName\": \"\",\n")
                .append("    \"studentId\": \"\",\n")
                .append("    \"email\": \"\",\n")
                .append("    \"phone\": \"\",\n")
                .append("    \"degreeProgramme\": \"\",\n")
                .append("    \"yearOfStudy\": \"\",\n")
                .append("    \"relevantCourses\": [],\n")
                .append("    \"skills\": [],\n")
                .append("    \"taExperience\": \"\",\n")
                .append("    \"projectOrLeadershipExperience\": \"\",\n")
                .append("    \"availability\": \"\"\n")
                .append("  },\n")
                .append("  \"rankings\": [\n")
                .append("    {\"vacancyId\": \"\", \"score\": 0, \"reasons\": [\"\"]}\n")
                .append("  ]\n")
                .append("}\n")
                .append("Ranking rules:\n")
                .append("- score must be an integer 0-100\n")
                .append("- use only vacancyId values from the list below\n")
                .append("- return up to ").append(MAX_RANKING_ITEMS).append(" items sorted by score descending\n")
                .append("- reasons should be short (1-3 per vacancy)\n")
                .append("\nVacancy list:\n")
                .append(buildVacancySummary(candidateVacancies))
                .append("\nThen call this callback endpoint with POST and raw JSON body:\n")
                .append(callbackUrl).append("\n")
                .append("Set header: X-Callback-Token: ").append(task.getCallbackToken()).append("\n")
                .append("Do not include markdown fences.");
        return builder.toString();
    }

    private List<Vacancy> getBrowsableVacancies() {
        return vacancyService.getAllVacancies().stream()
                .filter(vacancy -> isBrowsableStatus(vacancy.getStatus()))
                .toList();
    }

    private String buildVacancySummary(List<Vacancy> vacancies) {
        StringBuilder builder = new StringBuilder();
        for (Vacancy vacancy : vacancies) {
            builder.append("- vacancyId=").append(ValidationUtil.trimToEmpty(vacancy.getVacancyId()))
                    .append("; moduleCode=").append(ValidationUtil.trimToEmpty(vacancy.getModuleCode()))
                    .append("; moduleName=").append(ValidationUtil.trimToEmpty(vacancy.getModuleName()))
                    .append("; campus=").append(ValidationUtil.trimToEmpty(vacancy.getCampus()))
                    .append("; requiredSkills=")
                    .append(vacancy.getRequiredSkills() == null ? List.of() : vacancy.getRequiredSkills())
                    .append("; description=").append(ValidationUtil.trimToEmpty(vacancy.getDescription()))
                    .append("\n");
        }
        return builder.toString();
    }

    private boolean isBrowsableStatus(String status) {
        String normalized = ValidationUtil.trimToEmpty(status);
        return "OPEN".equalsIgnoreCase(normalized) || "CLOSED".equalsIgnoreCase(normalized);
    }

    public static class TaskCreationResult {
        private final AiImportTask task;
        private final String callbackUrl;
        private final String cvDownloadUrl;
        private final String promptTemplate;

        public TaskCreationResult(AiImportTask task, String callbackUrl, String cvDownloadUrl, String promptTemplate) {
            this.task = task;
            this.callbackUrl = callbackUrl;
            this.cvDownloadUrl = cvDownloadUrl;
            this.promptTemplate = promptTemplate;
        }

        public AiImportTask getTask() {
            return task;
        }

        public String getCallbackUrl() {
            return callbackUrl;
        }

        public String getCvDownloadUrl() {
            return cvDownloadUrl;
        }

        public String getPromptTemplate() {
            return promptTemplate;
        }
    }

    public static class CallbackResult {
        private final boolean ok;
        private final String code;
        private final String message;

        private CallbackResult(boolean ok, String code, String message) {
            this.ok = ok;
            this.code = code;
            this.message = message;
        }

        public static CallbackResult ok() {
            return new CallbackResult(true, "OK", "Accepted");
        }

        public static CallbackResult error(String code, String message) {
            return new CallbackResult(false, code, message);
        }

        public boolean isOk() {
            return ok;
        }

        public String getCode() {
            return code;
        }

        public String getMessage() {
            return message;
        }
    }

    public static class ApplyResult {
        private final boolean ok;
        private final String code;
        private final String message;
        private final ApplicantProfile profile;

        private ApplyResult(boolean ok, String code, String message, ApplicantProfile profile) {
            this.ok = ok;
            this.code = code;
            this.message = message;
            this.profile = profile;
        }

        public static ApplyResult ok(ApplicantProfile profile) {
            return new ApplyResult(true, "OK", "Applied", profile);
        }

        public static ApplyResult error(String code, String message) {
            return new ApplyResult(false, code, message, null);
        }

        public boolean isOk() {
            return ok;
        }

        public String getCode() {
            return code;
        }

        public String getMessage() {
            return message;
        }

        public ApplicantProfile getProfile() {
            return profile;
        }
    }

    public static class CvDownloadResult {
        private final boolean ok;
        private final String code;
        private final String message;
        private final Path filePath;
        private final String fileName;

        private CvDownloadResult(boolean ok, String code, String message, Path filePath, String fileName) {
            this.ok = ok;
            this.code = code;
            this.message = message;
            this.filePath = filePath;
            this.fileName = fileName;
        }

        public static CvDownloadResult ok(Path filePath, String fileName) {
            return new CvDownloadResult(true, "OK", "Ready", filePath, fileName);
        }

        public static CvDownloadResult error(String code, String message) {
            return new CvDownloadResult(false, code, message, null, "");
        }

        public boolean isOk() {
            return ok;
        }

        public String getCode() {
            return code;
        }

        public String getMessage() {
            return message;
        }

        public Path getFilePath() {
            return filePath;
        }

        public String getFileName() {
            return fileName;
        }
    }

    private record ParseResult(AiProfileSuggestion suggestion, List<AiVacancyRecommendation> recommendations) {
    }
}
