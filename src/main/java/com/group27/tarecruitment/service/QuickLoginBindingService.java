package com.group27.tarecruitment.service;

import com.group27.tarecruitment.model.QuickLoginBinding;
import com.group27.tarecruitment.model.UserAccount;
import com.group27.tarecruitment.repository.QuickLoginBindingRepository;
import com.group27.tarecruitment.util.ValidationUtil;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.UUID;

public class QuickLoginBindingService {
    public static final String QUICK_LOGIN_DEVICE_COOKIE = "ta_quick_login_device";
    private static final DateTimeFormatter BOUND_AT_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    private final QuickLoginBindingRepository quickLoginBindingRepository = new QuickLoginBindingRepository();

    public Optional<QuickLoginBinding> getActiveBinding(String userId) {
        if (ValidationUtil.isBlank(userId)) {
            return Optional.empty();
        }
        return quickLoginBindingRepository.findActiveByUserId(userId);
    }

    public QuickLoginBinding bindCurrentDevice(UserAccount userAccount, String userAgent) {
        return bindDevice(userAccount.getUserId(), userAgent);
    }

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

    public Optional<QuickLoginBinding> getActiveBindingByToken(String bindToken) {
        if (ValidationUtil.isBlank(bindToken)) {
            return Optional.empty();
        }
        return quickLoginBindingRepository.findActiveByBindToken(bindToken);
    }

    public void unbind(String userId) {
        quickLoginBindingRepository.deactivateByUserId(userId);
    }

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
