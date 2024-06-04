package com.example.tasklist.service;

import com.example.tasklist.model.entity.Task;
import com.example.tasklist.model.entity.User;
import com.example.tasklist.service.impl.ReminderServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ReminderServiceImplTest {
    @Mock
    UserService userService;

    @Mock
    TaskService taskService;

    @Mock
    MailService mailService;

    @InjectMocks
    ReminderServiceImpl reminderService;

    User user = User.builder()
            .id(10L)
            .username("username" + UUID.randomUUID())
            .name("name")
            .build();

    Task task = Task.builder()
            .id(20L)
            .title("title")
            .description("description")
            .owner(user)
            .build();

    @Test
    void remindTaskExpiration() {
        when(taskService.getAllExpiringTasks(any())).thenReturn(List.of(task));
        when(userService.getById(any())).thenReturn(user);

        reminderService.remindTaskExpiration();

        verify(taskService, times(1)).getAllExpiringTasks(any());
        verify(userService, times(1)).getById(any());
        verify(mailService, times(1)).sendMail(any(), any());
    }
}
