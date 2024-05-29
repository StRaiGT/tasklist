package com.example.tasklist.controller;

import com.example.tasklist.exception.ApiError;
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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
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
@Tag(name = "User Controller", description = "User API")
public class UserController {
    private final UserService userService;
    private final TaskService taskService;
    private final UserMapper userMapper;
    private final TaskMapper taskMapper;

    @PutMapping
    @PreAuthorize("@cse.canAccessUser(#userDto.id)")
    @Operation(summary = "Update user")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "OK",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(
                                    implementation = UserDto.class))
                    }
            ),
            @ApiResponse(
                    responseCode = "Error codes",
                    description = "Error responses",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(
                                    implementation = ApiError.class))
                    }
            )
    })
    public UserDto updateUser(
            @Validated(OnUpdate.class) @RequestBody final UserDto userDto
    ) {
        User userFromDto = userMapper.toEntity(userDto);
        User updatedUser = userService.update(userFromDto);

        return userMapper.toDto(updatedUser);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("@cse.canAccessUser(#userId)")
    @Operation(summary = "Delete user")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "OK"
            ),
            @ApiResponse(
                    responseCode = "Error codes",
                    description = "Error responses",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(
                                    implementation = ApiError.class))
                    }
            )
    })
    public void deleteUserById(
            @PathVariable(name = "id") final Long userId
    ) {
        userService.delete(userId);
    }

    @GetMapping("/{id}")
    @PreAuthorize("@cse.canAccessUser(#userId)")
    @Operation(summary = "Get user by id")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "OK",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(
                                    implementation = UserDto.class))
                    }
            ),
            @ApiResponse(
                    responseCode = "Error codes",
                    description = "Error responses",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(
                                    implementation = ApiError.class))
                    }
            )
    })
    public UserDto getUserById(
            @PathVariable(name = "id") final Long userId
    ) {
        User user = userService.getById(userId);

        return userMapper.toDto(user);
    }

    @PostMapping("/{id}/tasks")
    @PreAuthorize("@cse.canAccessUser(#userId)")
    @Operation(summary = "Create task")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "OK",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(
                                    implementation = TaskDto.class))
                    }
            ),
            @ApiResponse(
                    responseCode = "Error codes",
                    description = "Error responses",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(
                                    implementation = ApiError.class))
                    }
            )
    })
    public TaskDto createTask(
            @PathVariable(name = "id") final Long userId,
            @Validated(OnCreate.class) @RequestBody final TaskDto taskDto
    ) {
        Task taskFromDto = taskMapper.toEntity(taskDto);
        Task createdTask = taskService.create(userId, taskFromDto);

        return taskMapper.toDto(createdTask);
    }

    @GetMapping("/{id}/tasks")
    @PreAuthorize("@cse.canAccessUser(#userId)")
    @Operation(summary = "Get all user tasks")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "OK",
                    content = {@Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(
                                    implementation = TaskDto.class)))
                    }
            ),
            @ApiResponse(
                    responseCode = "Error codes",
                    description = "Error responses",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(
                                    implementation = ApiError.class))
                    }
            )
    })
    public List<TaskDto> getTasksByUserId(
            @PathVariable(name = "id") final Long userId
    ) {
        List<Task> tasks = taskService.getAllByUserId(userId);

        return taskMapper.toDto(tasks);
    }
}
