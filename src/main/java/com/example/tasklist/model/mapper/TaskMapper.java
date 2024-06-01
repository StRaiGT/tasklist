package com.example.tasklist.model.mapper;

import com.example.tasklist.model.dto.TaskCreateRequest;
import com.example.tasklist.model.dto.TaskResponse;
import com.example.tasklist.model.dto.TaskUpdateRequest;
import com.example.tasklist.model.entity.Task;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TaskMapper {
    Task toTask(TaskCreateRequest taskCreateRequest);

    Task toTask(TaskUpdateRequest taskUpdateRequest);

    TaskResponse toTaskResponse(Task task);

    List<TaskResponse> toTaskResponse(List<Task> tasks);
}
