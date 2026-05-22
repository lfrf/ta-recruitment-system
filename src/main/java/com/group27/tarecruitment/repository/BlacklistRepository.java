package com.group27.tarecruitment.repository;

import com.group27.tarecruitment.model.BlacklistEntry;
import com.group27.tarecruitment.util.JsonFileUtil;
import java.util.List;

/**
 * BlacklistRepository class type.
 *
 * <p>Repository type that encapsulates persistence and query behavior.</p>
 * <p>Package: {@code com.group27.tarecruitment.repository}</p>
 */
public class BlacklistRepository {
    private static final String BLACKLIST_RESOURCE = "data/blacklist.json";

    /**
     * Retrieves data using the provided criteria and current business rules.
     * @return a collection containing the computed result elements.
     */
    public List<BlacklistEntry> findAll() {
        return JsonFileUtil.readList(BLACKLIST_RESOURCE, BlacklistEntry.class);
    }

    /**
     * Retrieves data using the provided criteria and current business rules.
     * @return a collection containing the computed result elements.
     */
    public List<BlacklistEntry> findActiveEntries() {
        return findAll().stream()
                .filter(BlacklistEntry::isActive)
                .toList();
    }

    /**
     * Executes business behavior as part of the class contract.
     * @param entries input parameter of type {@code List<BlacklistEntry>}.
     */
    public void saveAll(List<BlacklistEntry> entries) {
        JsonFileUtil.writeList(BLACKLIST_RESOURCE, entries);
    }
}
