package com.group27.tarecruitment.service;

import com.group27.tarecruitment.model.ApplicationRecord;
import com.group27.tarecruitment.model.UserAccount;
import com.group27.tarecruitment.model.Vacancy;
import com.group27.tarecruitment.repository.ApplicationRepository;
import com.group27.tarecruitment.repository.VacancyRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import com.group27.tarecruitment.util.ValidationUtil;

public class ReviewService {
    private final ApplicationRepository applicationRepository = new ApplicationRepository();
    private final VacancyRepository vacancyRepository = new VacancyRepository();

    public List<Vacancy> getManagedVacancies(UserAccount organiser) {
        return vacancyRepository.findAll().stream()
                .filter(vacancy -> vacancy.getCreatedBy() != null
                        && (vacancy.getCreatedBy().equalsIgnoreCase(organiser.getUsername())
                        || vacancy.getCreatedBy().equalsIgnoreCase(organiser.getUserId())))
                .toList();
    }

    public Optional<Vacancy> getManagedVacancy(UserAccount organiser, String vacancyId) {
        return getManagedVacancies(organiser).stream()
                .filter(vacancy -> vacancy.getVacancyId().equals(vacancyId))
                .findFirst();
    }

    public List<ApplicationRecord> getApplicationsForVacancy(String vacancyId) {
        return applicationRepository.findByVacancyId(vacancyId);
    }

    public String updateDecision(UserAccount organiser, String vacancyId, String applicationId, String decision,
                                 String reviewNote, String optionalFeedback) {
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

        if (getManagedVacancy(organiser, vacancyId).isEmpty()) {
            return "You cannot review a vacancy that is not managed by your account.";
        }

        if (!ValidationUtil.STATUS_OFFERED.equals(decision)
                && !ValidationUtil.STATUS_UNSUCCESSFUL.equals(decision)) {
            return "Decision must be either Offered or Unsuccessful.";
        }

        if (ValidationUtil.isBlank(reviewNote)) {
            return "Review note is required before saving a decision.";
        }

        List<ApplicationRecord> applications = new ArrayList<>(applicationRepository.findAll());
        boolean updated = false;

        for (ApplicationRecord application : applications) {
            if (applicationId.equals(application.getApplicationId())
                    && vacancyId.equals(application.getVacancyId())) {
                application.setStatus(decision);
                application.setReviewNote(reviewNote);
                application.setOptionalFeedback(optionalFeedback);
                updated = true;
                break;
            }
        }

        if (!updated) {
            return "The selected application record could not be found.";
        }

        applicationRepository.saveAll(applications);
        return null;
    }
}
