package com.group27.tarecruitment.repository;

import com.group27.tarecruitment.model.AdminConfig;
import com.group27.tarecruitment.util.JsonFileUtil;

public class AdminConfigRepository {
    private static final String CONFIG_RESOURCE = "data/admin_config.json";

    public AdminConfig load() {
        return JsonFileUtil.readObject(CONFIG_RESOURCE, AdminConfig.class);
    }

    public void save(AdminConfig adminConfig) {
        JsonFileUtil.writeObject(CONFIG_RESOURCE, adminConfig);
    }
}
