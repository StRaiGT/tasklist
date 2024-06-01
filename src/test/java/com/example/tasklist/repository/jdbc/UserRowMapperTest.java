package com.example.tasklist.repository.jdbc;

import com.example.tasklist.model.entity.User;
import com.example.tasklist.model.enums.Role;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UserRowMapperTest {
    ResultSet resultSet = mock(ResultSet.class);

    @Nested
    class MapUser {
        @Test
        void shouldMap() throws SQLException {
            User expected = User.builder()
                    .id(10L)
                    .username("username" + UUID.randomUUID())
                    .name("name")
                    .password("password")
                    .roles(Set.of(Role.ROLE_USER))
                    .build();

            when(resultSet.next()).thenReturn(true)
                    .thenReturn(false);
            when(resultSet.getLong("id")).thenReturn(expected.getId());
            when(resultSet.getString("name")).thenReturn(expected.getName());
            when(resultSet.getString("username")).thenReturn(expected.getUsername());
            when(resultSet.getString("password")).thenReturn(expected.getPassword());
            when(resultSet.getString("role")).thenReturn(
                    expected.getRoles()
                            .stream()
                            .toList()
                            .get(0)
                            .toString()
            );

            User actual = UserRowMapper.mapUser(resultSet);

            assertThat(actual).isEqualTo(expected);
        }

        @Test
        void shouldReturnNull() throws SQLException {
            when(resultSet.next()).thenReturn(false);

            User actual = UserRowMapper.mapUser(resultSet);

            assertThat(actual).isNull();
        }
    }

}
