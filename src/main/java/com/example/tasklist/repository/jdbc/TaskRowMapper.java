package com.example.tasklist.repository.jdbc;

import com.example.tasklist.model.entity.Task;
import com.example.tasklist.model.entity.User;
import com.example.tasklist.model.enums.Status;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Component
public class TaskRowMapper {
    public static Task mapTask(ResultSet rs) throws SQLException {
        Task task = new Task();

        while (rs.next()) {
            task = Task.builder()
                    .id(rs.getLong("id"))
                    .title(rs.getString("title"))
                    .description(rs.getString("description"))
                    .status(Status.valueOf(rs.getString("status")))
                    .owner(User.builder()
                            .id(rs.getLong("owner_id"))
                            .build())
                    .build();
            Timestamp timestamp = rs.getTimestamp("expiration_date");
            if (timestamp != null) {
                task.setExpirationDate(timestamp.toLocalDateTime());
            }
        }

        if (task.getId() == null) {
            return null;
        }
        return task;
    }

    public static List<Task> mapTasks(ResultSet rs) throws SQLException {
        List<Task> tasks = new ArrayList<>();

        while (rs.next()) {
            Task task = Task.builder()
                    .id(rs.getLong("id"))
                    .title(rs.getString("title"))
                    .description(rs.getString("description"))
                    .status(Status.valueOf(rs.getString("status")))
                    .owner(User.builder()
                            .id(rs.getLong("owner_id"))
                            .build())
                    .build();
            Timestamp timestamp = rs.getTimestamp("expiration_date");
            if (timestamp != null) {
                task.setExpirationDate(timestamp.toLocalDateTime());
            }

            tasks.add(task);
        }

        return tasks;
    }
}
