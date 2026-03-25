package com.group27.tarecruitment.repository;

import com.group27.tarecruitment.model.BlacklistEntry;
import com.group27.tarecruitment.util.JsonFileUtil;
import java.util.List;

public class BlacklistRepository {
    private static final String BLACKLIST_RESOURCE = "data/blacklist.json";

    public List<BlacklistEntry> findAll() {
        return JsonFileUtil.readList(BLACKLIST_RESOURCE, BlacklistEntry.class);
    }

    public List<BlacklistEntry> findActiveEntries() {
        return findAll().stream()
                .filter(BlacklistEntry::isActive)
                .toList();
    }

    public void saveAll(List<BlacklistEntry> entries) {
        JsonFileUtil.writeList(BLACKLIST_RESOURCE, entries);
    }
}
