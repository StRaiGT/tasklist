package com.example.tasklist.controller;

import com.example.tasklist.exception.ErrorHandler;
import com.example.tasklist.model.dto.TaskResponse;
import com.example.tasklist.model.dto.TaskUpdateRequest;
import com.example.tasklist.model.entity.Task;
import com.example.tasklist.model.entity.User;
import com.example.tasklist.model.enums.Status;
import com.example.tasklist.model.mapper.TaskMapperImpl;
import com.example.tasklist.service.TaskService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class TaskControllerTest {
    @Mock
    TaskService taskService;

    @Mock
    TaskMapperImpl taskMapper;

    @InjectMocks
    TaskController taskController;

    ObjectMapper mapper = new ObjectMapper()
            .registerModule(new JavaTimeModule());
    MockMvc mvc;
    User user = User.builder()
            .id(10L)
            .build();
    Task task;

    @BeforeEach
    void beforeEach() {
        mvc = MockMvcBuilders
                .standaloneSetup(taskController)
                .setControllerAdvice(ErrorHandler.class)
                .build();

        task = Task.builder()
                .id(20L)
                .title("title")
                .description("description")
                .owner(user)
                .status(Status.TODO)
                .expirationDate(LocalDateTime.of(2025, 9, 5, 10, 0, 0))
                .build();
    }

    @Nested
    class UpdateTask {
        @Test
        void shouldUpdate() throws Exception {
            TaskUpdateRequest taskUpdateRequest = TaskUpdateRequest.builder()
                    .id(task.getId())
                    .title(task.getTitle())
                    .description(task.getDescription())
                    .status(task.getStatus())
                    .expirationDate(task.getExpirationDate())
                    .build();

            TaskResponse taskResponse = TaskResponse.builder()
                    .id(task.getId())
                    .title(task.getTitle())
                    .description(task.getDescription())
                    .status(task.getStatus())
                    .expirationDate(task.getExpirationDate())
                    .build();

            when(taskMapper.toTask((TaskUpdateRequest) any())).thenCallRealMethod();
            when(taskService.update(any())).thenReturn(task);
            when(taskMapper.toTaskResponse((Task) any())).thenCallRealMethod();

            mvc.perform(put("/api/v1/tasks")
                            .content(mapper.writeValueAsString(taskUpdateRequest))
                            .characterEncoding(StandardCharsets.UTF_8)
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(content().json(mapper.writeValueAsString(taskResponse)));

            verify(taskMapper, times(1)).toTask((TaskUpdateRequest) any());
            verify(taskService, times(1)).update(any());
            verify(taskMapper, times(1)).toTaskResponse((Task) any());
        }

        @Test
        void shouldReturnBadRequestIfNotValid() throws Exception {
            TaskUpdateRequest taskUpdateRequest = new TaskUpdateRequest();

            mvc.perform(put("/api/v1/tasks")
                            .content(mapper.writeValueAsString(taskUpdateRequest))
                            .characterEncoding(StandardCharsets.UTF_8)
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest());

            verify(taskService, never()).update(any());
        }
    }

    @Test
    void deleteTaskById() throws Exception {
        mvc.perform(delete("/api/v1/tasks/10")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(taskService, times(1)).delete(10L);
    }

    @Test
    void getTaskById() throws Exception {
        TaskResponse taskResponse = TaskResponse.builder()
                .id(task.getId())
                .title(task.getTitle())
                .description(task.getDescription())
                .status(task.getStatus())
                .expirationDate(task.getExpirationDate())
                .build();

        when(taskService.getById(20L)).thenReturn(task);
        when(taskMapper.toTaskResponse(task)).thenCallRealMethod();

        mvc.perform(get("/api/v1/tasks/20")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(taskResponse)));

        verify(taskService, times(1)).getById(20L);
        verify(taskMapper, times(1)).toTaskResponse(task);
    }
}
