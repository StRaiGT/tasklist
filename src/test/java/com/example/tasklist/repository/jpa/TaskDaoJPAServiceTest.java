package com.example.tasklist.repository.jpa;

import com.example.tasklist.model.entity.Task;
import com.example.tasklist.model.entity.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class TaskDaoJPAServiceTest {
    @Mock
    TaskRepository taskRepository;

    @InjectMocks
    TaskDaoJPAService taskDaoJPAService;

    User user = User.builder()
            .id(1L)
            .build();

    Task task = Task.builder()
            .id(1L)
            .owner(user)
            .build();

    @Test
    void createTask() {
        taskDaoJPAService.createTask(task);

        verify(taskRepository).save(task);
    }

    @Test
    void updateTask() {
        taskDaoJPAService.updateTask(task);

        verify(taskRepository).save(task);
    }

    @Test
    void deleteTaskById() {
        taskDaoJPAService.deleteTaskById(task.getId());

        verify(taskRepository).deleteById(task.getId());
    }

    @Test
    void getTaskById() {
        taskDaoJPAService.getTaskById(task.getId());

        verify(taskRepository).findById(task.getId());
    }

    @Test
    void getTasksByUserId() {
        taskDaoJPAService.getTasksByUserId(task.getId());

        verify(taskRepository).findAllByOwnerId(task.getOwner().getId());
    }
}
