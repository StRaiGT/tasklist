package com.example.tasklist.repository.jpa;

import com.example.tasklist.model.entity.User;
import com.example.tasklist.repository.UserDao;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository("userDaoJPA")
public class UserDaoJPAService implements UserDao {
    private final UserRepository userRepository;

    public UserDaoJPAService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User createUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public User updateUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public void deleteUserById(Long userId) {
        userRepository.deleteById(userId);
    }

    @Override
    public Optional<User> getUserById(Long userId) {
        return userRepository.findById(userId);
    }

    @Override
    public Optional<User> getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }
}
