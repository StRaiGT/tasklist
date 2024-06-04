package com.example.tasklist.service.impl;

import com.example.tasklist.model.entity.Task;
import com.example.tasklist.model.entity.User;
import com.example.tasklist.service.MailService;
import com.example.tasklist.service.ReminderService;
import com.example.tasklist.service.TaskService;
import com.example.tasklist.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;
import java.util.Properties;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReminderServiceImpl implements ReminderService {
    private final UserService userService;
    private final TaskService taskService;
    private final MailService mailService;

    @Value("${spring.mail.timeByExpiration}")
    private Duration timeByExpiration;

    @Async("reminderExecutor")
    @Scheduled(cron = "0 0 * * * *")        // каждый час в 0 минут 0 секунд
    @Override
    public void remindTaskExpiration() {
        log.info("Старт цикла оповещений по истекающим задачам.");

        List<Task> tasks = taskService.getAllExpiringTasks(timeByExpiration);
        tasks.forEach(task -> {
            User user = userService.getById(task.getOwner()
                    .getId());

            Properties properties = new Properties();
            properties.setProperty("task.title", task.getTitle());
            properties.setProperty("task.description", task.getDescription());

            mailService.sendMail(user, properties);
        });
    }
}
