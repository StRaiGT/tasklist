package com.example.tasklist.repository.jdbc;

import com.example.tasklist.model.entity.User;
import com.example.tasklist.model.enums.Role;
import com.example.tasklist.repository.PostgreSQLExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ExtendWith(PostgreSQLExtension.class)
public class UserDaoJDBCServiceTest {
    UserDaoJDBCService userDaoJDBCService = new UserDaoJDBCService(PostgreSQLExtension.getJdbcTemplate());
    User user;

    @BeforeEach
    void beforeEach() {
        user = User.builder()
                .username("username" + UUID.randomUUID())
                .name("name")
                .password("password")
                .roles(Set.of(Role.ROLE_USER))
                .build();
    }

    @Test
    void createUser() {
        userDaoJDBCService.createUser(user);
        Optional<User> optionalUser = userDaoJDBCService.getUserByUsername(user.getUsername());

        assertThat(optionalUser).isPresent()
                .hasValueSatisfying(userFromDB -> {
                    assertThat(userFromDB.getId()).isNotNull();
                    assertThat(userFromDB.getUsername()).isEqualTo(user.getUsername());
                    assertThat(userFromDB.getName()).isEqualTo(user.getName());
                    assertThat(userFromDB.getPassword()).isEqualTo(user.getPassword());
                    assertThat(userFromDB.getRoles()).isEqualTo(user.getRoles());
                });
    }

    @Test
    void updateUser() {
        User updatedUser = User.builder()
                .username("updatedUsername" + UUID.randomUUID())
                .name("updatedName")
                .password("updatedPassword")
                .build();

        User createdUser = userDaoJDBCService.createUser(user);
        updatedUser.setId(createdUser.getId());
        userDaoJDBCService.updateUser(updatedUser);
        Optional<User> optionalUser = userDaoJDBCService.getUserByUsername(updatedUser.getUsername());

        assertThat(optionalUser).isPresent()
                .hasValueSatisfying(userFromDB -> {
                    assertThat(userFromDB.getId()).isEqualTo(createdUser.getId());
                    assertThat(userFromDB.getUsername()).isEqualTo(updatedUser.getUsername());
                    assertThat(userFromDB.getName()).isEqualTo(updatedUser.getName());
                    assertThat(userFromDB.getPassword()).isEqualTo(updatedUser.getPassword());
                });
    }

    @Test
    void deleteUserById() {
        userDaoJDBCService.createUser(user);
        Optional<User> optionalUser = userDaoJDBCService.getUserByUsername(user.getUsername());

        assertThat(optionalUser).isPresent();

        userDaoJDBCService.deleteUserById(optionalUser.get()
                .getId());
        Optional<User> deletedUser = userDaoJDBCService.getUserByUsername(user.getUsername());

        assertThat(deletedUser).isNotPresent();
    }

    @Test
    void getUserById() {
        User createdUser = userDaoJDBCService.createUser(user);
        Optional<User> optionalUser = userDaoJDBCService.getUserById(createdUser.getId());

        assertThat(optionalUser).isPresent()
                .hasValueSatisfying(userFromDB -> {
                    assertThat(userFromDB.getId()).isEqualTo(createdUser.getId());
                    assertThat(userFromDB.getUsername()).isEqualTo(user.getUsername());
                    assertThat(userFromDB.getName()).isEqualTo(user.getName());
                    assertThat(userFromDB.getPassword()).isEqualTo(user.getPassword());
                    assertThat(userFromDB.getRoles()).isEqualTo(user.getRoles());
                });
    }
}
