package com.example.tasklist.controller;

import com.example.tasklist.exception.ApiError;
import com.example.tasklist.model.dto.AuthLoginRequest;
import com.example.tasklist.model.dto.AuthRefreshRequest;
import com.example.tasklist.model.dto.UserCreateRequest;
import com.example.tasklist.model.dto.AuthResponse;
import com.example.tasklist.model.dto.UserResponse;
import com.example.tasklist.model.entity.User;
import com.example.tasklist.model.mapper.UserMapper;
import com.example.tasklist.service.AuthService;
import com.example.tasklist.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Tag(name = "Auth Controller", description = "Auth API")
public class AuthController {
    private final AuthService authService;
    private final UserService userService;
    private final UserMapper userMapper;

    @PostMapping("/login")
    @Operation(summary = "Login with username and password")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "OK",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(
                                    implementation = AuthResponse.class))
                    }
            ),
            @ApiResponse(
                    responseCode = "Error codes",
                    description = "Error responses",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(
                                    implementation = ApiError.class))
                    }
            )
    })
    public AuthResponse login(
            @Valid @RequestBody final AuthLoginRequest authLoginRequest
    ) {
        return authService.login(authLoginRequest);
    }

    @PostMapping("/register")
    @Operation(summary = "Register new user")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "OK",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(
                                    implementation = UserResponse.class))
                    }
            ),
            @ApiResponse(
                    responseCode = "Error codes",
                    description = "Error responses",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(
                                    implementation = ApiError.class))
                    }
            )
    })
    public UserResponse register(
            @Valid @RequestBody final UserCreateRequest userCreateRequest
    ) {
        User user = userMapper.toUser(userCreateRequest);
        User createdUser = userService.create(user);

        return userMapper.toUserResponse(createdUser);
    }

    @PostMapping("/refresh")
    @Operation(summary = "Refresh jwt token")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "OK",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(
                                    implementation = AuthResponse.class))
                    }
            ),
            @ApiResponse(
                    responseCode = "Error codes",
                    description = "Error responses",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(
                                    implementation = ApiError.class))
                    }
            )
    })
    public AuthResponse refresh(
            @Valid @RequestBody final AuthRefreshRequest authRefreshRequest
    ) {
        return authService.refresh(authRefreshRequest);
    }
}
