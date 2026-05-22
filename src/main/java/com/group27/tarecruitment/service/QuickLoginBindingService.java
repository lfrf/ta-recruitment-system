package com.group27.tarecruitment.service;

import com.group27.tarecruitment.model.QuickLoginBinding;
import com.group27.tarecruitment.model.UserAccount;
import com.group27.tarecruitment.repository.QuickLoginBindingRepository;
import com.group27.tarecruitment.util.ValidationUtil;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.UUID;

/**
 * QuickLoginBindingService class type.
 *
 * <p>Service type that centralizes business rules and multi-step domain workflows.</p>
 * <p>Package: {@code com.group27.tarecruitment.service}</p>
 */
public class QuickLoginBindingService {
    public static final String QUICK_LOGIN_DEVICE_COOKIE = "ta_quick_login_device";
    private static final DateTimeFormatter BOUND_AT_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    private final QuickLoginBindingRepository quickLoginBindingRepository = new QuickLoginBindingRepository();

    /**
     * Retrieves data using the provided criteria and current business rules.
     * @param userId input parameter of type {@code String}.
     * @return an optional result that is present when data is available.
     */
    public Optional<QuickLoginBinding> getActiveBinding(String userId) {
        if (ValidationUtil.isBlank(userId)) {
            return Optional.empty();
        }
        return quickLoginBindingRepository.findActiveByUserId(userId);
    }

    /**
     * Executes business behavior as part of the class contract.
     * @param userAccount input parameter of type {@code UserAccount}.
     * @param userAgent input parameter of type {@code String}.
     * @return the computed `QuickLoginBinding` value for this operation.
     */
    public QuickLoginBinding bindCurrentDevice(UserAccount userAccount, String userAgent) {
        return bindDevice(userAccount.getUserId(), userAgent);
    }

    /**
     * Executes business behavior as part of the class contract.
     * @param userId input parameter of type {@code String}.
     * @param userAgent input parameter of type {@code String}.
     * @return the computed `QuickLoginBinding` value for this operation.
     */
    public QuickLoginBinding bindDevice(String userId, String userAgent) {
        QuickLoginBinding binding = new QuickLoginBinding();
        binding.setUserId(userId);
        binding.setBindToken(UUID.randomUUID().toString().replace("-", ""));
        binding.setDeviceName(sanitizeDeviceName(userAgent));
        binding.setBoundAt(BOUND_AT_FORMATTER.format(LocalDateTime.now()));
        binding.setActive(true);
        quickLoginBindingRepository.saveOrReplace(binding);
        return binding;
    }

    /**
     * Retrieves data using the provided criteria and current business rules.
     * @param bindToken input parameter of type {@code String}.
     * @return an optional result that is present when data is available.
     */
    public Optional<QuickLoginBinding> getActiveBindingByToken(String bindToken) {
        if (ValidationUtil.isBlank(bindToken)) {
            return Optional.empty();
        }
        return quickLoginBindingRepository.findActiveByBindToken(bindToken);
    }

    /**
     * Executes business behavior as part of the class contract.
     * @param userId input parameter of type {@code String}.
     */
    public void unbind(String userId) {
        quickLoginBindingRepository.deactivateByUserId(userId);
    }

    /**
     * Executes business behavior as part of the class contract.
     * @param userAgent input parameter of type {@code String}.
     * @return the computed `String` value for this operation.
     */
    private String sanitizeDeviceName(String userAgent) {
        if (ValidationUtil.isBlank(userAgent)) {
            return "Current device";
        }
        String compact = userAgent.replaceAll("\\s+", " ").trim();
        if (compact.length() > 80) {
            compact = compact.substring(0, 80);
        }
        return compact;
    }
}
