package com.example.tasklist.repository.jpa;

import com.example.tasklist.model.entity.Task;
import com.example.tasklist.model.entity.User;
import com.example.tasklist.model.enums.Status;
import com.example.tasklist.repository.PostgreSQLExtension;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ExtendWith(PostgreSQLExtension.class)
public class TaskRepositoryTest {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TaskRepository taskRepository;

    @Nested
    class FindAllByOwnerId {
        @Test
        void shouldFindTwo() {
            User user = User.builder()
                    .username("username" + UUID.randomUUID())
                    .name("name")
                    .password("password")
                    .build();
            user = userRepository.save(user);

            Task task1 = Task.builder()
                    .title("task1")
                    .owner(user)
                    .status(Status.TODO)
                    .build();
            Task task2 = Task.builder()
                    .title("task2")
                    .owner(user)
                    .status(Status.DONE)
                    .build();
            taskRepository.saveAll(List.of(task1, task2));

            List<Task> tasks = taskRepository.findAllByOwnerId(user.getId());

            assertThat(tasks).isNotEmpty();
            assertThat(tasks.size()).isEqualTo(2);
        }

        @Test
        void shouldFindEmpty() {
            User user = User.builder()
                    .username("username" + UUID.randomUUID())
                    .name("name")
                    .password("password")
                    .build();
            user = userRepository.save(user);

            List<Task> tasks = taskRepository.findAllByOwnerId(user.getId());

            assertThat(tasks).isEmpty();
        }
    }
}
