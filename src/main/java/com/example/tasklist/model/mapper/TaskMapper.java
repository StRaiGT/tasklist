package com.example.tasklist.model.mapper;

import com.example.tasklist.model.dto.TaskDto;
import com.example.tasklist.model.entity.Task;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TaskMapper extends Mappable<Task, TaskDto> {
}
