package com.example.tasklist.mapper;

import com.example.tasklist.model.dto.UserCreateRequest;
import com.example.tasklist.model.dto.UserResponse;
import com.example.tasklist.model.dto.UserUpdateRequest;
import com.example.tasklist.model.entity.User;
import com.example.tasklist.model.enums.Role;
import com.example.tasklist.model.mapper.UserMapperImpl;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
public class UserMapperTest {
    @InjectMocks
    private UserMapperImpl userMapper;

    User user = User.builder()
            .id(10L)
            .username("username" + UUID.randomUUID())
            .name("name")
            .password("password")
            .roles(Set.of(Role.ROLE_USER))
            .build();

    UserCreateRequest userCreateRequest = UserCreateRequest.builder()
            .name(user.getName())
            .username(user.getUsername())
            .password(user.getPassword())
            .passwordConfirmation(user.getPassword())
            .build();

    UserUpdateRequest userUpdateRequest = UserUpdateRequest.builder()
            .id(user.getId())
            .username("updatedUsername")
            .name("updatedName")
            .password("updatedPassword")
            .build();

    @Nested
    class CreateRequestToUser {
        @Test
        public void shouldMap() {
            User result = userMapper.toUser(userCreateRequest);

            assertThat(result.getId()).isNull();
            assertThat(result.getName()).isEqualTo(userCreateRequest.getName());
            assertThat(result.getUsername()).isEqualTo(userCreateRequest.getUsername());
            assertThat(result.getPassword()).isEqualTo(userCreateRequest.getPassword());
            assertThat(result.getPasswordConfirmation()).isEqualTo(userCreateRequest.getPasswordConfirmation());
            assertThat(result.getRoles()).isNull();
        }

        @Test
        public void shouldReturnNull() {
            User result = userMapper.toUser((UserCreateRequest) null);

            assertThat(result).isNull();
        }
    }

    @Nested
    class UpdateRequestToUser {
        @Test
        public void shouldMap() {
            User result = userMapper.toUser(userUpdateRequest);

            assertThat(result.getId()).isEqualTo(userUpdateRequest.getId());
            assertThat(result.getName()).isEqualTo(userUpdateRequest.getName());
            assertThat(result.getUsername()).isEqualTo(userUpdateRequest.getUsername());
            assertThat(result.getPassword()).isEqualTo(userUpdateRequest.getPassword());
            assertThat(result.getPasswordConfirmation()).isNull();
            assertThat(result.getRoles()).isNull();
        }

        @Test
        public void shouldReturnNull() {
            User result = userMapper.toUser((UserUpdateRequest) null);

            assertThat(result).isNull();
        }
    }

    @Nested
    class ToUserResponse {
        @Test
        public void shouldMap() {
            UserResponse result = userMapper.toUserResponse(user);

            assertThat(result.getId()).isEqualTo(user.getId());
            assertThat(result.getName()).isEqualTo(user.getName());
            assertThat(result.getUsername()).isEqualTo(user.getUsername());
        }

        @Test
        public void shouldReturnNull() {
            UserResponse result = userMapper.toUserResponse(null);

            assertThat(result).isNull();
        }
    }

}
