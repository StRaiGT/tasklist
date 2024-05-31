package com.example.tasklist.service.impl;

import com.example.tasklist.exception.NotFoundException;
import com.example.tasklist.model.entity.Task;
import com.example.tasklist.model.enums.Status;
import com.example.tasklist.repository.TaskDao;
import com.example.tasklist.service.TaskService;
import com.example.tasklist.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class TaskServiceImpl implements TaskService {
    private final UserService userService;
    private final TaskDao taskDao;

    @Override
    @Transactional
    @Cacheable(
            value = "TaskService::getById",
            condition = "#task.id!=null",
            key = "#task.id"
    )
    public Task create(
            final Long userId,
            final Task task
    ) {
        log.info("Создание задачи {} для пользователя с id {}", task, userId);

        task.setOwner(userService.getById(userId));
        task.setStatus(Status.TODO);
        return taskDao.createTask(task);
    }

    @Override
    @Transactional
    @CachePut(
            value = "TaskService::getById",
            key = "#task.id"
    )
    public Task update(final Task task) {
        log.info("Обновление задачи {}", task);

        Task taskFromDB = getById(task.getId());
        if (task.getStatus() != null) {
            taskFromDB.setStatus(task.getStatus());
        }
        taskFromDB.setTitle(task.getTitle());
        taskFromDB.setDescription(task.getDescription());
        taskFromDB.setExpirationDate(task.getExpirationDate());

        return taskDao.updateTask(taskFromDB);
    }

    @Override
    @Transactional
    @CacheEvict(
            value = "TaskService::getById",
            key = "#taskId"
    )
    public void delete(final Long taskId) {
        log.info("Удаление задачи с id {}", taskId);

        taskDao.deleteTaskById(taskId);
    }

    @Override
    @Cacheable(
            value = "TaskService::getById",
            key = "#taskId"
    )
    public Task getById(final Long taskId) {
        log.info("Вывод задачи с id {}", taskId);

        return taskDao.getTaskById(taskId)
                .orElseThrow(() -> new NotFoundException(
                        "Задачи с таким id не существует"
                ));
    }

    @Override
    public List<Task> getAllByUserId(final Long userId) {
        log.info("Вывод всех задачи пользователя с id {}", userId);

        userService.getById(userId);
        return taskDao.getTasksByUserId(userId);
    }
}
