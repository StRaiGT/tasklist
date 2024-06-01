package com.example.tasklist.controller;

import com.example.tasklist.exception.ApiError;
import com.example.tasklist.model.dto.TaskResponse;
import com.example.tasklist.model.dto.TaskUpdateRequest;
import com.example.tasklist.model.entity.Task;
import com.example.tasklist.model.mapper.TaskMapper;
import com.example.tasklist.service.TaskService;
import io.swagger.v3.oas.annotations.Operation;
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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/tasks")
@RequiredArgsConstructor
@Tag(name = "Task Controller", description = "Task API")
public class TaskController {
    private final TaskService taskService;
    private final TaskMapper taskMapper;

    @PutMapping
    @PreAuthorize("@cse.canAccessTask(#taskUpdateRequest.id)")
    @Operation(summary = "Update task")
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
    public TaskResponse updateTask(
            @Valid @RequestBody final TaskUpdateRequest taskUpdateRequest
    ) {
        Task task = taskMapper.toTask(taskUpdateRequest);
        Task updatedTask = taskService.update(task);

        return taskMapper.toTaskResponse(updatedTask);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("@cse.canAccessTask(#id)")
    @Operation(summary = "Delete task")
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
    public void deleteTaskById(
            @PathVariable final Long id
    ) {
        taskService.delete(id);
    }

    @GetMapping("/{id}")
    @PreAuthorize("@cse.canAccessTask(#id)")
    @Operation(summary = "Get task by id")
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
    public TaskResponse getTaskById(
            @PathVariable final Long id
    ) {
        Task task = taskService.getById(id);

        return taskMapper.toTaskResponse(task);
    }
}
