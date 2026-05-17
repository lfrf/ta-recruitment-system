package com.group27.tarecruitment.service;

import com.group27.tarecruitment.model.UserAccount;
import com.group27.tarecruitment.repository.UserRepository;
import com.group27.tarecruitment.util.PasswordUtil;
import com.group27.tarecruitment.util.ValidationUtil;

import java.util.Optional;

public class AccountSecurityService {
    private static final int MIN_PASSWORD_LENGTH = 8;
    private static final int MAX_PASSWORD_LENGTH = 64;

    private final UserRepository userRepository = new UserRepository();

    public ChangePasswordResult changePassword(String userId, String currentPassword, String newPassword) {
        if (ValidationUtil.isBlank(userId)) {
            return ChangePasswordResult.error("AUTH_REQUIRED", "Please log in first.");
        }
        if (ValidationUtil.isBlank(currentPassword) || ValidationUtil.isBlank(newPassword)) {
            return ChangePasswordResult.error("INVALID_INPUT", "Current password and new password are required.");
        }
        if (newPassword.length() < MIN_PASSWORD_LENGTH || newPassword.length() > MAX_PASSWORD_LENGTH) {
            return ChangePasswordResult.error("INVALID_LENGTH",
                    "New password must be between " + MIN_PASSWORD_LENGTH + " and " + MAX_PASSWORD_LENGTH + " characters.");
        }

        Optional<UserAccount> optionalUser = userRepository.findByUserId(userId);
        if (optionalUser.isEmpty()) {
            return ChangePasswordResult.error("USER_NOT_FOUND", "Account was not found.");
        }

        UserAccount user = optionalUser.get();
        if (!PasswordUtil.matches(currentPassword, user.getPassword())) {
            return ChangePasswordResult.error("CURRENT_PASSWORD_INCORRECT", "Current password is incorrect.");
        }
        if (PasswordUtil.matches(newPassword, user.getPassword())) {
            return ChangePasswordResult.error("PASSWORD_UNCHANGED", "New password must be different from current password.");
        }

        user.setPassword(PasswordUtil.hashWithPrefix(newPassword));
        userRepository.save(user);
        return ChangePasswordResult.ok(user);
    }

    public static class ChangePasswordResult {
        private final boolean ok;
        private final String code;
        private final String message;
        private final UserAccount user;

        private ChangePasswordResult(boolean ok, String code, String message, UserAccount user) {
            this.ok = ok;
            this.code = code;
            this.message = message;
            this.user = user;
        }

        public static ChangePasswordResult ok(UserAccount user) {
            return new ChangePasswordResult(true, "OK", "Password updated.", user);
        }

        public static ChangePasswordResult error(String code, String message) {
            return new ChangePasswordResult(false, code, message, null);
        }

        public boolean isOk() {
            return ok;
        }

        public String getCode() {
            return code;
        }

        public String getMessage() {
            return message;
        }

        public UserAccount getUser() {
            return user;
        }
    }
}
