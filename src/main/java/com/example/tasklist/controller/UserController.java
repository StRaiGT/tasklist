package com.example.tasklist.controller;

import com.example.tasklist.model.dto.TaskDto;
import com.example.tasklist.model.dto.UserDto;
import com.example.tasklist.model.entity.Task;
import com.example.tasklist.model.entity.User;
import com.example.tasklist.model.mapper.TaskMapper;
import com.example.tasklist.model.mapper.UserMapper;
import com.example.tasklist.model.validation.OnCreate;
import com.example.tasklist.model.validation.OnUpdate;
import com.example.tasklist.service.TaskService;
import com.example.tasklist.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final TaskService taskService;
    private final UserMapper userMapper;
    private final TaskMapper taskMapper;

    @PutMapping
    public UserDto updateUser(
            @Validated(OnUpdate.class) @RequestBody final UserDto userDto
    ) {
        User userFromDto = userMapper.toEntity(userDto);
        User updatedUser = userService.update(userFromDto);

        return userMapper.toDto(updatedUser);
    }

    @DeleteMapping("/{id}")
    public void deleteUserById(
            @PathVariable(name = "id") final Long userId
    ) {
        userService.delete(userId);
    }

    @GetMapping("/{id}")
    public UserDto getUserById(
            @PathVariable(name = "id") final Long userId
    ) {
        User user = userService.getById(userId);

        return userMapper.toDto(user);
    }

    @PostMapping("/{id}/tasks")
    public TaskDto createTask(
            @PathVariable(name = "id") final Long userId,
            @Validated(OnCreate.class) @RequestBody final TaskDto taskDto
    ) {
        Task taskFromDto = taskMapper.toEntity(taskDto);
        Task createdTask = taskService.create(userId, taskFromDto);

        return taskMapper.toDto(createdTask);
    }

    @GetMapping("/{id}/tasks")
    public List<TaskDto> getTasksByUserId(
            @PathVariable(name = "id") final Long userId
    ) {
        List<Task> tasks = taskService.getAllByUserId(userId);

        return taskMapper.toDto(tasks);
    }
}
