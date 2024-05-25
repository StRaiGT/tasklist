package com.example.tasklist.repository;

import com.example.tasklist.model.entity.User;

import java.util.Optional;

public interface UserDao {
    User createUser(User user);

    User updateUser(User user);

    void deleteUserById(Long userId);

    Optional<User> getUserById(Long userId);

    Optional<User> getUserByUsername(String username);
}
