package com.group27.tarecruitment.service;

import com.group27.tarecruitment.model.UserAccount;
import com.group27.tarecruitment.repository.UserRepository;
import com.group27.tarecruitment.util.PasswordUtil;

import java.util.Optional;

/**
 * AuthService class type.
 *
 * <p>Service type that centralizes business rules and multi-step domain workflows.</p>
 * <p>Package: {@code com.group27.tarecruitment.service}</p>
 */
public class AuthService {
    private final UserRepository userRepository = new UserRepository();

    /**
     * Performs authentication or security-related validation logic.
     * @param username input parameter of type {@code String}.
     * @param password input parameter of type {@code String}.
     * @return an optional result that is present when data is available.
     */
    public Optional<UserAccount> authenticate(String username, String password) {
        return userRepository.findByUsername(username)
                .filter(UserAccount::isActive)
                .filter(user -> PasswordUtil.matches(password, user.getPassword()));
    }
}
