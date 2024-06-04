package com.example.tasklist.service;

import com.example.tasklist.exception.NotFoundException;
import com.example.tasklist.model.entity.Task;
import com.example.tasklist.model.entity.User;
import com.example.tasklist.model.enums.Status;
import com.example.tasklist.repository.TaskDao;
import com.example.tasklist.service.impl.TaskServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TaskServiceImplTest {
    @Mock
    UserService userService;

    @Mock
    TaskDao taskDao;

    @InjectMocks
    TaskServiceImpl taskServiceImpl;

    User user = User.builder()
            .id(10L)
            .build();

    Task task;

    @BeforeEach
    void beforeEach() {
        task = Task.builder()
                .id(20L)
                .title("title")
                .description("description")
                .owner(user)
                .status(Status.TODO)
                .expirationDate(LocalDateTime.of(2025, 9, 5, 10, 0, 0))
                .build();
    }

    @Test
    void create() {
        task.setId(null);
        task.setOwner(null);
        task.setStatus(null);

        when(userService.getById(user.getId())).thenReturn(user);

        taskServiceImpl.create(user.getId(), task);

        ArgumentCaptor<Task> taskArgumentCaptor = ArgumentCaptor.forClass(Task.class);
        verify(userService, times(1)).getById(user.getId());
        verify(taskDao, times(1)).createTask(taskArgumentCaptor.capture());

        Task captureTask = taskArgumentCaptor.getValue();
        assertThat(captureTask.getId()).isNull();
        assertThat(captureTask.getTitle()).isEqualTo(task.getTitle());
        assertThat(captureTask.getDescription()).isEqualTo(task.getDescription());
        assertThat(captureTask.getOwner()
                .getId()).isEqualTo(user.getId());
        assertThat(captureTask.getStatus()).isEqualTo(Status.TODO);
        assertThat(captureTask.getExpirationDate()).isEqualTo(task.getExpirationDate());
    }

    @Test
    void update() {
        Task updatedTask = Task.builder()
                .id(task.getId())
                .title("updatedTitle")
                .description("updatedDescription")
                .owner(task.getOwner())
                .status(Status.DONE)
                .expirationDate(LocalDateTime.of(3025, 5, 5, 10, 30, 0))
                .build();

        when(taskDao.getTaskById(updatedTask.getId())).thenReturn(Optional.ofNullable(task));

        taskServiceImpl.update(updatedTask);

        ArgumentCaptor<Task> taskArgumentCaptor = ArgumentCaptor.forClass(Task.class);
        verify(taskDao, times(1)).getTaskById(updatedTask.getId());
        verify(taskDao, times(1)).updateTask(taskArgumentCaptor.capture());

        Task captureTask = taskArgumentCaptor.getValue();
        assertThat(captureTask.getId()).isEqualTo(task.getId());
        assertThat(captureTask.getTitle()).isEqualTo(updatedTask.getTitle());
        assertThat(captureTask.getDescription()).isEqualTo(updatedTask.getDescription());
        assertThat(captureTask.getOwner()
                .getId()).isEqualTo(user.getId());
        assertThat(captureTask.getStatus()).isEqualTo(updatedTask.getStatus());
        assertThat(captureTask.getExpirationDate()).isEqualTo(updatedTask.getExpirationDate());
    }

    @Test
    void delete() {
        taskServiceImpl.delete(task.getId());

        verify(taskDao, times(1)).deleteTaskById(task.getId());
    }


    @Nested
    class GetById {
        @Test
        void shouldGet() {
            when(taskDao.getTaskById(task.getId())).thenReturn(Optional.ofNullable(task));

            taskServiceImpl.getById(task.getId());

            verify(taskDao, times(1)).getTaskById(task.getId());
        }

        @Test
        void shouldThrowIfTaskIdNotExists() {
            when(taskDao.getTaskById(task.getId())).thenReturn(Optional.empty());

            assertThatThrownBy(() -> taskServiceImpl.getById(task.getId()))
                    .isInstanceOf(NotFoundException.class)
                    .hasMessage("Задачи с таким id не существует");
            verify(taskDao, times(1)).getTaskById(task.getId());
        }
    }

    @Nested
    class GetAllByUserId {
        @Test
        void shouldGetOneTask() {
            when(userService.getById(user.getId())).thenReturn(user);
            when(taskDao.getTasksByUserId(user.getId())).thenReturn(List.of(task));

            List<Task> tasks = taskServiceImpl.getAllByUserId(user.getId());

            assertThat(tasks.size()).isEqualTo(1);

            Task taskFromDB = tasks.get(0);

            assertThat(taskFromDB.getId()).isEqualTo(task.getId());
            assertThat(taskFromDB.getTitle()).isEqualTo(task.getTitle());
            assertThat(taskFromDB.getDescription()).isEqualTo(task.getDescription());
            assertThat(taskFromDB.getOwner()
                    .getId()).isEqualTo(user.getId());
            assertThat(taskFromDB.getStatus()).isEqualTo(task.getStatus());
            assertThat(taskFromDB.getExpirationDate()).isEqualTo(task.getExpirationDate());

            verify(userService, times(1)).getById(user.getId());
            verify(taskDao, times(1)).getTasksByUserId(user.getId());
        }

        @Test
        void shouldGetEmpty() {
            when(userService.getById(user.getId())).thenReturn(user);
            when(taskDao.getTasksByUserId(user.getId())).thenReturn(List.of());

            List<Task> tasks = taskServiceImpl.getAllByUserId(user.getId());

            assertThat(tasks).isEmpty();

            verify(userService, times(1)).getById(user.getId());
            verify(taskDao, times(1)).getTasksByUserId(user.getId());
        }
    }

    @Nested
    class GetAllExpiringTasks {
        @Test
        void shouldGetOneTask() {
            when(taskDao.getAllExpiringTasks(any(), any())).thenReturn(List.of(task));

            List<Task> tasks = taskServiceImpl.getAllExpiringTasks(Duration.ofHours(1));

            assertThat(tasks.size()).isEqualTo(1);

            Task taskFromDB = tasks.get(0);

            assertThat(taskFromDB.getId()).isEqualTo(task.getId());
            assertThat(taskFromDB.getTitle()).isEqualTo(task.getTitle());
            assertThat(taskFromDB.getDescription()).isEqualTo(task.getDescription());
            assertThat(taskFromDB.getOwner()
                    .getId()).isEqualTo(user.getId());
            assertThat(taskFromDB.getStatus()).isEqualTo(task.getStatus());
            assertThat(taskFromDB.getExpirationDate()).isEqualTo(task.getExpirationDate());

            verify(taskDao, times(1)).getAllExpiringTasks(any(), any());
        }

        @Test
        void shouldGetEmpty() {
            when(taskDao.getAllExpiringTasks(any(), any())).thenReturn(List.of());

            List<Task> tasks = taskServiceImpl.getAllExpiringTasks(Duration.ofHours(1));

            assertThat(tasks).isEmpty();

            verify(taskDao, times(1)).getAllExpiringTasks(any(), any());
        }
    }
}
