package com.example.tasklist.repository.jpa;

import com.example.tasklist.model.entity.User;
import com.example.tasklist.repository.PostgreSQLExtension;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ExtendWith(PostgreSQLExtension.class)
public class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;

    @Nested
    class FindByUsername {
        @Test
        void shouldFind() {
            User user = User.builder()
                    .username("username" + UUID.randomUUID())
                    .name("name")
                    .password("password")
                    .build();
            userRepository.save(user);

            Optional<User> userFind = userRepository.findByUsername(user.getUsername());

            assertThat(userFind).isPresent();
            assertThat(userFind.get()
                    .getUsername()).isEqualTo(user.getUsername());
        }

        @Test
        void shouldNotFind() {
            String username = "username" + UUID.randomUUID();
            Optional<User> userFind = userRepository.findByUsername(username);

            assertThat(userFind).isNotPresent();
        }
    }
}
