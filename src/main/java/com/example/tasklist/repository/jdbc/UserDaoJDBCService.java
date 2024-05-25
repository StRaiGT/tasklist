package com.example.tasklist.repository.jdbc;

import com.example.tasklist.model.entity.User;
import com.example.tasklist.model.enums.Role;
import com.example.tasklist.repository.UserDao;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository("userDaoJDBC")
@RequiredArgsConstructor
public class UserDaoJDBCService implements UserDao {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public User createUser(final User user) {
        final String sqlQuery = """
                INSERT INTO users (name, username, password)
                VALUES (?, ?, ?)
                """;
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(
                connection -> {
                    PreparedStatement ps = connection
                            .prepareStatement(sqlQuery, new String[]{"id"});
                    ps.setString(1, user.getName());
                    ps.setString(2, user.getUsername());
                    ps.setString(3, user.getPassword());
                    return ps;
                },
                keyHolder
        );
        user.setId(Objects.requireNonNull(keyHolder.getKey())
                .longValue());

        insertUserRoles(user);

        return user;
    }

    private void insertUserRoles(final User user) {
        if (user.getRoles() != null) {
            final String sqlQuery = """
                    INSERT INTO users_roles
                    VALUES (?, ?)
                    """;
            List<Object[]> batch = new ArrayList<>();
            user.getRoles()
                    .stream()
                    .map(Role::toString)
                    .distinct()
                    .forEach(role -> batch.add(
                            new Object[]{user.getId(), role})
                    );
            jdbcTemplate.batchUpdate(sqlQuery, batch);
        }
    }

    @Override
    public User updateUser(final User user) {
        final String sqlQuery = """
                UPDATE users
                SET name = ?, username = ?, password = ?
                WHERE id = ?
                """;
        jdbcTemplate.update(
                sqlQuery,
                user.getName(),
                user.getUsername(),
                user.getPassword(),
                user.getId()
        );

        return user;
    }

    @Override
    public void deleteUserById(final Long userId) {
        final String sqlQuery = """
                DELETE FROM users
                WHERE id = ?
                """;
        jdbcTemplate.update(sqlQuery, userId);
    }

    @Override
    public Optional<User> getUserById(final Long userId) {
        final String sqlQuery = """
                SELECT u.id, u.name, u.username, u.password, ur.role
                FROM users AS u
                LEFT JOIN users_roles AS ur ON u.id = ur.user_id
                WHERE u.id = ?
                """;

        return Optional.ofNullable(
                jdbcTemplate.query(sqlQuery, UserRowMapper::mapUser, userId)
        );
    }

    @Override
    public Optional<User> getUserByUsername(final String username) {
        final String sqlQuery = """
                SELECT u.id, u.name, u.username, u.password, ur.role
                FROM users AS u
                LEFT JOIN users_roles AS ur ON u.id = ur.user_id
                WHERE u.username = ?
                """;

        return Optional.ofNullable(
                jdbcTemplate.query(sqlQuery, UserRowMapper::mapUser, username)
        );
    }
}
