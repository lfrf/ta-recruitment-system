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
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
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
                .filter(application -> !"Unsuccessful".equalsIgnoreCase(application.getStatus()))
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
                .anyMatch(application -> vacancyId.equals(application.getVacancyId()));
        if (duplicate) {
            return "You have already applied for this vacancy.";
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

        List<Vacancy> vacancies = new ArrayList<>(vacancyRepository.findAll());
        for (Vacancy vacancy : vacancies) {
            if (vacancyId.equals(vacancy.getVacancyId())) {
                vacancy.setApplicantCount(vacancy.getApplicantCount() + 1);
                break;
            }
        }
        vacancyRepository.saveAll(vacancies);
    }

    private boolean isBlacklisted(String applicantId) {
        return blacklistRepository.findActiveEntries().stream()
                .map(BlacklistEntry::getApplicantId)
                .anyMatch(applicantId::equals);
    }
}
