package com.example.tasklist.repository.jpa;

import com.example.tasklist.model.entity.Task;
import com.example.tasklist.repository.TaskDao;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository("taskDaoJPA")
public class TaskDaoJPAService implements TaskDao {
    private final TaskRepository taskRepository;

    public TaskDaoJPAService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @Override
    public Task createTask(Task task) {
        return taskRepository.save(task);
    }

    @Override
    public Task updateTask(Task task) {
        return taskRepository.save(task);
    }

    @Override
    public void deleteTaskById(Long taskId) {
        taskRepository.deleteById(taskId);
    }

    @Override
    public Optional<Task> getTaskById(Long taskId) {
        return taskRepository.findById(taskId);
    }

    @Override
    public List<Task> getTasksByUserId(Long userId) {
        return taskRepository.findAllByOwnerId(userId);
    }
}
