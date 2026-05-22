package com.group27.tarecruitment.service;

import com.group27.tarecruitment.model.ApplicationRecord;
import com.group27.tarecruitment.model.BlacklistEntry;
import com.group27.tarecruitment.model.UserAccount;
import com.group27.tarecruitment.model.UserRole;
import com.group27.tarecruitment.model.Vacancy;
import com.group27.tarecruitment.repository.ApplicationRepository;
import com.group27.tarecruitment.support.TestDataSupport;
import com.group27.tarecruitment.util.ValidationUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * ApplicationServiceIntegrationTest class type.
 *
 * <p>Test type used to verify behavior, edge cases, and regression safety.</p>
 * <p>Package: {@code com.group27.tarecruitment.service}</p>
 */
class ApplicationServiceIntegrationTest {

    private final ApplicationService applicationService = new ApplicationService();
    private final ApplicationRepository applicationRepository = new ApplicationRepository();

    /**
     * Updates existing state while preserving consistency constraints.
     */
    @BeforeEach
    void setUp() {
        TestDataSupport.resetRuntimeDataDir();
        TestDataSupport.seedAdminConfig(2, true);
        TestDataSupport.seedUsers(List.of(
                user("u-app-1", "applicant01", "pass123", UserRole.APPLICANT, true)
        ));
        TestDataSupport.seedVacancies(List.of(
                vacancy("vac-open", "OPEN", 2),
                vacancy("vac-full", "OPEN", 1)
        ));
        TestDataSupport.seedApplications(List.of(
                application("app-1", "vac-full", "u-app-2", ValidationUtil.STATUS_OFFERED, "2026-05-22T10:00:00", true, "2026-05-22T12:00:00"),
                application("app-2", "vac-open", "u-app-1", ValidationUtil.STATUS_SUBMITTED, "2026-05-22T10:10:00", true, "")
        ));
        TestDataSupport.seedBlacklist(List.of());
    }

    /**
     * Applies review or decision outcomes and related status changes.
     */
    @Test
    void validateApplicationShouldRejectDuplicateNonWithdrawn() {
        String error = applicationService.validateApplication(user("u-app-1", "applicant01", "pass123", UserRole.APPLICANT, true), "vac-open");
        assertEquals("You have already applied for this vacancy.", error);
    }

    /**
     * Applies review or decision outcomes and related status changes.
     */
    @Test
    void validateApplicationShouldRejectBlacklistedApplicant() {
        BlacklistEntry entry = new BlacklistEntry();
        entry.setEntryId("bl-1");
        entry.setApplicantId("u-app-1");
        entry.setReason("policy");
        entry.setActive(true);
        TestDataSupport.seedBlacklist(List.of(entry));

        String error = applicationService.validateApplication(user("u-app-1", "applicant01", "pass123", UserRole.APPLICANT, true), "vac-full");
        assertEquals("This applicant account is currently blocked from applying.", error);
    }

    /**
     * Applies review or decision outcomes and related status changes.
     */
    @Test
    void validateApplicationShouldRejectFullVacancy() {
        String error = applicationService.validateApplication(user("u-app-3", "applicant03", "pass123", UserRole.APPLICANT, true), "vac-full");
        assertEquals("This course job is currently full. No TA places are left.", error);
    }

    /**
     * Applies review or decision outcomes and related status changes.
     */
    @Test
    void validateApplicationShouldRejectWhenExceedingWorkloadLimit() {
        TestDataSupport.seedApplications(List.of(
                application("app-2", "vac-open", "u-app-1", ValidationUtil.STATUS_SUBMITTED, "2026-05-22T10:10:00", true, ""),
                application("app-3", "vac-full", "u-app-1", ValidationUtil.STATUS_OFFERED, "2026-05-22T10:20:00", true, "2026-05-22T13:00:00")
        ));

        String error = applicationService.validateApplication(user("u-app-1", "applicant01", "pass123", UserRole.APPLICANT, true), "vac-new");
        assertEquals("This vacancy is not currently open for application.", error);

        TestDataSupport.seedVacancies(List.of(
                vacancy("vac-open", "OPEN", 2),
                vacancy("vac-full", "OPEN", 1),
                vacancy("vac-new", "OPEN", 2)
        ));
        String workloadError = applicationService.validateApplication(
                user("u-app-1", "applicant01", "pass123", UserRole.APPLICANT, true), "vac-new");
        assertEquals("You have reached the current application limit of 2 roles.", workloadError);
    }

    /**
     * Applies review or decision outcomes and related status changes.
     */
    @Test
    void markAllDecisionsAsReadShouldUpdateOnlyUnreadDecisionItems() {
        TestDataSupport.seedApplications(List.of(
                application("app-offer-unread", "vac-open", "u-app-1", ValidationUtil.STATUS_OFFERED,
                        "2026-05-22T10:10:00", false, "2026-05-22T12:30:00"),
                application("app-unsuccessful-unread", "vac-open", "u-app-1", ValidationUtil.STATUS_UNSUCCESSFUL,
                        "2026-05-22T10:20:00", false, "2026-05-22T12:40:00"),
                application("app-submitted", "vac-open", "u-app-1", ValidationUtil.STATUS_SUBMITTED,
                        "2026-05-22T10:30:00", false, "")
        ));

        int updated = applicationService.markAllDecisionsAsRead("u-app-1");
        assertEquals(2, updated);

        List<ApplicationRecord> records = applicationRepository.findByApplicantId("u-app-1");
        assertEquals(3, records.size());
        assertTrue(records.stream()
                .filter(item -> ValidationUtil.STATUS_OFFERED.equals(item.getStatus())
                        || ValidationUtil.STATUS_UNSUCCESSFUL.equals(item.getStatus()))
                .allMatch(item -> Boolean.TRUE.equals(item.getDecisionRead())));
    }

    /**
     * Removes, archives, or cancels previously created business state.
     */
    @Test
    void cancelApplicationShouldWithdrawSubmittedApplication() {
        String error = applicationService.cancelApplication(
                user("u-app-1", "applicant01", "pass123", UserRole.APPLICANT, true), "app-2");
        assertNull(error);

        ApplicationRecord updated = applicationRepository.findById("app-2").orElse(null);
        assertNotNull(updated);
        assertEquals(ValidationUtil.STATUS_WITHDRAWN, updated.getStatus());
        assertTrue(!updated.isLeadTa());
    }

    /**
     * Executes business behavior as part of the class contract.
     * @param userId input parameter of type {@code String}.
     * @param username input parameter of type {@code String}.
     * @param password input parameter of type {@code String}.
     * @param role input parameter of type {@code UserRole}.
     * @param active input parameter of type {@code boolean}.
     * @return the computed `UserAccount` value for this operation.
     */
    private UserAccount user(String userId, String username, String password, UserRole role, boolean active) {
        UserAccount user = new UserAccount();
        user.setUserId(userId);
        user.setUsername(username);
        user.setPassword(password);
        user.setRole(role);
        user.setDisplayName(username);
        user.setEmail(username + "@example.com");
        user.setActive(active);
        return user;
    }

    /**
     * Executes business behavior as part of the class contract.
     * @param vacancyId input parameter of type {@code String}.
     * @param status input parameter of type {@code String}.
     * @param positionCount input parameter of type {@code int}.
     * @return the computed `Vacancy` value for this operation.
     */
    private Vacancy vacancy(String vacancyId, String status, int positionCount) {
        Vacancy vacancy = new Vacancy();
        vacancy.setVacancyId(vacancyId);
        vacancy.setStatus(status);
        vacancy.setPositionCount(positionCount);
        vacancy.setModuleCode("EBU0000");
        vacancy.setModuleName("Module");
        vacancy.setCampus("Mile End");
        vacancy.setCreatedBy("mo01");
        vacancy.setLeaderRoleAvailable(true);
        return vacancy;
    }

    /**
     * Executes business behavior as part of the class contract.
     * @param applicationId input parameter of type {@code String}.
     * @param vacancyId input parameter of type {@code String}.
     * @param applicantId input parameter of type {@code String}.
     * @param status input parameter of type {@code String}.
     * @param submittedAt input parameter of type {@code String}.
     * @param decisionRead input parameter of type {@code boolean}.
     * @param decisionUpdatedAt input parameter of type {@code String}.
     * @return the computed `ApplicationRecord` value for this operation.
     */
    private ApplicationRecord application(String applicationId,
                                          String vacancyId,
                                          String applicantId,
                                          String status,
                                          String submittedAt,
                                          boolean decisionRead,
                                          String decisionUpdatedAt) {
        ApplicationRecord application = new ApplicationRecord();
        application.setApplicationId(applicationId);
        application.setVacancyId(vacancyId);
        application.setApplicantId(applicantId);
        application.setStatus(status);
        application.setSubmittedAt(submittedAt);
        application.setDecisionRead(decisionRead);
        application.setDecisionUpdatedAt(decisionUpdatedAt);
        application.setReviewNote("");
        application.setOptionalFeedback("");
        application.setLeadTa(false);
        return application;
    }
}
