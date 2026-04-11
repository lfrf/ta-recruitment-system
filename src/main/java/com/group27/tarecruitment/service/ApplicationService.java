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

public class ApplicationService {
    private final AdminConfigRepository adminConfigRepository = new AdminConfigRepository();
    private final ApplicationRepository applicationRepository = new ApplicationRepository();
    private final BlacklistRepository blacklistRepository = new BlacklistRepository();
    private final VacancyRepository vacancyRepository = new VacancyRepository();

    public AdminConfig getAdminConfig() {
        return adminConfigRepository.load();
    }

    public List<ApplicationRecord> getApplicationsByApplicant(String applicantId) {
        return applicationRepository.findByApplicantId(applicantId);
    }

    public List<ApplicationRecord> getApplicationsByVacancy(String vacancyId) {
        return applicationRepository.findByVacancyId(vacancyId);
    }

    public int countActiveApplications(String applicantId) {
        return (int) getApplicationsByApplicant(applicantId).stream()
                .filter(application -> isActiveStatus(application.getStatus()))
                .count();
    }

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
        applications.add(application);
        applicationRepository.saveAll(applications);
    }

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

    private boolean isBlacklisted(String applicantId) {
        return blacklistRepository.findActiveEntries().stream()
                .map(BlacklistEntry::getApplicantId)
                .anyMatch(applicantId::equals);
    }

    public int countOfferedApplications(String vacancyId) {
        if (vacancyId == null || vacancyId.isBlank()) {
            return 0;
        }
        return (int) getApplicationsByVacancy(vacancyId).stream()
                .filter(application -> ValidationUtil.STATUS_OFFERED.equals(
                        ValidationUtil.normalizeApplicationStatus(application.getStatus())))
                .count();
    }

    public boolean hasAppointedLeadTa(String vacancyId) {
        if (vacancyId == null || vacancyId.isBlank()) {
            return false;
        }
        return getApplicationsByVacancy(vacancyId).stream()
                .anyMatch(application -> application.isLeadTa()
                        && ValidationUtil.STATUS_OFFERED.equals(
                        ValidationUtil.normalizeApplicationStatus(application.getStatus())));
    }

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

    public boolean isVacancyFull(String vacancyId) {
        Vacancy vacancy = vacancyRepository.findById(vacancyId).orElse(null);
        return isVacancyFull(vacancy);
    }

    public boolean isVacancyFull(Vacancy vacancy) {
        if (vacancy == null || vacancy.getPositionCount() <= 0) {
            return false;
        }
        return countOfferedApplications(vacancy.getVacancyId()) >= vacancy.getPositionCount();
    }

    private boolean isActiveStatus(String status) {
        String normalized = ValidationUtil.normalizeApplicationStatus(status);
        return ValidationUtil.STATUS_SUBMITTED.equals(normalized)
                || ValidationUtil.STATUS_OFFERED.equals(normalized);
    }
}
