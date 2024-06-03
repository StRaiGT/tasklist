package com.example.tasklist.service;

import com.example.tasklist.exception.ForbiddenException;
import com.example.tasklist.exception.NotFoundException;
import com.example.tasklist.model.entity.User;
import com.example.tasklist.model.enums.Role;
import com.example.tasklist.repository.UserDao;
import com.example.tasklist.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {
    @Mock
    UserDao userDao;

    @Mock
    PasswordEncoder passwordEncoder;

    @InjectMocks
    UserServiceImpl userServiceImpl;

    User user;
    User updatedUser;

    @BeforeEach
    void beforeEach() {
        user = User.builder()
                .id(10L)
                .username("username" + UUID.randomUUID())
                .name("name")
                .password("password")
                .passwordConfirmation("password")
                .roles(Set.of(Role.ROLE_USER))
                .build();
    }

    @Nested
    class create {
        @Test
        void shouldCreate() {
            user.setId(null);

            when(userDao.getUserByUsername(user.getUsername())).thenReturn(Optional.empty());
            when(passwordEncoder.encode(user.getPassword())).thenReturn("encodePassword");

            userServiceImpl.create(user);

            ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(User.class);
            verify(userDao, times(1)).getUserByUsername(user.getUsername());
            verify(userDao, times(1)).createUser(userArgumentCaptor.capture());

            User captureUser = userArgumentCaptor.getValue();
            assertThat(captureUser.getId()).isNull();
            assertThat(captureUser.getUsername()).isEqualTo(user.getUsername());
            assertThat(captureUser.getName()).isEqualTo(user.getName());
            assertThat(captureUser.getPassword()).isEqualTo("encodePassword");
            assertThat(captureUser.getRoles()).isEqualTo(Set.of(Role.ROLE_USER));
        }

        @Test
        void shouldThrowIfPasswordNotMatchConfirmation() {
            user.setPassword("password");
            user.setPasswordConfirmation("badPassword");

            when(userDao.getUserByUsername(user.getUsername())).thenReturn(Optional.empty());

            assertThatThrownBy(() -> userServiceImpl.create(user))
                    .isInstanceOf(ForbiddenException.class)
                    .hasMessage("Пароль и его подтверждение не совпадают");
            verify(userDao, never()).createUser(any());
        }

        @Test
        void shouldThrowIfUsernameAlreadyExists() {
            when(userDao.getUserByUsername(user.getUsername())).thenReturn(Optional.ofNullable(user));

            assertThatThrownBy(() -> userServiceImpl.create(user))
                    .isInstanceOf(ForbiddenException.class)
                    .hasMessage("Пользователь с таким Username уже существует");
            verify(userDao, never()).createUser(any());
        }
    }

    @Nested
    class update {
        @BeforeEach
        void beforeEachUpdate() {
            updatedUser = User.builder()
                    .id(user.getId())
                    .username("updatedUsername" + UUID.randomUUID())
                    .name("updatedName")
                    .password("updatedPassword")
                    .roles(user.getRoles())
                    .build();
        }

        @Test
        void shouldUpdate() {
            when(userDao.getUserById(user.getId())).thenReturn(Optional.ofNullable(user));
            when(userDao.getUserByUsername(updatedUser.getUsername())).thenReturn(Optional.empty());
            when(passwordEncoder.encode(updatedUser.getPassword())).thenReturn(updatedUser.getPassword());

            userServiceImpl.update(updatedUser);

            ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(User.class);
            verify(userDao, times(1)).getUserById(user.getId());
            verify(userDao, times(1)).getUserByUsername(user.getUsername());
            verify(userDao, times(1)).updateUser(userArgumentCaptor.capture());

            User captureUser = userArgumentCaptor.getValue();
            assertThat(captureUser.getId()).isEqualTo(updatedUser.getId());
            assertThat(captureUser.getUsername()).isEqualTo(updatedUser.getUsername());
            assertThat(captureUser.getName()).isEqualTo(updatedUser.getName());
            assertThat(captureUser.getPassword()).isEqualTo(updatedUser.getPassword());
            assertThat(captureUser.getRoles()).isEqualTo(updatedUser.getRoles());
        }

        @Test
        void shouldThrowIfUsernameAlreadyExists() {
            when(userDao.getUserById(user.getId())).thenReturn(Optional.ofNullable(user));
            when(userDao.getUserByUsername(updatedUser.getUsername())).thenReturn(Optional.ofNullable(user));

            assertThatThrownBy(() -> userServiceImpl.update(updatedUser))
                    .isInstanceOf(ForbiddenException.class)
                    .hasMessage("Пользователь с таким Username уже существует");
            verify(userDao, never()).updateUser(any());
        }
    }

    @Test
    void delete() {
        userServiceImpl.delete(user.getId());

        verify(userDao, times(1)).deleteUserById(user.getId());
    }

    @Nested
    class GetById {
        @Test
        void shouldGet() {
            when(userDao.getUserById(user.getId())).thenReturn(Optional.ofNullable(user));

            userServiceImpl.getById(user.getId());

            verify(userDao, times(1)).getUserById(user.getId());
        }

        @Test
        void shouldThrowIfIdNotExists() {
            when(userDao.getUserById(user.getId())).thenReturn(Optional.empty());

            assertThatThrownBy(() -> userServiceImpl.getById(user.getId()))
                    .isInstanceOf(NotFoundException.class)
                    .hasMessage("Пользователя с таким id не существует");
            verify(userDao, times(1)).getUserById(user.getId());
        }
    }

    @Nested
    class GetByUsername {
        @Test
        void shouldGet() {
            when(userDao.getUserByUsername(user.getUsername())).thenReturn(Optional.ofNullable(user));

            userServiceImpl.getByUsername(user.getUsername());

            verify(userDao, times(1)).getUserByUsername(user.getUsername());
        }

        @Test
        void shouldThrowIfUsernameNotExists() {
            when(userDao.getUserByUsername(user.getUsername())).thenReturn(Optional.empty());

            assertThatThrownBy(() -> userServiceImpl.getByUsername(user.getUsername()))
                    .isInstanceOf(NotFoundException.class)
                    .hasMessage("Пользователя с таким id не существует");
            verify(userDao, times(1)).getUserByUsername(user.getUsername());
        }
    }
}
