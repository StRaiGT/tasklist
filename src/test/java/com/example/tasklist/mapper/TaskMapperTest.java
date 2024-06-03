package com.example.tasklist.mapper;

import com.example.tasklist.model.dto.TaskCreateRequest;
import com.example.tasklist.model.dto.TaskResponse;
import com.example.tasklist.model.dto.TaskUpdateRequest;
import com.example.tasklist.model.entity.Task;
import com.example.tasklist.model.entity.User;
import com.example.tasklist.model.enums.Status;
import com.example.tasklist.model.mapper.TaskMapperImpl;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
public class TaskMapperTest {
    @InjectMocks
    private TaskMapperImpl taskMapper;

    User user = User.builder()
            .id(10L)
            .build();

    Task task = Task.builder()
            .id(20L)
            .title("title")
            .description("description")
            .owner(user)
            .status(Status.TODO)
            .expirationDate(LocalDateTime.of(2025, 9, 5, 10, 0, 0))
            .build();

    TaskCreateRequest taskCreateRequest = TaskCreateRequest.builder()
            .title(task.getTitle())
            .description(task.getDescription())
            .expirationDate(task.getExpirationDate())
            .build();

    TaskUpdateRequest taskUpdateRequest = TaskUpdateRequest.builder()
            .id(task.getId())
            .title(task.getTitle())
            .description(task.getDescription())
            .status(task.getStatus())
            .expirationDate(task.getExpirationDate())
            .build();

    @Nested
    class CreateRequestToTask {
        @Test
        public void shouldMap() {
            Task result = taskMapper.toTask(taskCreateRequest);

            assertThat(result.getId()).isNull();
            assertThat(result.getTitle()).isEqualTo(taskCreateRequest.getTitle());
            assertThat(result.getDescription()).isEqualTo(taskCreateRequest.getDescription());
            assertThat(result.getOwner()).isNull();
            assertThat(result.getStatus()).isNull();
            assertThat(result.getExpirationDate()).isEqualTo(taskCreateRequest.getExpirationDate());
        }

        @Test
        public void shouldReturnNull() {
            Task result = taskMapper.toTask((TaskCreateRequest) null);

            assertThat(result).isNull();
        }
    }

    @Nested
    class UpdateRequestToTask {
        @Test
        public void shouldMap() {
            Task result = taskMapper.toTask(taskUpdateRequest);

            assertThat(result.getId()).isEqualTo(taskUpdateRequest.getId());
            assertThat(result.getTitle()).isEqualTo(taskUpdateRequest.getTitle());
            assertThat(result.getDescription()).isEqualTo(taskUpdateRequest.getDescription());
            assertThat(result.getOwner()).isNull();
            assertThat(result.getStatus()).isEqualTo(taskUpdateRequest.getStatus());
            assertThat(result.getExpirationDate()).isEqualTo(taskUpdateRequest.getExpirationDate());
        }

        @Test
        public void shouldReturnNull() {
            Task result = taskMapper.toTask((TaskUpdateRequest) null);

            assertThat(result).isNull();
        }
    }

    @Nested
    class ToTaskResponse {
        @Test
        public void shouldMap() {
            TaskResponse result = taskMapper.toTaskResponse(task);

            assertThat(result.getId()).isEqualTo(task.getId());
            assertThat(result.getTitle()).isEqualTo(task.getTitle());
            assertThat(result.getDescription()).isEqualTo(task.getDescription());
            assertThat(result.getStatus()).isEqualTo(task.getStatus());
            assertThat(result.getExpirationDate()).isEqualTo(task.getExpirationDate());
        }

        @Test
        public void shouldReturnNull() {
            TaskResponse result = taskMapper.toTaskResponse((Task) null);

            assertThat(result).isNull();
        }
    }

    @Nested
    class ToTaskResponseList {
        @Test
        public void shouldMap() {
            List<TaskResponse> results = taskMapper.toTaskResponse(List.of(task));

            assertThat(results.size()).isEqualTo(1);

            TaskResponse result = results.get(0);

            assertThat(result.getId()).isEqualTo(task.getId());
            assertThat(result.getTitle()).isEqualTo(task.getTitle());
            assertThat(result.getDescription()).isEqualTo(task.getDescription());
            assertThat(result.getStatus()).isEqualTo(task.getStatus());
            assertThat(result.getExpirationDate()).isEqualTo(task.getExpirationDate());
        }

        @Test
        public void shouldReturnEmpty() {
            List<TaskResponse> results = taskMapper.toTaskResponse(List.of());

            assertThat(results).isEmpty();
        }

        @Test
        public void shouldReturnNull() {
            List<TaskResponse> results = taskMapper.toTaskResponse((List<Task>) null);

            assertThat(results).isNull();
        }
    }
}
