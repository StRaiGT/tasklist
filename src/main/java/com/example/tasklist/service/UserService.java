package com.example.tasklist.service;

import com.example.tasklist.model.entity.User;

public interface UserService {
    User create(User user);

    User update(User user);

    void delete(Long id);

    User getById(Long id);

    User getByUsername(String username);
}
