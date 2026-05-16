package com.group27.tarecruitment.repository;

import com.group27.tarecruitment.model.QuickLoginBinding;
import com.group27.tarecruitment.util.JsonFileUtil;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class QuickLoginBindingRepository {
    private static final String QUICK_LOGIN_BINDINGS_RESOURCE = "data/quick_login_bindings.json";

    public List<QuickLoginBinding> findAll() {
        try {
            return JsonFileUtil.readList(QUICK_LOGIN_BINDINGS_RESOURCE, QuickLoginBinding.class);
        } catch (IllegalStateException exception) {
            recoverCorruptedFile(exception);
            return Collections.emptyList();
        }
    }

    public Optional<QuickLoginBinding> findActiveByUserId(String userId) {
        return findAll().stream()
                .filter(binding -> binding.isActive() && userId.equals(binding.getUserId()))
                .findFirst();
    }

    public Optional<QuickLoginBinding> findActiveByBindToken(String bindToken) {
        return findAll().stream()
                .filter(binding -> binding.isActive() && bindToken.equals(binding.getBindToken()))
                .findFirst();
    }

    public void saveOrReplace(QuickLoginBinding target) {
        List<QuickLoginBinding> all = new ArrayList<>(findAll());
        all.removeIf(binding -> target.getUserId().equals(binding.getUserId()));
        all.add(target);
        JsonFileUtil.writeList(QUICK_LOGIN_BINDINGS_RESOURCE, all);
    }

    public void deactivateByUserId(String userId) {
        List<QuickLoginBinding> all = new ArrayList<>(findAll());
        boolean changed = false;
        for (QuickLoginBinding binding : all) {
            if (userId.equals(binding.getUserId()) && binding.isActive()) {
                binding.setActive(false);
                changed = true;
            }
        }
        if (changed) {
            JsonFileUtil.writeList(QUICK_LOGIN_BINDINGS_RESOURCE, all);
        }
    }

    private void recoverCorruptedFile(IllegalStateException originalException) {
        Path runtimeFile = JsonFileUtil.getRuntimeDataDirectory().resolve("quick_login_bindings.json");
        try {
            if (Files.exists(runtimeFile)) {
                Path backupFile = runtimeFile.resolveSibling(
                        "quick_login_bindings.corrupted." + System.currentTimeMillis() + ".bak.json"
                );
                Files.copy(runtimeFile, backupFile, StandardCopyOption.REPLACE_EXISTING);
            }
            JsonFileUtil.writeList(QUICK_LOGIN_BINDINGS_RESOURCE, Collections.emptyList());
        } catch (Exception recoverException) {
            originalException.addSuppressed(recoverException);
            throw originalException;
        }
    }
}
