package com.example.tasklist.service.impl;

import com.example.tasklist.exception.NotFoundException;
import com.example.tasklist.model.entity.Task;
import com.example.tasklist.model.enums.Status;
import com.example.tasklist.repository.TaskRepository;
import com.example.tasklist.service.TaskService;
import com.example.tasklist.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class TaskServiceImpl implements TaskService {
    private final UserService userService;
    private final TaskRepository taskRepository;

    @Override
    @Transactional
    public Task create(Long userId, Task task) {
        log.info("Создание задачи {} для пользователя с id {}", task, userId);
        task.setOwner(userService.getById(userId));
        task.setStatus(Status.TODO);

        return taskRepository.save(task);
    }

    @Override
    @Transactional
    public Task update(Task task) {
        log.info("Обновление задачи {}", task);
        Task taskFromDB = getById(task.getId());
        if (task.getStatus() != null) {
            taskFromDB.setStatus(task.getStatus());
        }
        taskFromDB.setTitle(task.getTitle());
        taskFromDB.setDescription(task.getDescription());
        taskFromDB.setExpirationDate(task.getExpirationDate());

        return taskRepository.save(taskFromDB);
    }

    @Override
    @Transactional
    public void delete(Long taskId) {
        log.info("Удаление задачи с id {}", taskId);
        taskRepository.deleteById(taskId);
    }

    @Override
    public Task getById(Long taskId) {
        log.info("Вывод задачи с id {}", taskId);
        return taskRepository.findById(taskId)
                .orElseThrow(() -> new NotFoundException("Задачи с таким id не существует"));
    }

    @Override
    public List<Task> getAllByUserId(Long userId) {
        log.info("Вывод всех задачи пользователя с id {}", userId);
        return taskRepository.findAllByOwnerId(userId);
    }
}
