package com.group27.tarecruitment.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.group27.tarecruitment.model.AiVacancyRecommendTask;
import com.group27.tarecruitment.model.AiVacancyRecommendation;
import com.group27.tarecruitment.model.ApplicantProfile;
import com.group27.tarecruitment.model.Vacancy;
import com.group27.tarecruitment.repository.AiVacancyRecommendTaskRepository;
import com.group27.tarecruitment.util.ValidationUtil;
import jakarta.servlet.http.HttpServletRequest;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

/**
 * AiVacancyRecommendService class type.
 *
 * <p>Service type that centralizes business rules and multi-step domain workflows.</p>
 * <p>Package: {@code com.group27.tarecruitment.service}</p>
 */
public class AiVacancyRecommendService {
    public static final String SCHEMA_VERSION = "vacancy-rank-v1";
    private static final long TASK_TTL_MILLIS = 15 * 60 * 1000L;
    private static final int MAX_CALLBACK_BODY_CHARS = 200_000;
    private static final int MAX_REASONS_PER_VACANCY = 3;
    private static final int MAX_REASON_LENGTH = 160;
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private final AiVacancyRecommendTaskRepository taskRepository = new AiVacancyRecommendTaskRepository();

    /**
     * Creates and initializes new business data for downstream use.
     * @param userId input parameter of type {@code String}.
     * @param request input parameter of type {@code HttpServletRequest}.
     * @param profile input parameter of type {@code ApplicantProfile}.
     * @param candidateVacancies input parameter of type {@code List<Vacancy>}.
     * @return the computed `TaskCreationResult` value for this operation.
     */
    public TaskCreationResult createTask(String userId,
                                         HttpServletRequest request,
                                         ApplicantProfile profile,
                                         List<Vacancy> candidateVacancies) {
        expireOldTasks();
        long now = Instant.now().toEpochMilli();

        AiVacancyRecommendTask task = new AiVacancyRecommendTask();
        task.setTaskId("ai-rank-task-" + UUID.randomUUID().toString().replace("-", ""));
        task.setUserId(userId);
        task.setStatus(AiVacancyRecommendTask.STATUS_CREATED);
        task.setCallbackToken(UUID.randomUUID().toString().replace("-", ""));
        task.setSchemaVersion(SCHEMA_VERSION);
        task.setCreatedAtEpochMillis(now);
        task.setExpiresAtEpochMillis(now + TASK_TTL_MILLIS);
        task.setEligibleVacancyIds(candidateVacancies.stream()
                .map(Vacancy::getVacancyId)
                .filter(id -> !ValidationUtil.isBlank(id))
                .toList());
        task.setValidationErrors(new ArrayList<>());
        taskRepository.save(task);

        String callbackUrl = buildAbsoluteUrl(request, request.getContextPath()
                + "/ai/recommend/callback?taskId=" + task.getTaskId());
        String prompt = buildPromptTemplate(task, profile, candidateVacancies, callbackUrl);
        return new TaskCreationResult(task, callbackUrl, prompt);
    }

    /**
     * Retrieves data using the provided criteria and current business rules.
     * @param userId input parameter of type {@code String}.
     * @param taskId input parameter of type {@code String}.
     * @return an optional result that is present when data is available.
     */
    public Optional<AiVacancyRecommendTask> findTaskForUser(String userId, String taskId) {
        expireOldTasks();
        return taskRepository.findById(taskId)
                .filter(task -> userId.equals(task.getUserId()));
    }

    /**
     * Retrieves data using the provided criteria and current business rules.
     * @param userId input parameter of type {@code String}.
     * @return an optional result that is present when data is available.
     */
    public Optional<AiVacancyRecommendTask> findLatestValidatedTaskForUser(String userId) {
        expireOldTasks();
        return taskRepository.findAll().stream()
                .filter(task -> userId.equals(task.getUserId()))
                .filter(task -> AiVacancyRecommendTask.STATUS_VALIDATED.equals(task.getStatus()))
                .max(Comparator.comparingLong(this::validatedSortKey));
    }

    /**
     * Executes business behavior as part of the class contract.
     * @param task input parameter of type {@code AiVacancyRecommendTask}.
     * @return the computed `long` value for this operation.
     */
    private long validatedSortKey(AiVacancyRecommendTask task) {
        if (task.getValidatedAtEpochMillis() != null) {
            return task.getValidatedAtEpochMillis();
        }
        return task.getCreatedAtEpochMillis();
    }

    /**
     * Executes business behavior as part of the class contract.
     * @param taskId input parameter of type {@code String}.
     * @param callbackToken input parameter of type {@code String}.
     * @param payloadJson input parameter of type {@code String}.
     * @return the computed `CallbackResult` value for this operation.
     */
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

        Optional<AiVacancyRecommendTask> optionalTask = taskRepository.findById(taskId);
        if (optionalTask.isEmpty()) {
            return CallbackResult.error("TASK_NOT_FOUND", "Task does not exist.");
        }

        AiVacancyRecommendTask task = optionalTask.get();
        if (!callbackToken.equals(task.getCallbackToken())) {
            return CallbackResult.error("INVALID_TOKEN", "Callback token does not match.");
        }
        if (AiVacancyRecommendTask.STATUS_EXPIRED.equals(task.getStatus())) {
            return CallbackResult.error("TASK_EXPIRED", "Task has expired.");
        }

        task.setStatus(AiVacancyRecommendTask.STATUS_RECEIVED);
        task.setReceivedAtEpochMillis(Instant.now().toEpochMilli());
        task.setRawPayloadJson(payloadJson);

        List<String> errors = new ArrayList<>();
        List<AiVacancyRecommendation> recommendations = parseRecommendations(payloadJson, task, errors);
        if (!errors.isEmpty()) {
            task.setStatus(AiVacancyRecommendTask.STATUS_FAILED);
            task.setValidationErrors(errors);
            taskRepository.save(task);
            return CallbackResult.error("VALIDATION_FAILED", "Payload failed schema validation.");
        }

        task.setRecommendations(recommendations);
        task.setValidatedAtEpochMillis(Instant.now().toEpochMilli());
        task.setStatus(AiVacancyRecommendTask.STATUS_VALIDATED);
        task.setValidationErrors(new ArrayList<>());
        taskRepository.save(task);
        return CallbackResult.ok();
    }

    /**
     * Executes business behavior as part of the class contract.
     */
    public void expireOldTasks() {
        long now = Instant.now().toEpochMilli();
        List<AiVacancyRecommendTask> tasks = new ArrayList<>(taskRepository.findAll());
        boolean changed = false;
        for (AiVacancyRecommendTask task : tasks) {
            if (task.getExpiresAtEpochMillis() <= now
                    && !AiVacancyRecommendTask.STATUS_EXPIRED.equals(task.getStatus())
                    && !AiVacancyRecommendTask.STATUS_VALIDATED.equals(task.getStatus())) {
                task.setStatus(AiVacancyRecommendTask.STATUS_EXPIRED);
                changed = true;
            }
        }
        if (changed) {
            taskRepository.saveAll(tasks);
        }
    }

    /**
     * Executes business behavior as part of the class contract.
     * @param payloadJson input parameter of type {@code String}.
     * @param task input parameter of type {@code AiVacancyRecommendTask}.
     * @param errors input parameter of type {@code List<String>}.
     * @return a collection containing the computed result elements.
     */
    private List<AiVacancyRecommendation> parseRecommendations(String payloadJson,
                                                               AiVacancyRecommendTask task,
                                                               List<String> errors) {
        try {
            JsonNode root = OBJECT_MAPPER.readTree(payloadJson);
            String schemaVersion = readText(root.get("schemaVersion"));
            if (!SCHEMA_VERSION.equals(schemaVersion)) {
                errors.add("schemaVersion must be " + SCHEMA_VERSION + ".");
            }

            JsonNode rankingsNode = root.get("rankings");
            if (rankingsNode == null || !rankingsNode.isArray()) {
                errors.add("rankings array is required.");
                return new ArrayList<>();
            }

            Set<String> allowedIds = new LinkedHashSet<>(task.getEligibleVacancyIds() == null
                    ? List.of()
                    : task.getEligibleVacancyIds());
            Set<String> seenIds = new HashSet<>();
            List<AiVacancyRecommendation> recommendations = new ArrayList<>();
            for (JsonNode itemNode : rankingsNode) {
                if (!itemNode.isObject()) {
                    errors.add("Each rankings item must be an object.");
                    return new ArrayList<>();
                }
                AiVacancyRecommendation recommendation = new AiVacancyRecommendation();
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

                List<String> reasons = readReasons(itemNode.get("reasons"), errors, vacancyId);
                recommendation.setVacancyId(vacancyId);
                recommendation.setScore(score);
                recommendation.setReasons(reasons);
                recommendations.add(recommendation);
            }

            if (recommendations.isEmpty()) {
                errors.add("rankings must contain at least one valid item.");
            }
            return recommendations;
        } catch (Exception exception) {
            errors.add("payload must be valid JSON.");
            return new ArrayList<>();
        }
    }

    /**
     * Executes business behavior as part of the class contract.
     * @param reasonsNode input parameter of type {@code JsonNode}.
     * @param errors input parameter of type {@code List<String>}.
     * @param vacancyId input parameter of type {@code String}.
     * @return a collection containing the computed result elements.
     */
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

    /**
     * Executes business behavior as part of the class contract.
     * @param scoreNode input parameter of type {@code JsonNode}.
     * @return the computed `Integer` value for this operation.
     */
    private Integer readScore(JsonNode scoreNode) {
        if (scoreNode == null || scoreNode.isNull() || !scoreNode.isNumber()) {
            return null;
        }
        return scoreNode.asInt();
    }

    /**
     * Executes business behavior as part of the class contract.
     * @param node input parameter of type {@code JsonNode}.
     * @return the computed `String` value for this operation.
     */
    private String readText(JsonNode node) {
        if (node == null || node.isNull() || !node.isTextual()) {
            return "";
        }
        return ValidationUtil.trimToEmpty(node.asText());
    }

    /**
     * Executes business behavior as part of the class contract.
     * @param request input parameter of type {@code HttpServletRequest}.
     * @param path input parameter of type {@code String}.
     * @return the computed `String` value for this operation.
     */
    private String buildAbsoluteUrl(HttpServletRequest request, String path) {
        String scheme = request.getScheme();
        String serverName = request.getServerName();
        int port = request.getServerPort();
        boolean defaultPort = ("http".equalsIgnoreCase(scheme) && port == 80)
                || ("https".equalsIgnoreCase(scheme) && port == 443);
        return scheme + "://" + serverName + (defaultPort ? "" : ":" + port) + path;
    }

    /**
     * Executes business behavior as part of the class contract.
     * @param task input parameter of type {@code AiVacancyRecommendTask}.
     * @param profile input parameter of type {@code ApplicantProfile}.
     * @param candidateVacancies input parameter of type {@code List<Vacancy>}.
     * @param callbackUrl input parameter of type {@code String}.
     * @return the computed `String` value for this operation.
     */
    private String buildPromptTemplate(AiVacancyRecommendTask task,
                                       ApplicantProfile profile,
                                       List<Vacancy> candidateVacancies,
                                       String callbackUrl) {
        StringBuilder builder = new StringBuilder();
        builder.append("You are ranking TA vacancies for one applicant.\n");
        builder.append("Output ONLY JSON, no markdown fences.\n");
        builder.append("Use this exact top-level shape:\n");
        builder.append("{\n");
        builder.append("  \"schemaVersion\": \"").append(SCHEMA_VERSION).append("\",\n");
        builder.append("  \"rankings\": [\n");
        builder.append("    {\"vacancyId\": \"\", \"score\": 0, \"reasons\": [\"\"]}\n");
        builder.append("  ]\n");
        builder.append("}\n");
        builder.append("Score range: 0-100. Higher means better fit.\n");
        builder.append("Only include vacancyId values from the provided list.\n");
        builder.append("Return up to 12 items sorted by score descending.\n");
        builder.append("\nApplicant profile:\n");
        builder.append(buildProfileSummary(profile)).append("\n");
        builder.append("\nVacancy list:\n");
        builder.append(buildVacancySummary(candidateVacancies)).append("\n");
        builder.append("\nThen call this callback endpoint with POST and raw JSON body:\n");
        builder.append(callbackUrl).append("\n");
        builder.append("Set header: X-Callback-Token: ").append(task.getCallbackToken()).append("\n");
        return builder.toString();
    }

    /**
     * Executes business behavior as part of the class contract.
     * @param profile input parameter of type {@code ApplicantProfile}.
     * @return the computed `String` value for this operation.
     */
    private String buildProfileSummary(ApplicantProfile profile) {
        if (profile == null) {
            return "No profile provided.";
        }
        StringBuilder builder = new StringBuilder();
        builder.append("fullName: ").append(ValidationUtil.trimToEmpty(profile.getFullName())).append("\n");
        builder.append("studentId: ").append(ValidationUtil.trimToEmpty(profile.getStudentId())).append("\n");
        builder.append("email: ").append(ValidationUtil.trimToEmpty(profile.getEmail())).append("\n");
        builder.append("degreeProgramme: ").append(ValidationUtil.trimToEmpty(profile.getDegreeProgramme())).append("\n");
        builder.append("yearOfStudy: ").append(ValidationUtil.trimToEmpty(profile.getYearOfStudy())).append("\n");
        builder.append("relevantCourses: ").append(profile.getRelevantCourses() == null ? List.of() : profile.getRelevantCourses()).append("\n");
        builder.append("skills: ").append(profile.getSkills() == null ? List.of() : profile.getSkills()).append("\n");
        builder.append("taExperience: ").append(ValidationUtil.trimToEmpty(profile.getTaExperience())).append("\n");
        builder.append("projectOrLeadershipExperience: ")
                .append(ValidationUtil.trimToEmpty(profile.getProjectOrLeadershipExperience())).append("\n");
        builder.append("availability: ").append(ValidationUtil.trimToEmpty(profile.getAvailability())).append("\n");
        return builder.toString();
    }

    /**
     * Executes business behavior as part of the class contract.
     * @param vacancies input parameter of type {@code List<Vacancy>}.
     * @return the computed `String` value for this operation.
     */
    private String buildVacancySummary(List<Vacancy> vacancies) {
        StringBuilder builder = new StringBuilder();
        for (Vacancy vacancy : vacancies) {
            builder.append("- vacancyId=").append(ValidationUtil.trimToEmpty(vacancy.getVacancyId()))
                    .append("; moduleCode=").append(ValidationUtil.trimToEmpty(vacancy.getModuleCode()))
                    .append("; moduleName=").append(ValidationUtil.trimToEmpty(vacancy.getModuleName()))
                    .append("; campus=").append(ValidationUtil.trimToEmpty(vacancy.getCampus()))
                    .append("; requiredSkills=").append(vacancy.getRequiredSkills() == null ? List.of() : vacancy.getRequiredSkills())
                    .append("; description=").append(ValidationUtil.trimToEmpty(vacancy.getDescription()))
                    .append("\n");
        }
        return builder.toString();
    }

    public static class TaskCreationResult {
        private final AiVacancyRecommendTask task;
        private final String callbackUrl;
        private final String promptTemplate;

        public TaskCreationResult(AiVacancyRecommendTask task, String callbackUrl, String promptTemplate) {
            this.task = task;
            this.callbackUrl = callbackUrl;
            this.promptTemplate = promptTemplate;
        }

        /**
         * Retrieves data using the provided criteria and current business rules.
         * @return the computed `AiVacancyRecommendTask` value for this operation.
         */
        public AiVacancyRecommendTask getTask() {
            return task;
        }

        /**
         * Retrieves data using the provided criteria and current business rules.
         * @return the computed `String` value for this operation.
         */
        public String getCallbackUrl() {
            return callbackUrl;
        }

        /**
         * Retrieves data using the provided criteria and current business rules.
         * @return the computed `String` value for this operation.
         */
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

        /**
         * Executes business behavior as part of the class contract.
         * @return the computed `CallbackResult` value for this operation.
         */
        public static CallbackResult ok() {
            return new CallbackResult(true, "OK", "Accepted");
        }

        /**
         * Executes business behavior as part of the class contract.
         * @param code input parameter of type {@code String}.
         * @param message input parameter of type {@code String}.
         * @return the computed `CallbackResult` value for this operation.
         */
        public static CallbackResult error(String code, String message) {
            return new CallbackResult(false, code, message);
        }

        /**
         * Evaluates and returns a boolean condition for caller logic.
         * @return true when the condition is met; otherwise false.
         */
        public boolean isOk() {
            return ok;
        }

        /**
         * Retrieves data using the provided criteria and current business rules.
         * @return the computed `String` value for this operation.
         */
        public String getCode() {
            return code;
        }

        /**
         * Retrieves data using the provided criteria and current business rules.
         * @return the computed `String` value for this operation.
         */
        public String getMessage() {
            return message;
        }
    }
}

