package com.group27.tarecruitment.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.group27.tarecruitment.model.ApplicantProfile;
import com.group27.tarecruitment.model.AiImportTask;
import com.group27.tarecruitment.model.AiProfileSuggestion;
import com.group27.tarecruitment.repository.AiImportTaskRepository;
import com.group27.tarecruitment.util.ValidationUtil;
import jakarta.servlet.http.HttpServletRequest;

import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.function.Consumer;

public class AiProfileImportService {
    public static final String SCHEMA_VERSION = "profile-import-v1";
    private static final long TASK_TTL_MILLIS = 10 * 60 * 1000L;
    private static final long CV_DOWNLOAD_TTL_MILLIS = 10 * 60 * 1000L;
    private static final int CV_DOWNLOAD_MAX_ACCESS_COUNT = 5;
    private static final int MAX_CALLBACK_BODY_CHARS = 200_000;
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

    public TaskCreationResult createTask(String userId, HttpServletRequest request) {
        expireOldTasks();
        long now = Instant.now().toEpochMilli();

        AiImportTask task = new AiImportTask();
        task.setTaskId("ai-task-" + UUID.randomUUID().toString().replace("-", ""));
        task.setUserId(userId);
        task.setStatus(AiImportTask.STATUS_CREATED);
        task.setCallbackToken(UUID.randomUUID().toString().replace("-", ""));
        task.setSchemaVersion(SCHEMA_VERSION);
        task.setCreatedAtEpochMillis(now);
        task.setExpiresAtEpochMillis(now + TASK_TTL_MILLIS);
        task.setValidationErrors(new ArrayList<>());

        String cvDownloadUrl = resolveCvDownloadUrl(task, userId, request, now);
        aiImportTaskRepository.save(task);

        String callbackUrl = buildAbsoluteUrl(request, request.getContextPath()
                + "/ai/callback?taskId=" + task.getTaskId());
        return new TaskCreationResult(task, callbackUrl, cvDownloadUrl, buildPromptTemplate(task, callbackUrl, cvDownloadUrl));
    }

    public Optional<AiImportTask> findTaskForUser(String userId, String taskId) {
        expireOldTasks();
        return aiImportTaskRepository.findById(taskId)
                .filter(task -> userId.equals(task.getUserId()));
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

        List<String> errors = new ArrayList<>();
        AiProfileSuggestion suggestion = parseSuggestion(payloadJson, errors);
        if (!errors.isEmpty()) {
            task.setStatus(AiImportTask.STATUS_FAILED);
            task.setValidationErrors(errors);
            aiImportTaskRepository.save(task);
            return CallbackResult.error("VALIDATION_FAILED", "Payload failed schema validation.");
        }

        task.setSuggestion(suggestion);
        task.setValidatedAtEpochMillis(Instant.now().toEpochMilli());
        task.setStatus(AiImportTask.STATUS_VALIDATED);
        task.setValidationErrors(new ArrayList<>());
        aiImportTaskRepository.save(task);
        return CallbackResult.ok();
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
        if (!AiImportTask.STATUS_VALIDATED.equals(task.getStatus())) {
            return ApplyResult.error("TASK_NOT_READY", "Task is not validated yet.");
        }
        if (task.getSuggestion() == null) {
            return ApplyResult.error("TASK_NO_SUGGESTION", "Task has no validated suggestion.");
        }

        ApplicantProfile profile = applicantProfileService.findByApplicantId(userId)
                .orElseGet(() -> buildEmptyProfile(userId));
        mergeSuggestionIntoProfile(profile, task.getSuggestion());
        applicantProfileService.saveProfile(profile);

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

    private AiProfileSuggestion parseSuggestion(String payloadJson, List<String> errors) {
        try {
            JsonNode root = OBJECT_MAPPER.readTree(payloadJson);
            String schemaVersion = readText(root.get("schemaVersion"));
            if (!SCHEMA_VERSION.equals(schemaVersion)) {
                errors.add("schemaVersion must be " + SCHEMA_VERSION + ".");
            }

            JsonNode profile = root.get("profile");
            if (profile == null || !profile.isObject()) {
                errors.add("profile object is required.");
                return null;
            }

            Set<String> seenFields = new HashSet<>();
            profile.fieldNames().forEachRemaining(seenFields::add);
            for (String field : seenFields) {
                if (!PROFILE_FIELD_WHITELIST.contains(field)) {
                    errors.add("Unsupported profile field: " + field);
                }
            }

            AiProfileSuggestion suggestion = new AiProfileSuggestion();
            suggestion.setFullName(readText(profile.get("fullName")));
            suggestion.setStudentId(readText(profile.get("studentId")));
            suggestion.setEmail(readText(profile.get("email")));
            suggestion.setPhone(readText(profile.get("phone")));
            suggestion.setDegreeProgramme(readText(profile.get("degreeProgramme")));
            suggestion.setYearOfStudy(readText(profile.get("yearOfStudy")));
            suggestion.setTaExperience(readText(profile.get("taExperience")));
            suggestion.setProjectOrLeadershipExperience(readText(profile.get("projectOrLeadershipExperience")));
            suggestion.setAvailability(readText(profile.get("availability")));
            suggestion.setRelevantCourses(readStringList(profile.get("relevantCourses"), "relevantCourses", errors));
            suggestion.setSkills(readStringList(profile.get("skills"), "skills", errors));

            if (!ValidationUtil.isBlank(suggestion.getEmail()) && !ValidationUtil.isValidEmail(suggestion.getEmail())) {
                errors.add("email must be a valid email address.");
            }
            if (!ValidationUtil.isBlank(suggestion.getYearOfStudy())
                    && !ValidationUtil.isPositiveInteger(suggestion.getYearOfStudy())) {
                errors.add("yearOfStudy must be a positive integer.");
            }
            return suggestion;
        } catch (Exception exception) {
            errors.add("payload must be valid JSON.");
            return null;
        }
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

    private String buildPromptTemplate(AiImportTask task, String callbackUrl, String cvDownloadUrl) {
        StringBuilder builder = new StringBuilder();
        builder.append("You are extracting a candidate profile from CV text.\n");
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
                .append("  }\n")
                .append("}\n")
                .append("Then call this callback endpoint with POST and raw JSON body:\n")
                .append(callbackUrl).append("\n")
                .append("Set header: X-Callback-Token: ").append(task.getCallbackToken()).append("\n")
                .append("Do not include markdown fences.");
        return builder.toString();
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
}
