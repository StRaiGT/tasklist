package com.example.tasklist.repository.jdbc;

import com.example.tasklist.model.entity.User;
import com.example.tasklist.model.enums.Role;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

@Component
public class UserRowMapper {
    public static User mapUser(ResultSet rs) throws SQLException {
        User user = new User();
        Set<Role> roles = new HashSet<>();

        while (rs.next()) {
            user = User.builder()
                    .id(rs.getLong("id"))
                    .name(rs.getString("name"))
                    .username(rs.getString("username"))
                    .password(rs.getString("password"))
                    .build();
            roles.add(Role.valueOf(rs.getString("role")));
            user.setRoles(roles);
        }

        if (user.getId() == null) {
            return null;
        }
        return user;
    }
}
