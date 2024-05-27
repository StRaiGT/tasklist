package com.example.tasklist.controller;

import com.example.tasklist.model.dto.JwtRequest;
import com.example.tasklist.model.dto.JwtResponse;
import com.example.tasklist.model.dto.UserDto;
import com.example.tasklist.model.entity.User;
import com.example.tasklist.model.mapper.UserMapper;
import com.example.tasklist.model.validation.OnCreate;
import com.example.tasklist.service.AuthService;
import com.example.tasklist.service.UserService;
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
public class AuthController {
    private final AuthService authService;
    private final UserService userService;
    private final UserMapper userMapper;

    @PostMapping("/login")
    public JwtResponse login(
            @Valid @RequestBody final JwtRequest jwtRequest
    ) {
        return authService.login(jwtRequest);
    }

    @PostMapping("/register")
    public UserDto register(
            @Validated(OnCreate.class) @RequestBody final UserDto userDto
    ) {
        User user = userMapper.toEntity(userDto);
        User createdUser = userService.create(user);

        return userMapper.toDto(createdUser);
    }

    @PostMapping("/refresh")
    public JwtResponse refresh(
            @NotBlank @RequestBody final String refreshToken
    ) {
        return authService.refresh(refreshToken);
    }
}
