package com.example.tasklist.service;

import com.example.tasklist.model.entity.Task;

import java.util.List;

public interface TaskService {
    Task create(Long userId, Task task);

    Task update(Task task);

    void delete(Long taskId);

    Task getById(Long id);

    List<Task> getAllByUserId(Long userId);
}
