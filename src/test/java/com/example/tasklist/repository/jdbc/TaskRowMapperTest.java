package com.example.tasklist.repository.jdbc;

import com.example.tasklist.model.entity.Task;
import com.example.tasklist.model.entity.User;
import com.example.tasklist.model.enums.Status;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TaskRowMapperTest {
    ResultSet resultSet = mock(ResultSet.class);

    @Nested
    class MapTask {
        @Test
        void shouldMap() throws SQLException {
            User user = User.builder()
                    .id(1L)
                    .build();

            Task expected = Task.builder()
                    .id(10L)
                    .title("title" + UUID.randomUUID())
                    .description("description")
                    .status(Status.TODO)
                    .owner(user)
                    .expirationDate(LocalDateTime.of(2025, 5, 2, 18, 0, 0))
                    .build();

            when(resultSet.next()).thenReturn(true)
                    .thenReturn(false);
            when(resultSet.getLong("id")).thenReturn(expected.getId());
            when(resultSet.getString("title")).thenReturn(expected.getTitle());
            when(resultSet.getString("description")).thenReturn(expected.getDescription());
            when(resultSet.getString("status")).thenReturn(expected.getStatus()
                    .toString());
            when(resultSet.getLong("owner_id")).thenReturn(expected.getOwner()
                    .getId());
            when(resultSet.getTimestamp("expiration_date")).thenReturn(
                    Timestamp.valueOf(expected.getExpirationDate())
            );

            Task actual = TaskRowMapper.mapTask(resultSet);

            assertThat(actual).isEqualTo(expected);
        }

        @Test
        void shouldReturnNull() throws SQLException {
            when(resultSet.next()).thenReturn(false);

            Task actual = TaskRowMapper.mapTask(resultSet);

            assertThat(actual).isNull();
        }
    }

    @Nested
    class MapTasks {
        @Test
        void shouldMap() throws SQLException {
            User user = User.builder()
                    .id(1L)
                    .build();

            Task expected = Task.builder()
                    .id(10L)
                    .title("title" + UUID.randomUUID())
                    .description("description")
                    .status(Status.TODO)
                    .owner(user)
                    .expirationDate(LocalDateTime.of(2025, 5, 2, 18, 0, 0))
                    .build();

            when(resultSet.next()).thenReturn(true)
                    .thenReturn(false);
            when(resultSet.getLong("id")).thenReturn(expected.getId());
            when(resultSet.getString("title")).thenReturn(expected.getTitle());
            when(resultSet.getString("description")).thenReturn(expected.getDescription());
            when(resultSet.getString("status")).thenReturn(expected.getStatus()
                    .toString());
            when(resultSet.getLong("owner_id")).thenReturn(expected.getOwner()
                    .getId());
            when(resultSet.getTimestamp("expiration_date")).thenReturn(
                    Timestamp.valueOf(expected.getExpirationDate())
            );

            List<Task> actualTasks = TaskRowMapper.mapTasks(resultSet);

            assertThat(actualTasks.size()).isEqualTo(1);
            assertThat(actualTasks.get(0)).isEqualTo(expected);
        }

        @Test
        void shouldReturnEmpty() throws SQLException {
            when(resultSet.next()).thenReturn(false);

            List<Task> actualTasks = TaskRowMapper.mapTasks(resultSet);

            assertThat(actualTasks).isEmpty();
        }
    }

}
