package com.group27.tarecruitment.repository;

import com.group27.tarecruitment.model.UserAccount;
import com.group27.tarecruitment.util.JsonFileUtil;
import java.util.List;
import java.util.Optional;

public class UserRepository {
    private static final String USERS_RESOURCE = "data/users.json";

    public List<UserAccount> findAll() {
        return JsonFileUtil.readList(USERS_RESOURCE, UserAccount.class);
    }

    public Optional<UserAccount> findByUsername(String username) {
        return findAll().stream()
                .filter(user -> user.getUsername() != null && user.getUsername().equalsIgnoreCase(username))
                .findFirst();
    }
}
