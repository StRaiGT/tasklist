package com.example.tasklist.repository.jdbc;

import com.example.tasklist.model.entity.Task;
import com.example.tasklist.model.entity.User;
import com.example.tasklist.model.enums.Role;
import com.example.tasklist.model.enums.Status;
import com.example.tasklist.repository.PostgreSQLExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;


@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ExtendWith(PostgreSQLExtension.class)
public class TaskDaoJDBCServiceTest {
    TaskDaoJDBCService taskDaoJDBCService = new TaskDaoJDBCService(PostgreSQLExtension.getJdbcTemplate());
    UserDaoJDBCService userDaoJDBCService = new UserDaoJDBCService(PostgreSQLExtension.getJdbcTemplate());
    User user;
    Task task;

    @BeforeEach
    void beforeEach() {
        user = User.builder()
                .username("username" + UUID.randomUUID())
                .name("name")
                .password("password")
                .roles(Set.of(Role.ROLE_USER))
                .build();
        user = userDaoJDBCService.createUser(user);

        task = Task.builder()
                .title("title")
                .description("description")
                .owner(user)
                .status(Status.TODO)
                .expirationDate(LocalDateTime.of(2025, 9, 5, 10, 0, 0))
                .build();
    }

    @Nested
    class CreateTask {
        @Test
        void createTaskWithExpirationDate() {
            Task createdTask = taskDaoJDBCService.createTask(task);
            Optional<Task> optionalTask = taskDaoJDBCService.getTaskById(createdTask.getId());

            assertThat(optionalTask).isPresent()
                    .hasValueSatisfying(taskFromDB -> {
                        assertThat(taskFromDB.getId()).isEqualTo(createdTask.getId());
                        assertThat(taskFromDB.getTitle()).isEqualTo(task.getTitle());
                        assertThat(taskFromDB.getDescription()).isEqualTo(task.getDescription());
                        assertThat(taskFromDB.getOwner()
                                .getId()).isEqualTo(task.getOwner()
                                .getId());
                        assertThat(taskFromDB.getStatus()).isEqualTo(task.getStatus());
                        assertThat(taskFromDB.getExpirationDate()).isEqualTo(task.getExpirationDate());
                    });
        }

        @Test
        void createTaskWithoutExpirationDate() {
            task.setExpirationDate(null);
            Task createdTask = taskDaoJDBCService.createTask(task);
            Optional<Task> optionalTask = taskDaoJDBCService.getTaskById(createdTask.getId());

            assertThat(optionalTask).isPresent()
                    .hasValueSatisfying(taskFromDB -> {
                        assertThat(taskFromDB.getId()).isEqualTo(createdTask.getId());
                        assertThat(taskFromDB.getTitle()).isEqualTo(task.getTitle());
                        assertThat(taskFromDB.getDescription()).isEqualTo(task.getDescription());
                        assertThat(taskFromDB.getOwner()
                                .getId()).isEqualTo(task.getOwner()
                                .getId());
                        assertThat(taskFromDB.getStatus()).isEqualTo(task.getStatus());
                        assertThat(taskFromDB.getExpirationDate()).isEqualTo(task.getExpirationDate());
                    });
        }
    }

    @Test
    void updateTask() {
        Task updatedTask = Task.builder()
                .title("updatedTitle")
                .description("updatedDescription")
                .owner(user)
                .status(Status.DONE)
                .expirationDate(LocalDateTime.of(3025, 5, 2, 18, 0, 0))
                .build();

        Task createdTask = taskDaoJDBCService.createTask(task);
        updatedTask.setId(createdTask.getId());
        taskDaoJDBCService.updateTask(updatedTask);
        Optional<Task> optionalTask = taskDaoJDBCService.getTaskById(updatedTask.getId());

        assertThat(optionalTask).isPresent()
                .hasValueSatisfying(taskFromDB -> {
                    assertThat(taskFromDB.getId()).isEqualTo(createdTask.getId());
                    assertThat(taskFromDB.getTitle()).isEqualTo(updatedTask.getTitle());
                    assertThat(taskFromDB.getDescription()).isEqualTo(updatedTask.getDescription());
                    assertThat(taskFromDB.getStatus()).isEqualTo(updatedTask.getStatus());
                    assertThat(taskFromDB.getExpirationDate()).isEqualTo(updatedTask.getExpirationDate());
                });
    }

    @Test
    void deleteTaskById() {
        Task createdTask = taskDaoJDBCService.createTask(task);
        taskDaoJDBCService.deleteTaskById(createdTask.getId());
        Optional<Task> optionalTask = taskDaoJDBCService.getTaskById(createdTask.getId());

        assertThat(optionalTask).isNotPresent();
    }

    @Nested
    class GetTasksByUserId {
        @Test
        void shouldGetOneTask() {
            Task createdTask = taskDaoJDBCService.createTask(task);
            List<Task> tasks = taskDaoJDBCService.getTasksByUserId(createdTask.getOwner()
                    .getId());

            assertThat(tasks.size()).isEqualTo(1);

            Task taskFromDB = tasks.get(0);

            assertThat(taskFromDB.getId()).isEqualTo(createdTask.getId());
            assertThat(taskFromDB.getTitle()).isEqualTo(task.getTitle());
            assertThat(taskFromDB.getDescription()).isEqualTo(task.getDescription());
            assertThat(taskFromDB.getStatus()).isEqualTo(task.getStatus());
            assertThat(taskFromDB.getExpirationDate()).isEqualTo(task.getExpirationDate());
        }

        @Test
        void shouldGetEmpty() {
            List<Task> tasks = taskDaoJDBCService.getTasksByUserId(user.getId());

            assertThat(tasks).isEmpty();
        }
    }
}
