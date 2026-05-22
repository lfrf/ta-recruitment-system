package com.group27.tarecruitment.service;

import com.group27.tarecruitment.model.AdminConfig;
import com.group27.tarecruitment.model.ApplicationRecord;
import com.group27.tarecruitment.model.BlacklistEntry;
import com.group27.tarecruitment.model.UserAccount;
import com.group27.tarecruitment.model.Vacancy;
import com.group27.tarecruitment.repository.AdminConfigRepository;
import com.group27.tarecruitment.repository.ApplicationRepository;
import com.group27.tarecruitment.repository.BlacklistRepository;
import com.group27.tarecruitment.repository.VacancyRepository;
import com.group27.tarecruitment.util.ValidationUtil;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * ApplicationService class type.
 *
 * <p>Service type that centralizes business rules and multi-step domain workflows.</p>
 * <p>Package: {@code com.group27.tarecruitment.service}</p>
 */
public class ApplicationService {
    private final AdminConfigRepository adminConfigRepository = new AdminConfigRepository();
    private final ApplicationRepository applicationRepository = new ApplicationRepository();
    private final BlacklistRepository blacklistRepository = new BlacklistRepository();
    private final VacancyRepository vacancyRepository = new VacancyRepository();

    /**
     * Retrieves data using the provided criteria and current business rules.
     * @return the computed `AdminConfig` value for this operation.
     */
    public AdminConfig getAdminConfig() {
        return adminConfigRepository.load();
    }

    /**
     * Retrieves data using the provided criteria and current business rules.
     * @param applicantId input parameter of type {@code String}.
     * @return a collection containing the computed result elements.
     */
    public List<ApplicationRecord> getApplicationsByApplicant(String applicantId) {
        return applicationRepository.findByApplicantId(applicantId);
    }

    /**
     * Retrieves data using the provided criteria and current business rules.
     * @param vacancyId input parameter of type {@code String}.
     * @return a collection containing the computed result elements.
     */
    public List<ApplicationRecord> getApplicationsByVacancy(String vacancyId) {
        return applicationRepository.findByVacancyId(vacancyId);
    }

    /**
     * Executes business behavior as part of the class contract.
     * @param applicantId input parameter of type {@code String}.
     * @return the computed `int` value for this operation.
     */
    public int countActiveApplications(String applicantId) {
        return (int) getApplicationsByApplicant(applicantId).stream()
                .filter(application -> isActiveStatus(application.getStatus()))
                .count();
    }

    /**
     * Executes business behavior as part of the class contract.
     * @param currentUser input parameter of type {@code UserAccount}.
     * @param vacancyId input parameter of type {@code String}.
     * @return the computed `String` value for this operation.
     */
    public String validateApplication(UserAccount currentUser, String vacancyId) {
        if (currentUser == null) {
            return "Please log in before applying for a vacancy.";
        }
        if (vacancyId == null || vacancyId.isBlank()) {
            return "Vacancy ID is missing.";
        }

        Vacancy vacancy = vacancyRepository.findById(vacancyId).orElse(null);
        if (vacancy == null || !"OPEN".equalsIgnoreCase(vacancy.getStatus())) {
            return "This vacancy is not currently open for application.";
        }

        if (isBlacklisted(currentUser.getUserId())) {
            return "This applicant account is currently blocked from applying.";
        }

        boolean duplicate = applicationRepository.findByApplicantId(currentUser.getUserId()).stream()
                .anyMatch(application -> vacancyId.equals(application.getVacancyId())
                        && !ValidationUtil.STATUS_WITHDRAWN.equalsIgnoreCase(
                        ValidationUtil.normalizeApplicationStatus(application.getStatus())));
        if (duplicate) {
            return "You have already applied for this vacancy.";
        }
        if (isVacancyFull(vacancy)) {
            return "This course job is currently full. No TA places are left.";
        }

        int maxWorkload = getAdminConfig().getMaxWorkload();
        if (countActiveApplications(currentUser.getUserId()) >= maxWorkload) {
            return "You have reached the current application limit of " + maxWorkload + " roles.";
        }

        return null;
    }

    /**
     * Executes business behavior as part of the class contract.
     * @param currentUser input parameter of type {@code UserAccount}.
     * @param vacancyId input parameter of type {@code String}.
     */
    public void submitApplication(UserAccount currentUser, String vacancyId) {
        List<ApplicationRecord> applications = new ArrayList<>(applicationRepository.findAll());

        ApplicationRecord application = new ApplicationRecord();
        application.setApplicationId(UUID.randomUUID().toString());
        application.setVacancyId(vacancyId);
        application.setApplicantId(currentUser.getUserId());
        application.setSubmittedAt(LocalDateTime.now().toString());
        application.setStatus("Submitted");
        application.setReviewNote("");
        application.setOptionalFeedback("");
        application.setDecisionUpdatedAt("");
        application.setDecisionRead(Boolean.TRUE);
        applications.add(application);
        applicationRepository.saveAll(applications);
    }

    /**
     * Applies review or decision outcomes and related status changes.
     * @param applicantId input parameter of type {@code String}.
     * @return the computed `int` value for this operation.
     */
    public int countUnreadDecisions(String applicantId) {
        if (ValidationUtil.isBlank(applicantId)) {
            return 0;
        }
        int count = 0;
        for (ApplicationRecord application : applicationRepository.findByApplicantId(applicantId)) {
            if (isUnreadDecision(application)) {
                count++;
            }
        }
        return count;
    }

    /**
     * Applies review or decision outcomes and related status changes.
     * @param applicantId input parameter of type {@code String}.
     * @return the computed `int` value for this operation.
     */
    public int markAllDecisionsAsRead(String applicantId) {
        if (ValidationUtil.isBlank(applicantId)) {
            return 0;
        }
        List<ApplicationRecord> all = new ArrayList<>(applicationRepository.findAll());
        int updated = 0;
        for (ApplicationRecord application : all) {
            if (!applicantId.equals(application.getApplicantId())) {
                continue;
            }
            if (!isUnreadDecision(application)) {
                continue;
            }
            application.setDecisionRead(Boolean.TRUE);
            updated++;
        }
        if (updated > 0) {
            applicationRepository.saveAll(all);
        }
        return updated;
    }

    /**
     * Removes, archives, or cancels previously created business state.
     * @param currentUser input parameter of type {@code UserAccount}.
     * @param applicationId input parameter of type {@code String}.
     * @return the computed `String` value for this operation.
     */
    public String cancelApplication(UserAccount currentUser, String applicationId) {
        if (currentUser == null) {
            return "Please log in before cancelling an application.";
        }
        if (applicationId == null || applicationId.isBlank()) {
            return "Application ID is missing.";
        }

        List<ApplicationRecord> applications = new ArrayList<>(applicationRepository.findAll());
        ApplicationRecord target = null;
        for (ApplicationRecord application : applications) {
            if (applicationId.equals(application.getApplicationId())
                    && currentUser.getUserId().equals(application.getApplicantId())) {
                target = application;
                break;
            }
        }

        if (target == null) {
            return "The selected application record could not be found.";
        }

        String status = ValidationUtil.normalizeApplicationStatus(target.getStatus());
        if (ValidationUtil.STATUS_WITHDRAWN.equals(status)) {
            return "This application has already been withdrawn.";
        }
        if (ValidationUtil.STATUS_OFFERED.equals(status) || ValidationUtil.STATUS_UNSUCCESSFUL.equals(status)) {
            return "Only applications still under review can be cancelled.";
        }

        target.setStatus(ValidationUtil.STATUS_WITHDRAWN);
        target.setLeadTa(false);
        applicationRepository.saveAll(applications);
        return null;
    }

    /**
     * Evaluates and returns a boolean condition for caller logic.
     * @param applicantId input parameter of type {@code String}.
     * @return true when the condition is met; otherwise false.
     */
    private boolean isBlacklisted(String applicantId) {
        return blacklistRepository.findActiveEntries().stream()
                .map(BlacklistEntry::getApplicantId)
                .anyMatch(applicantId::equals);
    }

    /**
     * Executes business behavior as part of the class contract.
     * @param vacancyId input parameter of type {@code String}.
     * @return the computed `int` value for this operation.
     */
    public int countOfferedApplications(String vacancyId) {
        if (vacancyId == null || vacancyId.isBlank()) {
            return 0;
        }
        return (int) getApplicationsByVacancy(vacancyId).stream()
                .filter(application -> ValidationUtil.STATUS_OFFERED.equals(
                        ValidationUtil.normalizeApplicationStatus(application.getStatus())))
                .count();
    }

    /**
     * Evaluates and returns a boolean condition for caller logic.
     * @param vacancyId input parameter of type {@code String}.
     * @return true when the condition is met; otherwise false.
     */
    public boolean hasAppointedLeadTa(String vacancyId) {
        if (vacancyId == null || vacancyId.isBlank()) {
            return false;
        }
        return getApplicationsByVacancy(vacancyId).stream()
                .anyMatch(application -> application.isLeadTa()
                        && ValidationUtil.STATUS_OFFERED.equals(
                        ValidationUtil.normalizeApplicationStatus(application.getStatus())));
    }

    /**
     * Retrieves data using the provided criteria and current business rules.
     * @return a mapping containing computed key/value results.
     */
    public Map<String, Integer> getOfferedCountByVacancyId() {
        Map<String, Integer> counts = new LinkedHashMap<>();
        for (ApplicationRecord application : applicationRepository.findAll()) {
            if (ValidationUtil.STATUS_OFFERED.equals(
                    ValidationUtil.normalizeApplicationStatus(application.getStatus()))) {
                counts.merge(application.getVacancyId(), 1, Integer::sum);
            }
        }
        return counts;
    }

    /**
     * Evaluates and returns a boolean condition for caller logic.
     * @param vacancyId input parameter of type {@code String}.
     * @return true when the condition is met; otherwise false.
     */
    public boolean isVacancyFull(String vacancyId) {
        Vacancy vacancy = vacancyRepository.findById(vacancyId).orElse(null);
        return isVacancyFull(vacancy);
    }

    /**
     * Evaluates and returns a boolean condition for caller logic.
     * @param vacancy input parameter of type {@code Vacancy}.
     * @return true when the condition is met; otherwise false.
     */
    public boolean isVacancyFull(Vacancy vacancy) {
        if (vacancy == null || vacancy.getPositionCount() <= 0) {
            return false;
        }
        return countOfferedApplications(vacancy.getVacancyId()) >= vacancy.getPositionCount();
    }

    /**
     * Evaluates and returns a boolean condition for caller logic.
     * @param status input parameter of type {@code String}.
     * @return true when the condition is met; otherwise false.
     */
    private boolean isActiveStatus(String status) {
        String normalized = ValidationUtil.normalizeApplicationStatus(status);
        return ValidationUtil.STATUS_SUBMITTED.equals(normalized)
                || ValidationUtil.STATUS_OFFERED.equals(normalized);
    }

    /**
     * Applies review or decision outcomes and related status changes.
     * @param application input parameter of type {@code ApplicationRecord}.
     * @return true when the condition is met; otherwise false.
     */
    private boolean isUnreadDecision(ApplicationRecord application) {
        if (application == null) {
            return false;
        }
        String status = ValidationUtil.normalizeApplicationStatus(application.getStatus());
        if (!ValidationUtil.STATUS_OFFERED.equals(status)
                && !ValidationUtil.STATUS_UNSUCCESSFUL.equals(status)) {
            return false;
        }
        if (ValidationUtil.isBlank(application.getDecisionUpdatedAt())) {
            return false;
        }
        return !Boolean.TRUE.equals(application.getDecisionRead());
    }
}
