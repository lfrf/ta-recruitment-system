package com.group27.tarecruitment.service;

import com.group27.tarecruitment.model.ApplicantProfile;
import com.group27.tarecruitment.model.ApplicantWorkloadSummary;
import com.group27.tarecruitment.model.ApplicationRecord;
import com.group27.tarecruitment.model.BlacklistEntry;
import com.group27.tarecruitment.model.UserAccount;
import com.group27.tarecruitment.model.UserRole;
import com.group27.tarecruitment.model.Vacancy;
import com.group27.tarecruitment.support.TestDataSupport;
import com.group27.tarecruitment.util.ValidationUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * WorkloadServiceIntegrationTest class type.
 *
 * <p>Test type used to verify behavior, edge cases, and regression safety.</p>
 * <p>Package: {@code com.group27.tarecruitment.service}</p>
 */
class WorkloadServiceIntegrationTest {

    private final WorkloadService workloadService = new WorkloadService();

    /**
     * Updates existing state while preserving consistency constraints.
     */
    @BeforeEach
    void setUp() {
        TestDataSupport.resetRuntimeDataDir();
        TestDataSupport.seedUsers(List.of(
                user("u-app-1", "Alice Zhang", "alice@example.com"),
                user("u-app-2", "Bob Li", "bob@example.com")
        ));
        TestDataSupport.seedProfiles(List.of(
                profile("u-app-1", "S1001", "Alice Zhang", "alice@example.com", false),
                profile("u-app-2", "S1002", "Bob Li", "bob@example.com", false)
        ));
        TestDataSupport.seedVacancies(List.of(
                vacancy("vac-1", "EBU4211", "Programming"),
                vacancy("vac-2", "EBU6304", "Software Engineering")
        ));
        TestDataSupport.seedApplications(List.of(
                application("a-1", "vac-1", "u-app-1", ValidationUtil.STATUS_SUBMITTED),
                application("a-2", "vac-2", "u-app-1", ValidationUtil.STATUS_OFFERED),
                application("a-3", "vac-2", "u-app-2", ValidationUtil.STATUS_UNSUCCESSFUL)
        ));
        TestDataSupport.seedBlacklist(List.of(activeBlacklist("bl-1", "u-app-2")));
    }

    /**
     * Executes business behavior as part of the class contract.
     */
    @Test
    void filterSummariesShouldReturnOnlyFlaggedWhenFlaggedOnlyIsTrue() {
        List<ApplicantWorkloadSummary> flagged = workloadService.filterSummaries(
                1, "", "", true);

        assertEquals(2, flagged.size());
        assertTrue(flagged.stream().anyMatch(item -> "u-app-1".equals(item.getApplicantId()) && item.isOverloaded()));
        assertTrue(flagged.stream().anyMatch(item -> "u-app-2".equals(item.getApplicantId()) && item.isBlacklisted()));
    }

    /**
     * Executes business behavior as part of the class contract.
     */
    @Test
    void filterSummariesShouldSupportApplicantAndModuleKeywordFiltering() {
        List<ApplicantWorkloadSummary> byApplicant = workloadService.filterSummaries(
                3, "alice", "", false);
        assertEquals(1, byApplicant.size());
        assertEquals("u-app-1", byApplicant.get(0).getApplicantId());

        List<ApplicantWorkloadSummary> byModule = workloadService.filterSummaries(
                3, "", "6304", false);
        assertEquals(1, byModule.size());
        assertEquals("u-app-1", byModule.get(0).getApplicantId());
    }

    /**
     * Executes business behavior as part of the class contract.
     * @param userId input parameter of type {@code String}.
     * @param displayName input parameter of type {@code String}.
     * @param email input parameter of type {@code String}.
     * @return the computed `UserAccount` value for this operation.
     */
    private UserAccount user(String userId, String displayName, String email) {
        UserAccount user = new UserAccount();
        user.setUserId(userId);
        user.setUsername(userId);
        user.setPassword("pass1234");
        user.setRole(UserRole.APPLICANT);
        user.setDisplayName(displayName);
        user.setEmail(email);
        user.setActive(true);
        return user;
    }

    /**
     * Executes business behavior as part of the class contract.
     * @param userId input parameter of type {@code String}.
     * @param studentId input parameter of type {@code String}.
     * @param fullName input parameter of type {@code String}.
     * @param email input parameter of type {@code String}.
     * @param blacklisted input parameter of type {@code boolean}.
     * @return the computed `ApplicantProfile` value for this operation.
     */
    private ApplicantProfile profile(String userId, String studentId, String fullName, String email, boolean blacklisted) {
        ApplicantProfile profile = new ApplicantProfile();
        profile.setApplicantId(userId);
        profile.setStudentId(studentId);
        profile.setFullName(fullName);
        profile.setEmail(email);
        profile.setBlacklisted(blacklisted);
        return profile;
    }

    /**
     * Executes business behavior as part of the class contract.
     * @param vacancyId input parameter of type {@code String}.
     * @param moduleCode input parameter of type {@code String}.
     * @param moduleName input parameter of type {@code String}.
     * @return the computed `Vacancy` value for this operation.
     */
    private Vacancy vacancy(String vacancyId, String moduleCode, String moduleName) {
        Vacancy vacancy = new Vacancy();
        vacancy.setVacancyId(vacancyId);
        vacancy.setModuleCode(moduleCode);
        vacancy.setModuleName(moduleName);
        vacancy.setStatus("OPEN");
        return vacancy;
    }

    /**
     * Executes business behavior as part of the class contract.
     * @param applicationId input parameter of type {@code String}.
     * @param vacancyId input parameter of type {@code String}.
     * @param applicantId input parameter of type {@code String}.
     * @param status input parameter of type {@code String}.
     * @return the computed `ApplicationRecord` value for this operation.
     */
    private ApplicationRecord application(String applicationId, String vacancyId, String applicantId, String status) {
        ApplicationRecord record = new ApplicationRecord();
        record.setApplicationId(applicationId);
        record.setVacancyId(vacancyId);
        record.setApplicantId(applicantId);
        record.setStatus(status);
        record.setSubmittedAt("2026-05-22T10:00:00");
        record.setDecisionRead(Boolean.TRUE);
        record.setDecisionUpdatedAt("");
        return record;
    }

    /**
     * Executes business behavior as part of the class contract.
     * @param entryId input parameter of type {@code String}.
     * @param applicantId input parameter of type {@code String}.
     * @return the computed `BlacklistEntry` value for this operation.
     */
    private BlacklistEntry activeBlacklist(String entryId, String applicantId) {
        BlacklistEntry entry = new BlacklistEntry();
        entry.setEntryId(entryId);
        entry.setApplicantId(applicantId);
        entry.setReason("policy");
        entry.setCreatedAt("2026-05-22T12:00:00");
        entry.setCreatedBy("admin01");
        entry.setActive(true);
        return entry;
    }
}
