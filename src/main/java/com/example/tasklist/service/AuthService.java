package com.example.tasklist.service;

import com.example.tasklist.model.dto.AuthLoginRequest;
import com.example.tasklist.model.dto.AuthResponse;
import com.example.tasklist.model.dto.AuthRefreshRequest;

public interface AuthService {
    AuthResponse login(AuthLoginRequest authLoginRequest);

    AuthResponse refresh(AuthRefreshRequest authRefreshRequest);
}
