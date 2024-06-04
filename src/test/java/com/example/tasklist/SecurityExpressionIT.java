package com.example.tasklist;

import com.example.tasklist.exception.ApiError;
import com.example.tasklist.model.dto.AuthLoginRequest;
import com.example.tasklist.model.dto.AuthResponse;
import com.example.tasklist.model.dto.TaskCreateRequest;
import com.example.tasklist.model.dto.TaskResponse;
import com.example.tasklist.model.dto.UserCreateRequest;
import com.example.tasklist.model.dto.UserResponse;
import com.example.tasklist.model.enums.Status;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@AutoConfigureWebTestClient
public class SecurityExpressionIT {
    @Autowired
    WebTestClient webTestClient;

    final String LOGIN_URI = "/api/v1/auth/login";
    final String REGISTER_URI = "/api/v1/auth/register";
    final String USERS_URI = "/api/v1/users";
    final String TASKS_URI = "/api/v1/tasks";

    @Test
    void testAuthService() {
        // register new user1
        UserCreateRequest createRequestUser1 = UserCreateRequest.builder()
                .name("user1")
                .username("username" + UUID.randomUUID())
                .password("password")
                .passwordConfirmation("password")
                .build();

        EntityExchangeResult<UserResponse> registerUser1 = webTestClient.post()
                .uri(REGISTER_URI)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(createRequestUser1), UserCreateRequest.class)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(new ParameterizedTypeReference<UserResponse>() {
                })
                .returnResult();

        UserResponse registerResponseUser1 = Objects.requireNonNull(registerUser1.getResponseBody());

        // login by user1
        AuthLoginRequest loginRequestUser1 = AuthLoginRequest.builder()
                .username(createRequestUser1.getUsername())
                .password(createRequestUser1.getPassword())
                .build();

        EntityExchangeResult<AuthResponse> successLoginUser1 = webTestClient.post()
                .uri(LOGIN_URI)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(loginRequestUser1), AuthLoginRequest.class)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(new ParameterizedTypeReference<AuthResponse>() {
                })
                .returnResult();

        AuthResponse loginResponseUser1 = Objects.requireNonNull(successLoginUser1.getResponseBody());

        // create new task by user1
        TaskCreateRequest taskCreateRequestUser1 = TaskCreateRequest.builder()
                .title("title")
                .description("description")
                .expirationDate(LocalDateTime.of(2025, 9, 5, 10, 0, 0))
                .build();

        EntityExchangeResult<TaskResponse> createTaskUser1 = webTestClient.post()
                .uri(USERS_URI + "/" + registerResponseUser1.getId() + "/tasks")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .header(AUTHORIZATION, String.format("Bearer %s", loginResponseUser1.getAccessToken()))
                .body(Mono.just(taskCreateRequestUser1), TaskCreateRequest.class)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(new ParameterizedTypeReference<TaskResponse>() {
                })
                .returnResult();

        TaskResponse taskResponseUser1 = Objects.requireNonNull(createTaskUser1.getResponseBody());

        assertThat((taskResponseUser1.getId())).isNotNull();
        assertThat((taskResponseUser1.getTitle())).isEqualTo(taskCreateRequestUser1.getTitle());
        assertThat((taskResponseUser1.getDescription())).isEqualTo(taskCreateRequestUser1.getDescription());
        assertThat((taskResponseUser1.getStatus())).isEqualTo(Status.TODO);
        assertThat((taskResponseUser1.getExpirationDate())).isEqualTo(taskCreateRequestUser1.getExpirationDate());

        // register new user2
        UserCreateRequest createRequestUser2 = UserCreateRequest.builder()
                .name("user2")
                .username("username" + UUID.randomUUID())
                .password("password")
                .passwordConfirmation("password")
                .build();

        webTestClient.post()
                .uri(REGISTER_URI)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(createRequestUser2), UserCreateRequest.class)
                .exchange()
                .expectStatus()
                .isOk();

        // login by user2
        AuthLoginRequest loginRequestUser2 = AuthLoginRequest.builder()
                .username(createRequestUser2.getUsername())
                .password(createRequestUser2.getPassword())
                .build();

        EntityExchangeResult<AuthResponse> successLoginUser2 = webTestClient.post()
                .uri(LOGIN_URI)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(loginRequestUser2), AuthLoginRequest.class)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(new ParameterizedTypeReference<AuthResponse>() {
                })
                .returnResult();

        AuthResponse loginResponseUser2 = Objects.requireNonNull(successLoginUser2.getResponseBody());


        // try get user1 by user2 without ADMIN_ROLE
        EntityExchangeResult<ApiError> failedGetUserById = webTestClient.get()
                .uri(USERS_URI + "/" + registerResponseUser1.getId())
                .header(AUTHORIZATION, String.format("Bearer %s", loginResponseUser2.getAccessToken()))
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isForbidden()
                .expectBody(new ParameterizedTypeReference<ApiError>() {
                })
                .returnResult();

        ApiError failedResponseGetUserById = Objects.requireNonNull(failedGetUserById.getResponseBody());

        assertThat(failedResponseGetUserById.message()).isEqualTo("Access Denied");

        // try get user1 task by user2 without ADMIN_ROLE
        EntityExchangeResult<ApiError> failedGetTaskById = webTestClient.get()
                .uri(TASKS_URI + "/" + taskResponseUser1.getId())
                .header(AUTHORIZATION, String.format("Bearer %s", loginResponseUser2.getAccessToken()))
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isForbidden()
                .expectBody(new ParameterizedTypeReference<ApiError>() {
                })
                .returnResult();

        ApiError failedResponseGetTaskById = Objects.requireNonNull(failedGetTaskById.getResponseBody());

        assertThat(failedResponseGetTaskById.message()).isEqualTo("Access Denied");
    }
}