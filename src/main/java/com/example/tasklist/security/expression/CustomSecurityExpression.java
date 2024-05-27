package com.example.tasklist.security.expression;

import com.example.tasklist.model.entity.Task;
import com.example.tasklist.model.enums.Role;
import com.example.tasklist.security.UserDetailsImpl;
import com.example.tasklist.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component("cse")
@RequiredArgsConstructor
public class CustomSecurityExpression {
    private final TaskService taskService;

    public Boolean canAccessUser(final Long userId) {
        UserDetailsImpl userDetails = getUserDetails();
        Long authUserId = getAuthUserId(userDetails);
        List<Role> roles = getRoles(userDetails);

        return roles.contains(Role.ROLE_ADMIN) || authUserId.equals(userId);
    }

    public Boolean canAccessTask(final Long taskId) {
        UserDetailsImpl userDetails = getUserDetails();
        Long authUserId = getAuthUserId(userDetails);
        List<Role> roles = getRoles(userDetails);
        if (roles.contains(Role.ROLE_ADMIN)) {
            return true;
        }

        Task task = taskService.getById(taskId);

        return task.getOwner()
                .getId()
                .equals(authUserId);
    }

    private UserDetailsImpl getUserDetails() {
        Authentication authentication = SecurityContextHolder.getContext()
                .getAuthentication();

        return (UserDetailsImpl) authentication.getPrincipal();
    }

    private Long getAuthUserId(final UserDetailsImpl userDetails) {
        return userDetails.getUser()
                .getId();
    }

    private List<Role> getRoles(final UserDetailsImpl userDetails) {
        return userDetails.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .map(Role::valueOf)
                .toList();
    }
}
