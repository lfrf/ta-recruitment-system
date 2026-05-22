package com.group27.tarecruitment.service;

import com.group27.tarecruitment.model.UserAccount;
import com.group27.tarecruitment.model.UserRole;
import com.group27.tarecruitment.repository.UserRepository;
import com.group27.tarecruitment.service.AccountSecurityService.ChangePasswordResult;
import com.group27.tarecruitment.support.TestDataSupport;
import com.group27.tarecruitment.util.PasswordUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * AccountSecurityAndAuthServiceIntegrationTest class type.
 *
 * <p>Test type used to verify behavior, edge cases, and regression safety.</p>
 * <p>Package: {@code com.group27.tarecruitment.service}</p>
 */
class AccountSecurityAndAuthServiceIntegrationTest {

    private final AccountSecurityService accountSecurityService = new AccountSecurityService();
    private final AuthService authService = new AuthService();
    private final UserRepository userRepository = new UserRepository();

    /**
     * Updates existing state while preserving consistency constraints.
     */
    @BeforeEach
    void setUp() {
        TestDataSupport.resetRuntimeDataDir();
        TestDataSupport.seedUsers(java.util.List.of(
                user("u-plain", "plainUser", "pass1234", true),
                user("u-hashed", "hashedUser", PasswordUtil.hashWithPrefix("securePass!"), true),
                user("u-inactive", "inactiveUser", PasswordUtil.hashWithPrefix("inactivePass!"), false)
        ));
    }

    /**
     * Updates existing state while preserving consistency constraints.
     */
    @Test
    void changePasswordShouldMigratePlainPasswordToHashedValue() {
        ChangePasswordResult result = accountSecurityService.changePassword("u-plain", "pass1234", "newPass123!");
        assertTrue(result.isOk());
        assertEquals("OK", result.getCode());

        UserAccount updated = userRepository.findByUserId("u-plain").orElse(null);
        assertNotNull(updated);
        assertTrue(updated.getPassword().startsWith("sha256$"));
        assertTrue(PasswordUtil.matches("newPass123!", updated.getPassword()));
    }

    /**
     * Updates existing state while preserving consistency constraints.
     */
    @Test
    void changePasswordShouldRejectWrongCurrentPassword() {
        ChangePasswordResult result = accountSecurityService.changePassword("u-hashed", "wrongPass", "newPass123!");
        assertFalse(result.isOk());
        assertEquals("CURRENT_PASSWORD_INCORRECT", result.getCode());
    }

    /**
     * Updates existing state while preserving consistency constraints.
     */
    @Test
    void changePasswordShouldRejectShortPassword() {
        ChangePasswordResult result = accountSecurityService.changePassword("u-hashed", "securePass!", "123");
        assertFalse(result.isOk());
        assertEquals("INVALID_LENGTH", result.getCode());
    }

    /**
     * Performs authentication or security-related validation logic.
     */
    @Test
    void authenticateShouldSupportHashedAndPlainPasswords() {
        assertTrue(authService.authenticate("plainUser", "pass1234").isPresent());
        assertTrue(authService.authenticate("hashedUser", "securePass!").isPresent());
    }

    /**
     * Performs authentication or security-related validation logic.
     */
    @Test
    void authenticateShouldRejectInactiveUser() {
        assertTrue(authService.authenticate("inactiveUser", "inactivePass!").isEmpty());
    }

    /**
     * Executes business behavior as part of the class contract.
     * @param userId input parameter of type {@code String}.
     * @param username input parameter of type {@code String}.
     * @param password input parameter of type {@code String}.
     * @param active input parameter of type {@code boolean}.
     * @return the computed `UserAccount` value for this operation.
     */
    private UserAccount user(String userId, String username, String password, boolean active) {
        UserAccount user = new UserAccount();
        user.setUserId(userId);
        user.setUsername(username);
        user.setPassword(password);
        user.setRole(UserRole.APPLICANT);
        user.setDisplayName(username);
        user.setEmail(username + "@example.com");
        user.setActive(active);
        return user;
    }
}
