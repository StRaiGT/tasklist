package com.example.tasklist.service;

import com.example.tasklist.model.dto.JwtRequest;
import com.example.tasklist.model.dto.JwtResponse;

public interface AuthService {
    JwtResponse login(JwtRequest loginRequest);

    JwtResponse refresh(String refreshToken);
}
