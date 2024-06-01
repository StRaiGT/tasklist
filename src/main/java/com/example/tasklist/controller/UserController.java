package com.example.tasklist.controller;

import com.example.tasklist.exception.ApiError;
import com.example.tasklist.model.dto.TaskCreateRequest;
import com.example.tasklist.model.dto.TaskResponse;
import com.example.tasklist.model.dto.UserResponse;
import com.example.tasklist.model.dto.UserUpdateRequest;
import com.example.tasklist.model.entity.Task;
import com.example.tasklist.model.entity.User;
import com.example.tasklist.model.mapper.TaskMapper;
import com.example.tasklist.model.mapper.UserMapper;
import com.example.tasklist.service.TaskService;
import com.example.tasklist.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
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
    @PreAuthorize("@cse.canAccessUser(#userUpdateRequest.id)")
    @Operation(summary = "Update user")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "OK",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(
                                    implementation = UserResponse.class))
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
    public UserResponse updateUser(
            @Valid @RequestBody final UserUpdateRequest userUpdateRequest
    ) {
        User userFromDto = userMapper.toUser(userUpdateRequest);
        User updatedUser = userService.update(userFromDto);

        return userMapper.toUserResponse(updatedUser);
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
                                    implementation = UserResponse.class))
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
    public UserResponse getUserById(
            @PathVariable(name = "id") final Long userId
    ) {
        User user = userService.getById(userId);

        return userMapper.toUserResponse(user);
    }

    @PostMapping("/{id}/tasks")
    @PreAuthorize("@cse.canAccessUser(#userId)")
    @Operation(summary = "Create task by user id")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "OK",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(
                                    implementation = TaskResponse.class))
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
    public TaskResponse createTask(
            @PathVariable(name = "id") final Long userId,
            @Valid @RequestBody final TaskCreateRequest taskCreateRequest
    ) {
        Task taskFromDto = taskMapper.toTask(taskCreateRequest);
        Task createdTask = taskService.create(userId, taskFromDto);

        return taskMapper.toTaskResponse(createdTask);
    }

    @GetMapping("/{id}/tasks")
    @PreAuthorize("@cse.canAccessUser(#userId)")
    @Operation(summary = "Get all user tasks by user id")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "OK",
                    content = {@Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(
                                    implementation = TaskResponse.class)))
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
    public List<TaskResponse> getTasksByUserId(
            @PathVariable(name = "id") final Long userId
    ) {
        List<Task> tasks = taskService.getAllByUserId(userId);

        return taskMapper.toTaskResponse(tasks);
    }
}
