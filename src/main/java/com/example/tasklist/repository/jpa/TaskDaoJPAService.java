package com.example.tasklist.repository.jpa;

import com.example.tasklist.model.entity.Task;
import com.example.tasklist.repository.TaskDao;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository("taskDaoJPA")
@Primary
public class TaskDaoJPAService implements TaskDao {
    private final TaskRepository taskRepository;

    public TaskDaoJPAService(final TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @Override
    public Task createTask(final Task task) {
        return taskRepository.save(task);
    }

    @Override
    public Task updateTask(final Task task) {
        return taskRepository.save(task);
    }

    @Override
    public void deleteTaskById(final Long taskId) {
        taskRepository.deleteById(taskId);
    }

    @Override
    public Optional<Task> getTaskById(final Long taskId) {
        return taskRepository.findById(taskId);
    }

    @Override
    public List<Task> getTasksByUserId(final Long userId) {
        return taskRepository.findAllByOwnerId(userId);
    }

    @Override
    public List<Task> getAllExpiringTasks(
            final LocalDateTime start,
            final LocalDateTime end
    ) {
        return taskRepository.getAllByExpirationDateBetween(start, end);
    }
}
