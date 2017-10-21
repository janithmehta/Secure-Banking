package com.group06FALL2017.account.service;

import com.group06FALL2017.account.model.User;

public interface UserService {
    void save(User user);

    User findByUsername(String username);
}
