package com.example.tasklist.repository.jdbc;

import com.example.tasklist.model.entity.Task;
import com.example.tasklist.repository.TaskDao;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.sql.Types;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository("taskDaoJDBC")
@RequiredArgsConstructor
public class TaskDaoJDBCService implements TaskDao {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public Task createTask(final Task task) {
        final String sqlQuery = """
                INSERT INTO tasks (title, description, status,
                owner_id, expiration_date)
                VALUES (?, ?, ?, ?, ?)
                """;
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(
                connection -> {
                    PreparedStatement ps = connection
                            .prepareStatement(sqlQuery, new String[]{"id"});
                    ps.setString(1, task.getTitle());
                    ps.setString(2, task.getDescription());
                    ps.setString(3, task.getStatus()
                            .toString());
                    ps.setLong(4, task.getOwner()
                            .getId());
                    if (task.getExpirationDate() == null) {
                        ps.setNull(5, Types.TIMESTAMP);
                    } else {
                        ps.setTimestamp(5,
                                Timestamp.valueOf(task.getExpirationDate()));
                    }
                    return ps;
                },
                keyHolder
        );
        task.setId(Objects.requireNonNull(keyHolder.getKey())
                .longValue());

        return task;
    }

    @Override
    public Task updateTask(final Task task) {
        final String sqlQuery = """
                UPDATE tasks
                SET title = ?, description = ?,
                status = ?, expiration_date = ?
                WHERE id = ?
                """;
        jdbcTemplate.update(
                sqlQuery,
                task.getTitle(),
                task.getDescription(),
                task.getStatus()
                        .toString(),
                task.getExpirationDate(),
                task.getId()
        );

        return task;
    }

    @Override
    public void deleteTaskById(final Long taskId) {
        final String sqlQuery = """
                DELETE FROM tasks
                WHERE id = ?
                """;
        jdbcTemplate.update(sqlQuery, taskId);
    }

    @Override
    public Optional<Task> getTaskById(final Long taskId) {
        final String sqlQuery = """
                SELECT id, title, description, status,
                owner_id, expiration_date
                FROM tasks
                WHERE id = ?
                """;

        return Optional.ofNullable(
                jdbcTemplate.query(sqlQuery, TaskRowMapper::mapTask, taskId)
        );
    }

    @Override
    public List<Task> getTasksByUserId(final Long userId) {
        final String sqlQuery = """
                SELECT t.id, t.title, t.description, t.status,
                t.owner_id, t.expiration_date
                FROM tasks AS t
                LEFT JOIN users AS u ON u.id = t.owner_id
                WHERE u.id = ?
                """;

        return jdbcTemplate.query(sqlQuery, TaskRowMapper::mapTasks, userId);
    }

    @Override
    public List<Task> getAllExpiringTasks(
            final LocalDateTime start,
            final LocalDateTime end
    ) {
        final String sqlQuery = """
                SELECT id, title, description, status,
                owner_id, expiration_date
                FROM tasks
                WHERE expiration_date is not null
                AND expiration_date BETWEEN ? AND ?
                """;

        return jdbcTemplate.query(
                sqlQuery,
                TaskRowMapper::mapTasks,
                start,
                end);
    }
}
