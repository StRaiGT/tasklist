package com.example.tasklist;

import com.example.tasklist.exception.ApiError;
import com.example.tasklist.model.dto.AuthLoginRequest;
import com.example.tasklist.model.dto.AuthRefreshRequest;
import com.example.tasklist.model.dto.AuthResponse;
import com.example.tasklist.model.dto.UserCreateRequest;
import com.example.tasklist.model.dto.UserResponse;
import com.example.tasklist.security.JwtTokenService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.Objects;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@AutoConfigureWebTestClient
public class AuthenticationIT {
    @Autowired
    WebTestClient webTestClient;

    @Autowired
    JwtTokenService jwtTokenService;

    final String LOGIN_URI = "/api/v1/auth/login";
    final String REGISTER_URI = "/api/v1/auth/register";
    final String REFRESH_URI = "/api/v1/auth/refresh";

    @Test
    void testAuthService() {
        UserCreateRequest userCreateRequest = UserCreateRequest.builder()
                .name("name")
                .username("username" + UUID.randomUUID())
                .password("password")
                .passwordConfirmation("password")
                .build();

        AuthLoginRequest authLoginRequest = AuthLoginRequest.builder()
                .username(userCreateRequest.getUsername())
                .password(userCreateRequest.getPassword())
                .build();

        // check that username not existed
        EntityExchangeResult<ApiError> failedLogin = webTestClient.post()
                .uri(LOGIN_URI)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(authLoginRequest), AuthLoginRequest.class)
                .exchange()
                .expectBody(new ParameterizedTypeReference<ApiError>() {
                })
                .returnResult();

        ApiError response = Objects.requireNonNull(failedLogin.getResponseBody());

        assertThat(response.message()).isEqualTo("Пользователя с таким id не существует");

        // register new user
        EntityExchangeResult<UserResponse> register = webTestClient.post()
                .uri(REGISTER_URI)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(userCreateRequest), UserCreateRequest.class)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(new ParameterizedTypeReference<UserResponse>() {
                })
                .returnResult();

        UserResponse registerResponse = Objects.requireNonNull(register.getResponseBody());

        assertThat((registerResponse.getId())).isNotNull();
        assertThat((registerResponse.getName())).isEqualTo(userCreateRequest.getName());
        assertThat((registerResponse.getUsername())).isEqualTo(userCreateRequest.getUsername());

        // login by created user
        EntityExchangeResult<AuthResponse> successLogin = webTestClient.post()
                .uri(LOGIN_URI)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(authLoginRequest), AuthLoginRequest.class)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(new ParameterizedTypeReference<AuthResponse>() {
                })
                .returnResult();

        AuthResponse loginResponse = Objects.requireNonNull(successLogin.getResponseBody());

        final String loginUsername = loginResponse.getUsername();
        final String loginAccessToken = loginResponse.getAccessToken();
        final String loginRefreshToken = loginResponse.getRefreshToken();

        assertThat(loginUsername).isEqualTo(authLoginRequest.getUsername());
        assertThat(jwtTokenService.isTokenValid(loginAccessToken, loginUsername)).isTrue();
        assertThat(jwtTokenService.isTokenValid(loginRefreshToken, loginUsername)).isTrue();

        // refresh jwt by created user
        AuthRefreshRequest authRefreshRequest = AuthRefreshRequest.builder()
                .refreshToken(loginRefreshToken)
                .build();

        EntityExchangeResult<AuthResponse> refresh = webTestClient.post()
                .uri(REFRESH_URI)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(authRefreshRequest), AuthRefreshRequest.class)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(new ParameterizedTypeReference<AuthResponse>() {
                })
                .returnResult();

        AuthResponse refreshResponse = Objects.requireNonNull(refresh.getResponseBody());

        final String refreshUsername = refreshResponse.getUsername();
        final String refreshAccessToken = refreshResponse.getAccessToken();
        final String refreshRefreshToken = refreshResponse.getRefreshToken();

        assertThat(refreshUsername).isEqualTo(authLoginRequest.getUsername());
        assertThat(jwtTokenService.isTokenValid(refreshAccessToken, refreshUsername)).isTrue();
        assertThat(jwtTokenService.isTokenValid(refreshRefreshToken, refreshUsername)).isTrue();
    }
}