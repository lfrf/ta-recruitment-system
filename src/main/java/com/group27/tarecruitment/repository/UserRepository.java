package com.group27.tarecruitment.repository;

import com.group27.tarecruitment.model.UserAccount;
import com.group27.tarecruitment.util.JsonFileUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * UserRepository class type.
 *
 * <p>Repository type that encapsulates persistence and query behavior.</p>
 * <p>Package: {@code com.group27.tarecruitment.repository}</p>
 */
public class UserRepository {
    private static final String USERS_RESOURCE = "data/users.json";

    /**
     * Retrieves data using the provided criteria and current business rules.
     * @return a collection containing the computed result elements.
     */
    public List<UserAccount> findAll() {
        return JsonFileUtil.readList(USERS_RESOURCE, UserAccount.class);
    }

    /**
     * Retrieves data using the provided criteria and current business rules.
     * @param username input parameter of type {@code String}.
     * @return an optional result that is present when data is available.
     */
    public Optional<UserAccount> findByUsername(String username) {
        return findAll().stream()
                .filter(user -> user.getUsername() != null && user.getUsername().equalsIgnoreCase(username))
                .findFirst();
    }

    /**
     * Retrieves data using the provided criteria and current business rules.
     * @param userId input parameter of type {@code String}.
     * @return an optional result that is present when data is available.
     */
    public Optional<UserAccount> findByUserId(String userId) {
        return findAll().stream()
                .filter(user -> user.getUserId() != null && user.getUserId().equals(userId))
                .findFirst();
    }

    /**
     * Executes business behavior as part of the class contract.
     * @param user input parameter of type {@code UserAccount}.
     */
    public void save(UserAccount user) {
        List<UserAccount> all = new ArrayList<>(findAll());
        all.removeIf(item -> item.getUserId() != null && item.getUserId().equals(user.getUserId()));
        all.add(user);
        JsonFileUtil.writeList(USERS_RESOURCE, all);
    }

    /**
     * Executes business behavior as part of the class contract.
     * @param users input parameter of type {@code List<UserAccount>}.
     */
    public void saveAll(List<UserAccount> users) {
        JsonFileUtil.writeList(USERS_RESOURCE, users);
    }
}
