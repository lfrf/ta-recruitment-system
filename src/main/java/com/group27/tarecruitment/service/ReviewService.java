package com.group27.tarecruitment.service;

import com.group27.tarecruitment.model.ApplicationRecord;
import com.group27.tarecruitment.model.AiImportTask;
import com.group27.tarecruitment.model.AiVacancyRecommendation;
import com.group27.tarecruitment.model.UserAccount;
import com.group27.tarecruitment.model.Vacancy;
import com.group27.tarecruitment.repository.AiImportTaskRepository;
import com.group27.tarecruitment.repository.ApplicationRepository;
import com.group27.tarecruitment.repository.VacancyRepository;
import com.group27.tarecruitment.util.ValidationUtil;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * ReviewService class type.
 *
 * <p>Service type that centralizes business rules and multi-step domain workflows.</p>
 * <p>Package: {@code com.group27.tarecruitment.service}</p>
 */
public class ReviewService {
    public static final String ORDER_MODE_DEFAULT = "default";
    public static final String ORDER_MODE_AI = "ai";

    private final ApplicationRepository applicationRepository = new ApplicationRepository();
    private final VacancyRepository vacancyRepository = new VacancyRepository();
    private final AiImportTaskRepository aiImportTaskRepository = new AiImportTaskRepository();

    /**
     * Retrieves data using the provided criteria and current business rules.
     * @param organiser input parameter of type {@code UserAccount}.
     * @return a collection containing the computed result elements.
     */
    public List<Vacancy> getManagedVacancies(UserAccount organiser) {
        return vacancyRepository.findAll().stream()
                .filter(vacancy -> vacancy.getCreatedBy() != null
                        && (vacancy.getCreatedBy().equalsIgnoreCase(organiser.getUsername())
                        || vacancy.getCreatedBy().equalsIgnoreCase(organiser.getUserId())))
                .filter(vacancy -> isManageableStatus(vacancy.getStatus()))
                .toList();
    }

    /**
     * Retrieves data using the provided criteria and current business rules.
     * @param organiser input parameter of type {@code UserAccount}.
     * @param vacancyId input parameter of type {@code String}.
     * @return an optional result that is present when data is available.
     */
    public Optional<Vacancy> getManagedVacancy(UserAccount organiser, String vacancyId) {
        return getManagedVacancies(organiser).stream()
                .filter(vacancy -> vacancy.getVacancyId().equals(vacancyId))
                .findFirst();
    }

    /**
     * Retrieves data using the provided criteria and current business rules.
     * @param vacancyId input parameter of type {@code String}.
     * @return a collection containing the computed result elements.
     */
    public List<ApplicationRecord> getApplicationsForVacancy(String vacancyId) {
        return applicationRepository.findByVacancyId(vacancyId).stream()
                .filter(application -> !ValidationUtil.STATUS_WITHDRAWN.equalsIgnoreCase(
                        ValidationUtil.normalizeApplicationStatus(application.getStatus())))
                .toList();
    }

    /**
     * Executes business behavior as part of the class contract.
     * @param rawMode input parameter of type {@code String}.
     * @return the computed `String` value for this operation.
     */
    public String normalizeOrderMode(String rawMode) {
        return ORDER_MODE_AI.equalsIgnoreCase(ValidationUtil.trimToEmpty(rawMode))
                ? ORDER_MODE_AI
                : ORDER_MODE_DEFAULT;
    }

    /**
     * Applies review or decision outcomes and related status changes.
     * @param applications input parameter of type {@code List<ApplicationRecord>}.
     * @param aiFitByApplicantId input parameter of type {@code Map<String, ApplicantAiFit>}.
     * @param orderMode input parameter of type {@code String}.
     * @return a collection containing the computed result elements.
     */
    public List<ApplicationRecord> sortApplicationsForReview(List<ApplicationRecord> applications,
                                                             Map<String, ApplicantAiFit> aiFitByApplicantId,
                                                             String orderMode) {
        List<ApplicationRecord> sorted = new ArrayList<>(applications == null ? List.of() : applications);
        Comparator<ApplicationRecord> comparator = ORDER_MODE_AI.equalsIgnoreCase(orderMode)
                ? aiReviewComparator(aiFitByApplicantId)
                : defaultReviewComparator();
        sorted.sort(comparator);
        return sorted;
    }

    /**
     * Retrieves data using the provided criteria and current business rules.
     * @param applications input parameter of type {@code List<ApplicationRecord>}.
     * @param vacancyId input parameter of type {@code String}.
     * @return a mapping containing computed key/value results.
     */
    public Map<String, ApplicantAiFit> getApplicantAiFitForVacancy(List<ApplicationRecord> applications, String vacancyId) {
        if (applications == null || applications.isEmpty() || ValidationUtil.isBlank(vacancyId)) {
            return new LinkedHashMap<>();
        }

        Set<String> applicantIds = applications.stream()
                .map(ApplicationRecord::getApplicantId)
                .filter(id -> !ValidationUtil.isBlank(id))
                .collect(Collectors.toSet());
        if (applicantIds.isEmpty()) {
            return new LinkedHashMap<>();
        }

        Map<String, AiImportTask> latestTaskByApplicantId = new LinkedHashMap<>();
        for (AiImportTask task : aiImportTaskRepository.findAll()) {
            String userId = ValidationUtil.trimToEmpty(task.getUserId());
            if (!applicantIds.contains(userId)) {
                continue;
            }
            if (!AiImportTask.IMPORT_STATUS_VALIDATED.equals(task.getRankingStatus())) {
                continue;
            }
            if (task.getRecommendations() == null || task.getRecommendations().isEmpty()) {
                continue;
            }

            AiImportTask existing = latestTaskByApplicantId.get(userId);
            if (existing == null || aiTaskSortKey(task) > aiTaskSortKey(existing)) {
                latestTaskByApplicantId.put(userId, task);
            }
        }

        Map<String, ApplicantAiFit> result = new LinkedHashMap<>();
        for (String applicantId : applicantIds) {
            AiImportTask task = latestTaskByApplicantId.get(applicantId);
            if (task == null || task.getRecommendations() == null) {
                continue;
            }
            AiVacancyRecommendation match = task.getRecommendations().stream()
                    .filter(item -> vacancyId.equals(ValidationUtil.trimToEmpty(item.getVacancyId())))
                    .findFirst()
                    .orElse(null);
            if (match == null || match.getScore() == null) {
                continue;
            }

            ApplicantAiFit fit = new ApplicantAiFit();
            fit.setScore(match.getScore());
            fit.setReasons(match.getReasons() == null ? List.of() : match.getReasons());
            fit.setTaskId(task.getTaskId());
            fit.setValidatedAtEpochMillis(task.getValidatedAtEpochMillis());
            result.put(applicantId, fit);
        }
        return result;
    }

    /**
     * Updates existing state while preserving consistency constraints.
     * @param organiser input parameter of type {@code UserAccount}.
     * @param vacancyId input parameter of type {@code String}.
     * @param applicationId input parameter of type {@code String}.
     * @param decision input parameter of type {@code String}.
     * @param reviewNote input parameter of type {@code String}.
     * @param optionalFeedback input parameter of type {@code String}.
     * @param appointLeadTa input parameter of type {@code boolean}.
     * @return the computed `String` value for this operation.
     */
    public String updateDecision(UserAccount organiser,
                                 String vacancyId,
                                 String applicationId,
                                 String decision,
                                 String reviewNote,
                                 String optionalFeedback,
                                 boolean appointLeadTa) {
        if (organiser == null) {
            return "Please log in before updating a review decision.";
        }

        vacancyId = ValidationUtil.trimToEmpty(vacancyId);
        applicationId = ValidationUtil.trimToEmpty(applicationId);
        decision = ValidationUtil.normalizeApplicationStatus(decision);
        reviewNote = ValidationUtil.trimToEmpty(reviewNote);
        optionalFeedback = ValidationUtil.trimToEmpty(optionalFeedback);

        if (ValidationUtil.isBlank(vacancyId) || ValidationUtil.isBlank(applicationId)) {
            return "Vacancy ID and application ID are required.";
        }

        Vacancy vacancy = getManagedVacancy(organiser, vacancyId).orElse(null);
        if (vacancy == null) {
            return "You cannot review a course job that is not managed by your account.";
        }

        if (!ValidationUtil.STATUS_OFFERED.equals(decision)
                && !ValidationUtil.STATUS_UNSUCCESSFUL.equals(decision)) {
            return "Decision must be either Offered or Unsuccessful.";
        }

        if (ValidationUtil.isBlank(reviewNote)) {
            reviewNote = ValidationUtil.STATUS_OFFERED.equals(decision)
                    ? "Offer decision recorded by organiser."
                    : "Unsuccessful decision recorded by organiser.";
        }

        if (appointLeadTa && !ValidationUtil.STATUS_OFFERED.equals(decision)) {
            return "Only offered applicants can be appointed as the lead TA.";
        }

        if (appointLeadTa && !vacancy.isLeaderRoleAvailable()) {
            return "This course job was published without a lead TA appointment slot.";
        }

        List<ApplicationRecord> applications = new ArrayList<>(applicationRepository.findAll());
        ApplicationRecord target = null;
        int offeredElsewhere = 0;
        for (ApplicationRecord application : applications) {
            if (vacancyId.equals(application.getVacancyId())
                    && ValidationUtil.STATUS_OFFERED.equalsIgnoreCase(ValidationUtil.normalizeApplicationStatus(application.getStatus()))
                    && !applicationId.equals(application.getApplicationId())) {
                offeredElsewhere++;
            }
            if (applicationId.equals(application.getApplicationId()) && vacancyId.equals(application.getVacancyId())) {
                target = application;
            }
        }

        if (target == null) {
            return "The selected application record could not be found.";
        }

        if (ValidationUtil.STATUS_OFFERED.equals(decision)
                && vacancy.getPositionCount() > 0
                && offeredElsewhere >= vacancy.getPositionCount()) {
            return "This course already has the maximum number of offered TA places.";
        }

        if (appointLeadTa) {
            for (ApplicationRecord application : applications) {
                if (vacancyId.equals(application.getVacancyId())) {
                    application.setLeadTa(false);
                }
            }
        }

        target.setStatus(decision);
        target.setReviewNote(reviewNote);
        target.setOptionalFeedback(optionalFeedback);
        target.setLeadTa(appointLeadTa && ValidationUtil.STATUS_OFFERED.equals(decision));
        target.setDecisionUpdatedAt(LocalDateTime.now().toString());
        target.setDecisionRead(Boolean.FALSE);

        if (ValidationUtil.STATUS_UNSUCCESSFUL.equals(decision)) {
            target.setLeadTa(false);
        }

        applicationRepository.saveAll(applications);
        syncVacancyOpenStatus(vacancy, applications);
        return null;
    }

    /**
     * Executes business behavior as part of the class contract.
     * @param vacancy input parameter of type {@code Vacancy}.
     * @param applications input parameter of type {@code List<ApplicationRecord>}.
     */
    private void syncVacancyOpenStatus(Vacancy vacancy, List<ApplicationRecord> applications) {
        if (vacancy == null || vacancy.getPositionCount() <= 0) {
            return;
        }

        int offeredCount = 0;
        for (ApplicationRecord application : applications) {
            if (vacancy.getVacancyId().equals(application.getVacancyId())
                    && ValidationUtil.STATUS_OFFERED.equals(
                    ValidationUtil.normalizeApplicationStatus(application.getStatus()))) {
                offeredCount++;
            }
        }

        String targetStatus = offeredCount >= vacancy.getPositionCount() ? "CLOSED" : "OPEN";
        if (targetStatus.equalsIgnoreCase(ValidationUtil.trimToEmpty(vacancy.getStatus()))) {
            return;
        }
        vacancy.setStatus(targetStatus);

        List<Vacancy> vacancies = new ArrayList<>(vacancyRepository.findAll());
        for (Vacancy item : vacancies) {
            if (vacancy.getVacancyId().equals(item.getVacancyId())) {
                item.setStatus(targetStatus);
                break;
            }
        }
        vacancyRepository.saveAll(vacancies);
    }

    /**
     * Evaluates and returns a boolean condition for caller logic.
     * @param status input parameter of type {@code String}.
     * @return true when the condition is met; otherwise false.
     */
    private boolean isManageableStatus(String status) {
        String normalized = ValidationUtil.trimToEmpty(status);
        return "OPEN".equalsIgnoreCase(normalized) || "CLOSED".equalsIgnoreCase(normalized);
    }

    /**
     * Applies review or decision outcomes and related status changes.
     * @return the computed `Comparator<ApplicationRecord>` value for this operation.
     */
    private Comparator<ApplicationRecord> defaultReviewComparator() {
        return Comparator.comparingInt(this::reviewStatusPriority)
                .thenComparingLong(this::submittedAtSortKey)
                .thenComparing(item -> ValidationUtil.trimToEmpty(item.getApplicationId()), String.CASE_INSENSITIVE_ORDER);
    }

    /**
     * Applies review or decision outcomes and related status changes.
     * @param aiFitByApplicantId input parameter of type {@code Map<String, ApplicantAiFit>}.
     * @return the computed `Comparator<ApplicationRecord>` value for this operation.
     */
    private Comparator<ApplicationRecord> aiReviewComparator(Map<String, ApplicantAiFit> aiFitByApplicantId) {
        return Comparator.comparingInt(this::reviewStatusPriority)
                .thenComparingInt(item -> aiPresencePriority(item, aiFitByApplicantId))
                .thenComparingInt(item -> aiScoreSortKey(item, aiFitByApplicantId))
                .thenComparingLong(this::submittedAtSortKey)
                .thenComparing(item -> ValidationUtil.trimToEmpty(item.getApplicationId()), String.CASE_INSENSITIVE_ORDER);
    }

    /**
     * Applies review or decision outcomes and related status changes.
     * @param application input parameter of type {@code ApplicationRecord}.
     * @return the computed `int` value for this operation.
     */
    private int reviewStatusPriority(ApplicationRecord application) {
        String status = ValidationUtil.normalizeApplicationStatus(application.getStatus());
        if (ValidationUtil.STATUS_SUBMITTED.equals(status)) {
            return 0;
        }
        if (ValidationUtil.STATUS_OFFERED.equals(status)) {
            return 1;
        }
        if (ValidationUtil.STATUS_UNSUCCESSFUL.equals(status)) {
            return 2;
        }
        return 3;
    }

    /**
     * Executes business behavior as part of the class contract.
     * @param application input parameter of type {@code ApplicationRecord}.
     * @param aiFitByApplicantId input parameter of type {@code Map<String, ApplicantAiFit>}.
     * @return the computed `int` value for this operation.
     */
    private int aiPresencePriority(ApplicationRecord application, Map<String, ApplicantAiFit> aiFitByApplicantId) {
        if (reviewStatusPriority(application) != 0) {
            return 0;
        }
        ApplicantAiFit fit = aiFitByApplicantId == null ? null : aiFitByApplicantId.get(application.getApplicantId());
        return fit == null || fit.getScore() == null ? 1 : 0;
    }

    /**
     * Executes business behavior as part of the class contract.
     * @param application input parameter of type {@code ApplicationRecord}.
     * @param aiFitByApplicantId input parameter of type {@code Map<String, ApplicantAiFit>}.
     * @return the computed `int` value for this operation.
     */
    private int aiScoreSortKey(ApplicationRecord application, Map<String, ApplicantAiFit> aiFitByApplicantId) {
        if (reviewStatusPriority(application) != 0) {
            return 0;
        }
        ApplicantAiFit fit = aiFitByApplicantId == null ? null : aiFitByApplicantId.get(application.getApplicantId());
        if (fit == null || fit.getScore() == null) {
            return 0;
        }
        return -fit.getScore();
    }

    /**
     * Executes business behavior as part of the class contract.
     * @param application input parameter of type {@code ApplicationRecord}.
     * @return the computed `long` value for this operation.
     */
    private long submittedAtSortKey(ApplicationRecord application) {
        String submittedAt = ValidationUtil.trimToEmpty(application.getSubmittedAt());
        if (submittedAt.isEmpty()) {
            return Long.MAX_VALUE;
        }
        try {
            return LocalDateTime.parse(submittedAt)
                    .atZone(ZoneId.systemDefault())
                    .toInstant()
                    .toEpochMilli();
        } catch (DateTimeParseException ignored) {
            return Long.MAX_VALUE;
        }
    }

    /**
     * Executes business behavior as part of the class contract.
     * @param task input parameter of type {@code AiImportTask}.
     * @return the computed `long` value for this operation.
     */
    private long aiTaskSortKey(AiImportTask task) {
        if (task.getValidatedAtEpochMillis() != null) {
            return task.getValidatedAtEpochMillis();
        }
        return task.getCreatedAtEpochMillis();
    }

    public static class ApplicantAiFit {
        private Integer score;
        private List<String> reasons;
        private String taskId;
        private Long validatedAtEpochMillis;

        /**
         * Retrieves data using the provided criteria and current business rules.
         * @return the computed `Integer` value for this operation.
         */
        public Integer getScore() {
            return score;
        }

        /**
         * Updates existing state while preserving consistency constraints.
         * @param score input parameter of type {@code Integer}.
         */
        public void setScore(Integer score) {
            this.score = score;
        }

        /**
         * Retrieves data using the provided criteria and current business rules.
         * @return a collection containing the computed result elements.
         */
        public List<String> getReasons() {
            return reasons;
        }

        /**
         * Updates existing state while preserving consistency constraints.
         * @param reasons input parameter of type {@code List<String>}.
         */
        public void setReasons(List<String> reasons) {
            this.reasons = reasons;
        }

        /**
         * Retrieves data using the provided criteria and current business rules.
         * @return the computed `String` value for this operation.
         */
        public String getTaskId() {
            return taskId;
        }

        /**
         * Updates existing state while preserving consistency constraints.
         * @param taskId input parameter of type {@code String}.
         */
        public void setTaskId(String taskId) {
            this.taskId = taskId;
        }

        /**
         * Retrieves data using the provided criteria and current business rules.
         * @return the computed `Long` value for this operation.
         */
        public Long getValidatedAtEpochMillis() {
            return validatedAtEpochMillis;
        }

        /**
         * Updates existing state while preserving consistency constraints.
         * @param validatedAtEpochMillis input parameter of type {@code Long}.
         */
        public void setValidatedAtEpochMillis(Long validatedAtEpochMillis) {
            this.validatedAtEpochMillis = validatedAtEpochMillis;
        }
    }
}
