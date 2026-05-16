package com.group27.tarecruitment.service;

import com.group27.tarecruitment.model.ApplicationRecord;
import com.group27.tarecruitment.model.UserAccount;
import com.group27.tarecruitment.model.Vacancy;
import com.group27.tarecruitment.repository.ApplicationRepository;
import com.group27.tarecruitment.repository.VacancyRepository;
import com.group27.tarecruitment.util.ValidationUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ReviewService {
    private final ApplicationRepository applicationRepository = new ApplicationRepository();
    private final VacancyRepository vacancyRepository = new VacancyRepository();

    public List<Vacancy> getManagedVacancies(UserAccount organiser) {
        return vacancyRepository.findAll().stream()
                .filter(vacancy -> vacancy.getCreatedBy() != null
                        && (vacancy.getCreatedBy().equalsIgnoreCase(organiser.getUsername())
                        || vacancy.getCreatedBy().equalsIgnoreCase(organiser.getUserId())))
                .filter(vacancy -> isManageableStatus(vacancy.getStatus()))
                .toList();
    }

    public Optional<Vacancy> getManagedVacancy(UserAccount organiser, String vacancyId) {
        return getManagedVacancies(organiser).stream()
                .filter(vacancy -> vacancy.getVacancyId().equals(vacancyId))
                .findFirst();
    }

    public List<ApplicationRecord> getApplicationsForVacancy(String vacancyId) {
        return applicationRepository.findByVacancyId(vacancyId).stream()
                .filter(application -> !ValidationUtil.STATUS_WITHDRAWN.equalsIgnoreCase(
                        ValidationUtil.normalizeApplicationStatus(application.getStatus())))
                .toList();
    }

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

        if (ValidationUtil.STATUS_UNSUCCESSFUL.equals(decision)) {
            target.setLeadTa(false);
        }

        applicationRepository.saveAll(applications);
        syncVacancyOpenStatus(vacancy, applications);
        return null;
    }

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

    private boolean isManageableStatus(String status) {
        String normalized = ValidationUtil.trimToEmpty(status);
        return "OPEN".equalsIgnoreCase(normalized) || "CLOSED".equalsIgnoreCase(normalized);
    }
}
