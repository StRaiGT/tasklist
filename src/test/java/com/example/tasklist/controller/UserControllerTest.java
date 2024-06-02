package com.example.tasklist.controller;

import com.example.tasklist.exception.ErrorHandler;
import com.example.tasklist.model.dto.TaskCreateRequest;
import com.example.tasklist.model.dto.TaskResponse;
import com.example.tasklist.model.dto.UserResponse;
import com.example.tasklist.model.dto.UserUpdateRequest;
import com.example.tasklist.model.entity.Task;
import com.example.tasklist.model.entity.User;
import com.example.tasklist.model.enums.Role;
import com.example.tasklist.model.enums.Status;
import com.example.tasklist.model.mapper.TaskMapperImpl;
import com.example.tasklist.model.mapper.UserMapperImpl;
import com.example.tasklist.service.TaskService;
import com.example.tasklist.service.UserService;
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
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest {
    @Mock
    UserService userService;

    @Mock
    TaskService taskService;

    @Mock
    UserMapperImpl userMapper;

    @Mock
    TaskMapperImpl taskMapper;

    @InjectMocks
    UserController userController;

    ObjectMapper mapper = new ObjectMapper()
            .registerModule(new JavaTimeModule());
    MockMvc mvc;
    User user;
    Task task;

    @BeforeEach
    void beforeEach() {
        mvc = MockMvcBuilders
                .standaloneSetup(userController)
                .setControllerAdvice(ErrorHandler.class)
                .build();

        user = User.builder()
                .id(10L)
                .username("username" + UUID.randomUUID())
                .name("name")
                .password("password")
                .roles(Set.of(Role.ROLE_USER))
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
    class UpdateUser {
        @Test
        void shouldUpdate() throws Exception {
            UserUpdateRequest userUpdateRequest = UserUpdateRequest.builder()
                    .id(user.getId())
                    .username(user.getUsername())
                    .name(user.getName())
                    .password("updatedPassword")
                    .build();

            UserResponse userResponse = UserResponse.builder()
                    .id(user.getId())
                    .username(user.getUsername())
                    .name(user.getName())
                    .build();

            when(userMapper.toUser((UserUpdateRequest) any())).thenCallRealMethod();
            when(userService.update(any())).thenReturn(user);
            when(userMapper.toUserResponse(any())).thenCallRealMethod();

            mvc.perform(put("/api/v1/users")
                            .content(mapper.writeValueAsString(userUpdateRequest))
                            .characterEncoding(StandardCharsets.UTF_8)
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(content().json(mapper.writeValueAsString(userResponse)));

            verify(userMapper, times(1)).toUser((UserUpdateRequest) any());
            verify(userService, times(1)).update(any());
            verify(userMapper, times(1)).toUserResponse(any());
        }

        @Test
        void shouldReturnBadRequestIfNotValid() throws Exception {
            UserUpdateRequest userUpdateRequest = new UserUpdateRequest();

            mvc.perform(put("/api/v1/users")
                            .content(mapper.writeValueAsString(userUpdateRequest))
                            .characterEncoding(StandardCharsets.UTF_8)
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest());

            verify(userService, never()).update(any());
        }
    }

    @Test
    void deleteById() throws Exception {
        mvc.perform(delete("/api/v1/users/10")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(userService, times(1)).delete(10L);
    }

    @Test
    void getUserById() throws Exception {
        UserResponse userResponse = UserResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .name(user.getName())
                .build();

        when(userService.getById(10L)).thenReturn(user);
        when(userMapper.toUserResponse(any())).thenCallRealMethod();

        mvc.perform(get("/api/v1/users/10")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(userResponse)));

        verify(userService, times(1)).getById(10L);
        verify(userMapper, times(1)).toUserResponse(any());
    }

    @Nested
    class CreateTask {
        @Test
        void shouldCreate() throws Exception {
            TaskCreateRequest taskCreateRequest = TaskCreateRequest.builder()
                    .title(task.getTitle())
                    .description(task.getDescription())
                    .expirationDate(task.getExpirationDate())
                    .build();

            TaskResponse taskResponse = TaskResponse.builder()
                    .id(task.getId())
                    .title(task.getTitle())
                    .description(task.getDescription())
                    .status(task.getStatus())
                    .expirationDate(task.getExpirationDate())
                    .build();

            when(taskMapper.toTask((TaskCreateRequest) any())).thenCallRealMethod();
            when(taskService.create(any(), any())).thenReturn(task);
            when(taskMapper.toTaskResponse((Task) any())).thenCallRealMethod();

            mvc.perform(post("/api/v1/users/10/tasks")
                            .content(mapper.writeValueAsString(taskCreateRequest))
                            .characterEncoding(StandardCharsets.UTF_8)
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(content().json(mapper.writeValueAsString(taskResponse)));

            verify(taskMapper, times(1)).toTask((TaskCreateRequest) any());
            verify(taskService, times(1)).create(any(), any());
            verify(taskMapper, times(1)).toTaskResponse((Task) any());
        }

        @Test
        void shouldReturnBadRequestIfNotValid() throws Exception {
            TaskCreateRequest taskCreateRequest = new TaskCreateRequest();

            mvc.perform(post("/api/v1/users/10/tasks")
                            .content(mapper.writeValueAsString(taskCreateRequest))
                            .characterEncoding(StandardCharsets.UTF_8)
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest());

            verify(taskService, never()).create(any(), any());
        }
    }

    @Nested
    class GetTasksByUserId {
        @Test
        void shouldGetOne() throws Exception {
            TaskResponse taskResponse = TaskResponse.builder()
                    .id(task.getId())
                    .title(task.getTitle())
                    .description(task.getDescription())
                    .status(task.getStatus())
                    .expirationDate(task.getExpirationDate())
                    .build();

            when(taskService.getAllByUserId(task.getOwner()
                    .getId())).thenReturn(List.of(task));
            when(taskMapper.toTaskResponse(List.of(task))).thenCallRealMethod();
            when(taskMapper.toTaskResponse((Task) any())).thenCallRealMethod();


            mvc.perform(get("/api/v1/users/10/tasks")
                            .characterEncoding(StandardCharsets.UTF_8)
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(content().json(mapper.writeValueAsString(List.of(taskResponse))));

            verify(taskService, times(1)).getAllByUserId(task.getOwner()
                    .getId());
            verify(taskMapper, times(1)).toTaskResponse(List.of(task));
            verify(taskMapper, times(1)).toTaskResponse((Task) any());
        }

        @Test
        void shouldGetEmpty() throws Exception {
            when(taskService.getAllByUserId(task.getOwner()
                    .getId())).thenReturn(List.of());
            when(taskMapper.toTaskResponse(List.of())).thenReturn(List.of());


            mvc.perform(get("/api/v1/users/10/tasks")
                            .characterEncoding(StandardCharsets.UTF_8)
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(content().json(mapper.writeValueAsString(List.of())));

            verify(taskService, times(1)).getAllByUserId(task.getOwner()
                    .getId());
            verify(taskMapper, times(1)).toTaskResponse(List.of());
        }
    }
}
