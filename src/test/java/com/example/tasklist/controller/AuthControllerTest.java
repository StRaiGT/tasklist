package com.example.tasklist.controller;

import com.example.tasklist.exception.ErrorHandler;
import com.example.tasklist.model.dto.AuthLoginRequest;
import com.example.tasklist.model.dto.AuthRefreshRequest;
import com.example.tasklist.model.dto.AuthResponse;
import com.example.tasklist.model.dto.UserCreateRequest;
import com.example.tasklist.model.dto.UserResponse;
import com.example.tasklist.model.entity.User;
import com.example.tasklist.model.enums.Role;
import com.example.tasklist.model.mapper.UserMapperImpl;
import com.example.tasklist.service.AuthService;
import com.example.tasklist.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.nio.charset.StandardCharsets;
import java.util.Set;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class AuthControllerTest {
    @Mock
    AuthService authService;

    @Mock
    UserService userService;

    @Mock
    UserMapperImpl userMapper;

    @InjectMocks
    AuthController authController;

    ObjectMapper mapper = new ObjectMapper()
            .registerModule(new JavaTimeModule());
    MockMvc mvc;
    User user;

    @BeforeEach
    void beforeEach() {
        mvc = MockMvcBuilders
                .standaloneSetup(authController)
                .setControllerAdvice(ErrorHandler.class)
                .build();

        user = User.builder()
                .id(10L)
                .username("username" + UUID.randomUUID())
                .name("name")
                .password("password")
                .roles(Set.of(Role.ROLE_USER))
                .build();
    }

    @Nested
    class Login {
        @Test
        void shouldLogin() throws Exception {
            AuthLoginRequest authLoginRequest = AuthLoginRequest.builder()
                    .username(user.getUsername())
                    .password(user.getPassword())
                    .build();

            AuthResponse authResponse = AuthResponse.builder()
                    .username(user.getUsername())
                    .accessToken("accessToken")
                    .refreshToken("refreshToken")
                    .build();

            when(authService.login(any())).thenReturn(authResponse);

            mvc.perform(post("/api/v1/auth/login")
                            .content(mapper.writeValueAsString(authLoginRequest))
                            .characterEncoding(StandardCharsets.UTF_8)
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(content().json(mapper.writeValueAsString(authResponse)));

            verify(authService, times(1)).login(any());
        }

        @Test
        void shouldReturnBadRequestIfNotValid() throws Exception {
            AuthLoginRequest authLoginRequest = new AuthLoginRequest();

            mvc.perform(post("/api/v1/auth/login")
                            .content(mapper.writeValueAsString(authLoginRequest))
                            .characterEncoding(StandardCharsets.UTF_8)
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest());

            verify(authService, never()).login(any());
        }
    }

    @Nested
    class Register {
        @Test
        void shouldRegister() throws Exception {
            UserCreateRequest userCreateRequest = UserCreateRequest.builder()
                    .name(user.getName())
                    .username(user.getUsername())
                    .password(user.getPassword())
                    .passwordConfirmation(user.getPassword())
                    .build();

            UserResponse userResponse = UserResponse.builder()
                    .id(user.getId())
                    .name(user.getName())
                    .username(user.getUsername())
                    .build();

            when(userMapper.toUser((UserCreateRequest) any())).thenCallRealMethod();
            when(userService.create(any())).thenReturn(user);
            when(userMapper.toUserResponse(any())).thenCallRealMethod();

            mvc.perform(post("/api/v1/auth/register")
                            .content(mapper.writeValueAsString(userCreateRequest))
                            .characterEncoding(StandardCharsets.UTF_8)
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(content().json(mapper.writeValueAsString(userResponse)));

            verify(userMapper, times(1)).toUser((UserCreateRequest) any());
            verify(userService, times(1)).create(any());
            verify(userMapper, times(1)).toUserResponse(any());
        }

        @Test
        void shouldReturnBadRequestIfNotValid() throws Exception {
            UserCreateRequest userCreateRequest = new UserCreateRequest();

            mvc.perform(post("/api/v1/auth/register")
                            .content(mapper.writeValueAsString(userCreateRequest))
                            .characterEncoding(StandardCharsets.UTF_8)
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest());

            verify(userService, never()).create(any());
        }
    }

    @Nested
    class Refresh {
        @Test
        void shouldRefresh() throws Exception {
            AuthRefreshRequest authRefreshRequest = AuthRefreshRequest.builder()
                    .refreshToken("refreshToken")
                    .build();

            AuthResponse authResponse = AuthResponse.builder()
                    .username(user.getUsername())
                    .accessToken("newAccessToken")
                    .refreshToken("newRefreshToken")
                    .build();

            when(authService.refresh(any())).thenReturn(authResponse);

            mvc.perform(post("/api/v1/auth/refresh")
                            .content(mapper.writeValueAsString(authRefreshRequest))
                            .characterEncoding(StandardCharsets.UTF_8)
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(content().json(mapper.writeValueAsString(authResponse)));

            verify(authService, times(1)).refresh(any());
        }

        @Test
        void shouldReturnBadRequestIfNotValid() throws Exception {
            AuthRefreshRequest authRefreshRequest = new AuthRefreshRequest();

            mvc.perform(post("/api/v1/auth/refresh")
                            .content(mapper.writeValueAsString(authRefreshRequest))
                            .characterEncoding(StandardCharsets.UTF_8)
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest());

            verify(authService, never()).refresh(any());
        }
    }
}
