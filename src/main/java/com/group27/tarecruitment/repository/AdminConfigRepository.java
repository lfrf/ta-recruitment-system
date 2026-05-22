package com.group27.tarecruitment.repository;

import com.group27.tarecruitment.model.AdminConfig;
import com.group27.tarecruitment.util.JsonFileUtil;

/**
 * AdminConfigRepository class type.
 *
 * <p>Repository type that encapsulates persistence and query behavior.</p>
 * <p>Package: {@code com.group27.tarecruitment.repository}</p>
 */
public class AdminConfigRepository {
    private static final String CONFIG_RESOURCE = "data/admin_config.json";

    /**
     * Retrieves data using the provided criteria and current business rules.
     * @return the computed `AdminConfig` value for this operation.
     */
    public AdminConfig load() {
        return JsonFileUtil.readObject(CONFIG_RESOURCE, AdminConfig.class);
    }

    /**
     * Executes business behavior as part of the class contract.
     * @param adminConfig input parameter of type {@code AdminConfig}.
     */
    public void save(AdminConfig adminConfig) {
        JsonFileUtil.writeObject(CONFIG_RESOURCE, adminConfig);
    }
}
