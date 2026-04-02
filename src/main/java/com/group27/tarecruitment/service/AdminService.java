package com.group27.tarecruitment.service;

import com.group27.tarecruitment.model.AdminConfig;
import com.group27.tarecruitment.repository.AdminConfigRepository;

public class AdminService {
    private final AdminConfigRepository adminConfigRepository = new AdminConfigRepository();

    public AdminConfig getConfig() {
        return adminConfigRepository.load();
    }

    public String updateConfig(String maxWorkloadValue, boolean allowVisitorBrowsing) {
        int maxWorkload;
        try {
            maxWorkload = Integer.parseInt(maxWorkloadValue);
        } catch (NumberFormatException exception) {
            return "Max workload must be a valid integer.";
        }

        if (maxWorkload < 1 || maxWorkload > 10) {
            return "Max workload should be between 1 and 10 roles.";
        }

        AdminConfig config = adminConfigRepository.load();
        config.setMaxWorkload(maxWorkload);
        config.setAllowVisitorBrowsing(allowVisitorBrowsing);
        adminConfigRepository.save(config);
        return null;
    }
}
