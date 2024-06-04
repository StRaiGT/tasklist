package com.example.tasklist.repository;

import com.example.tasklist.model.entity.Task;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface TaskDao {
    Task createTask(Task task);

    Task updateTask(Task task);

    void deleteTaskById(Long taskId);

    Optional<Task> getTaskById(Long taskId);

    List<Task> getTasksByUserId(Long userId);

    List<Task> getAllExpiringTasks(LocalDateTime start, LocalDateTime end);
}
