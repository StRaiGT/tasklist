package com.example.tasklist.repository.jpa;

import com.example.tasklist.model.entity.User;
import com.example.tasklist.repository.UserDao;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository("userDaoJPA")
@Primary
public class UserDaoJPAService implements UserDao {
    private final UserRepository userRepository;

    public UserDaoJPAService(final UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User createUser(final User user) {
        return userRepository.save(user);
    }

    @Override
    public User updateUser(final User user) {
        return userRepository.save(user);
    }

    @Override
    public void deleteUserById(final Long userId) {
        userRepository.deleteById(userId);
    }

    @Override
    public Optional<User> getUserById(final Long userId) {
        return userRepository.findById(userId);
    }

    @Override
    public Optional<User> getUserByUsername(final String username) {
        return userRepository.findByUsername(username);
    }
}
