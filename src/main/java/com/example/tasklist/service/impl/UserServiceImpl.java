package com.example.tasklist.service.impl;

import com.example.tasklist.exception.ForbiddenException;
import com.example.tasklist.exception.NotFoundException;
import com.example.tasklist.model.entity.User;
import com.example.tasklist.model.enums.Role;
import com.example.tasklist.repository.UserDao;
import com.example.tasklist.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
@Transactional(readOnly = true)
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserDao userDao;

    public UserServiceImpl(@Qualifier("userDaoJPA") final UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    @Transactional
    public User create(final User user) {
        log.info("Создание пользователя {}", user);
        checkIsUsernameExist(user.getUsername());
        if (!user.getPassword()
                .equals(user.getPasswordConfirmation())) {
            throw new ForbiddenException(
                    "Пароль и его подтверждение не совпадают"
            );
        }
        user.setRoles(Set.of(Role.ROLE_USER));

        return userDao.createUser(user);
    }

    @Override
    @Transactional
    public User update(final User user) {
        log.info("Обновление пользователя {}", user);
        User userFromDB = getById(user.getId());
        userFromDB.setName(user.getName());
        if (user.getUsername() != null
                && !user.getUsername()
                .equals(userFromDB.getUsername())
        ) {
            checkIsUsernameExist(user.getUsername());
            userFromDB.setUsername(user.getUsername());
        }
        userFromDB.setPassword(user.getPassword());

        return userDao.updateUser(userFromDB);
    }

    @Override
    @Transactional
    public void delete(final Long userId) {
        log.info("Удаление пользователя с id {}", userId);
        userDao.deleteUserById(userId);
    }

    @Override
    public User getById(final Long userId) {
        log.info("Вывод пользователя с id {}", userId);
        return userDao.getUserById(userId)
                .orElseThrow(() -> new NotFoundException(
                        "Пользователя с таким id не существует"
                ));
    }

    private void checkIsUsernameExist(final String username) {
        if (userDao.getUserByUsername(username)
                .isPresent()) {
            throw new ForbiddenException(
                    "Пользователь с таким Username уже существует"
            );
        }
    }
}
