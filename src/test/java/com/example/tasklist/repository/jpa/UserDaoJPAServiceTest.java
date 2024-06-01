package com.example.tasklist.repository.jpa;

import com.example.tasklist.model.entity.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class UserDaoJPAServiceTest {
    @Mock
    UserRepository userRepository;

    @InjectMocks
    UserDaoJPAService userDaoJPAService;

    User user = User.builder()
            .id(1L)
            .username("username")
            .build();

    @Test
    void createUser() {
        userDaoJPAService.createUser(user);

        verify(userRepository).save(user);
    }

    @Test
    void updateUser() {
        userDaoJPAService.updateUser(user);

        verify(userRepository).save(user);
    }

    @Test
    void deleteUserById() {
        userDaoJPAService.deleteUserById(user.getId());

        verify(userRepository).deleteById(user.getId());
    }

    @Test
    void getUserById() {
        userDaoJPAService.getUserById(user.getId());

        verify(userRepository).findById(user.getId());
    }

    @Test
    void getUserByUsername() {
        userDaoJPAService.getUserByUsername(user.getUsername());

        verify(userRepository).findByUsername(user.getUsername());
    }
}
