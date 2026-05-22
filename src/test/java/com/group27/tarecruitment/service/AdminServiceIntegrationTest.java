package com.group27.tarecruitment.service;

import com.group27.tarecruitment.model.AdminConfig;
import com.group27.tarecruitment.model.ApplicantBlacklistSummary;
import com.group27.tarecruitment.model.ApplicantProfile;
import com.group27.tarecruitment.model.BlacklistEntry;
import com.group27.tarecruitment.model.UserAccount;
import com.group27.tarecruitment.model.UserRole;
import com.group27.tarecruitment.repository.ApplicantProfileRepository;
import com.group27.tarecruitment.repository.BlacklistRepository;
import com.group27.tarecruitment.support.TestDataSupport;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * AdminServiceIntegrationTest class type.
 *
 * <p>Test type used to verify behavior, edge cases, and regression safety.</p>
 * <p>Package: {@code com.group27.tarecruitment.service}</p>
 */
class AdminServiceIntegrationTest {

    private final AdminService adminService = new AdminService();
    private final ApplicantProfileRepository applicantProfileRepository = new ApplicantProfileRepository();
    private final BlacklistRepository blacklistRepository = new BlacklistRepository();

    /**
     * Updates existing state while preserving consistency constraints.
     */
    @BeforeEach
    void setUp() {
        TestDataSupport.resetRuntimeDataDir();
        TestDataSupport.seedAdminConfig(3, true);
        TestDataSupport.seedProfiles(List.of(profile("u-app-1", "Alice"), profile("u-app-2", "Bob")));
        TestDataSupport.seedUsers(List.of(
                user("admin01", UserRole.ADMIN, true),
                user("u-app-1", UserRole.APPLICANT, true),
                user("u-app-2", UserRole.APPLICANT, true)
        ));
        TestDataSupport.seedBlacklist(List.of());
    }

    /**
     * Updates existing state while preserving consistency constraints.
     */
    @Test
    void updateConfigShouldValidateRangeAndPersist() {
        String notPositive = adminService.updateConfig("0", true);
        assertEquals("Max workload must be a valid positive integer.", notPositive);

        String outOfRange = adminService.updateConfig("11", true);
        assertEquals("Max workload should be between 1 and 10 roles.", outOfRange);

        String ok = adminService.updateConfig("4", false);
        assertNull(ok);

        AdminConfig config = adminService.getConfig();
        assertEquals(4, config.getMaxWorkload());
        assertFalse(config.isAllowVisitorBrowsing());
    }

    /**
     * Creates and initializes new business data for downstream use.
     */
    @Test
    void addBlacklistEntryShouldRequireConfirmationAndSetProfileFlag() {
        String noConfirm = adminService.addBlacklistEntry(admin("admin01"), "u-app-1", "Policy issue", false);
        assertEquals("Please confirm the selected applicant before adding a blacklist entry.", noConfirm);

        String added = adminService.addBlacklistEntry(admin("admin01"), "u-app-1", "Policy issue", true);
        assertNull(added);

        List<BlacklistEntry> entries = blacklistRepository.findAll();
        assertEquals(1, entries.size());
        assertTrue(entries.get(0).isActive());
        assertEquals("u-app-1", entries.get(0).getApplicantId());

        ApplicantProfile profile = applicantProfileRepository.findByApplicantId("u-app-1").orElse(null);
        assertNotNull(profile);
        assertTrue(profile.isBlacklisted());
    }

    /**
     * Executes business behavior as part of the class contract.
     */
    @Test
    void deactivateBlacklistEntryShouldClearApplicantFlagWhenNoOtherActiveEntries() {
        String added = adminService.addBlacklistEntry(admin("admin01"), "u-app-2", "Test reason", true);
        assertNull(added);
        BlacklistEntry entry = blacklistRepository.findAll().get(0);

        String deactivateError = adminService.deactivateBlacklistEntry(entry.getEntryId());
        assertNull(deactivateError);

        BlacklistEntry updatedEntry = blacklistRepository.findAll().get(0);
        assertFalse(updatedEntry.isActive());

        ApplicantProfile profile = applicantProfileRepository.findByApplicantId("u-app-2").orElse(null);
        assertNotNull(profile);
        assertFalse(profile.isBlacklisted());
    }

    /**
     * Retrieves data using the provided criteria and current business rules.
     */
    @Test
    void getBlacklistSummariesShouldReturnLatestFirst() {
        adminService.addBlacklistEntry(admin("admin01"), "u-app-1", "Reason A", true);
        adminService.addBlacklistEntry(admin("admin01"), "u-app-2", "Reason B", true);

        List<ApplicantBlacklistSummary> summaries = adminService.getBlacklistSummaries();
        assertEquals(2, summaries.size());
        assertEquals("u-app-2", summaries.get(0).getApplicantId());
        assertTrue(summaries.stream().allMatch(ApplicantBlacklistSummary::isActive));
    }

    /**
     * Executes business behavior as part of the class contract.
     * @param id input parameter of type {@code String}.
     * @param role input parameter of type {@code UserRole}.
     * @param active input parameter of type {@code boolean}.
     * @return the computed `UserAccount` value for this operation.
     */
    private UserAccount user(String id, UserRole role, boolean active) {
        UserAccount account = new UserAccount();
        account.setUserId(id);
        account.setUsername(id);
        account.setPassword("pass1234");
        account.setRole(role);
        account.setDisplayName(id);
        account.setEmail(id + "@example.com");
        account.setActive(active);
        return account;
    }

    /**
     * Executes business behavior as part of the class contract.
     * @param username input parameter of type {@code String}.
     * @return the computed `UserAccount` value for this operation.
     */
    private UserAccount admin(String username) {
        UserAccount admin = new UserAccount();
        admin.setUserId(username);
        admin.setUsername(username);
        admin.setRole(UserRole.ADMIN);
        admin.setDisplayName(username);
        admin.setActive(true);
        return admin;
    }

    /**
     * Executes business behavior as part of the class contract.
     * @param userId input parameter of type {@code String}.
     * @param fullName input parameter of type {@code String}.
     * @return the computed `ApplicantProfile` value for this operation.
     */
    private ApplicantProfile profile(String userId, String fullName) {
        ApplicantProfile profile = new ApplicantProfile();
        profile.setApplicantId(userId);
        profile.setStudentId("S-" + userId);
        profile.setFullName(fullName);
        profile.setEmail(userId + "@example.com");
        profile.setBlacklisted(false);
        return profile;
    }
}
