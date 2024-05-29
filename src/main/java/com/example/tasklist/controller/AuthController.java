package com.example.tasklist.controller;

import com.example.tasklist.exception.ApiError;
import com.example.tasklist.model.dto.JwtRequest;
import com.example.tasklist.model.dto.JwtResponse;
import com.example.tasklist.model.dto.UserDto;
import com.example.tasklist.model.entity.User;
import com.example.tasklist.model.mapper.UserMapper;
import com.example.tasklist.model.validation.OnCreate;
import com.example.tasklist.service.AuthService;
import com.example.tasklist.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Validated
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
                                    implementation = JwtResponse.class))
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
    public JwtResponse login(
            @Valid @RequestBody final JwtRequest jwtRequest
    ) {
        return authService.login(jwtRequest);
    }

    @PostMapping("/register")
    @Operation(summary = "Register new user")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "OK",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(
                                    implementation = UserDto.class))
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
    public UserDto register(
            @Validated(OnCreate.class) @RequestBody final UserDto userDto
    ) {
        User user = userMapper.toEntity(userDto);
        User createdUser = userService.create(user);

        return userMapper.toDto(createdUser);
    }

    @PostMapping("/refresh")
    @Operation(summary = "Refresh jwt token")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "OK",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(
                                    implementation = JwtResponse.class))
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
    public JwtResponse refresh(
            @NotBlank @RequestBody final String refreshToken
    ) {
        return authService.refresh(refreshToken);
    }
}
