package com.example.tasklist.service.impl;

import com.example.tasklist.exception.NotFoundException;
import com.example.tasklist.model.entity.Task;
import com.example.tasklist.model.enums.Status;
import com.example.tasklist.repository.TaskDao;
import com.example.tasklist.service.TaskService;
import com.example.tasklist.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@Slf4j
public class TaskServiceImpl implements TaskService {
    private final UserService userService;
    private final TaskDao taskDao;

    TaskServiceImpl(UserService userService, @Qualifier("taskDaoJDBC") TaskDao taskDao) {
        this.userService = userService;
        this.taskDao = taskDao;
    }

    @Override
    @Transactional
    public Task create(Long userId, Task task) {
        log.info("Создание задачи {} для пользователя с id {}", task, userId);
        task.setOwner(userService.getById(userId));
        task.setStatus(Status.TODO);

        return taskDao.createTask(task);
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

        return taskDao.updateTask(taskFromDB);
    }

    @Override
    @Transactional
    public void delete(Long taskId) {
        log.info("Удаление задачи с id {}", taskId);
        taskDao.deleteTaskById(taskId);
    }

    @Override
    public Task getById(Long taskId) {
        log.info("Вывод задачи с id {}", taskId);
        return taskDao.getTaskById(taskId)
                .orElseThrow(() -> new NotFoundException("Задачи с таким id не существует"));
    }

    @Override
    public List<Task> getAllByUserId(Long userId) {
        log.info("Вывод всех задачи пользователя с id {}", userId);
        userService.getById(userId);

        return taskDao.getTasksByUserId(userId);
    }
}
