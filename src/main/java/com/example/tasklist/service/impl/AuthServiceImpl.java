package com.example.tasklist.service.impl;

import com.example.tasklist.exception.IncorrectTokenException;
import com.example.tasklist.model.dto.JwtRequest;
import com.example.tasklist.model.dto.JwtResponse;
import com.example.tasklist.model.entity.User;
import com.example.tasklist.security.JwtTokenService;
import com.example.tasklist.service.AuthService;
import com.example.tasklist.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {
    private final AuthenticationManager authenticationManager;
    private final JwtTokenService jwtTokenService;
    private final UserService userService;

    @Override
    public JwtResponse login(final JwtRequest jwtRequest) {
        log.info("Попытка логина {}", jwtRequest);

        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(
                                jwtRequest.getUsername(),
                                jwtRequest.getPassword()
                        )
                );
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String accessToken = jwtTokenService.issueToken(
                userDetails.getUsername(),
                userDetails.getAuthorities()
                        .stream()
                        .map(GrantedAuthority::getAuthority)
                        .toList()
        );
        String refreshToken = jwtTokenService.issueRefreshToken(
                userDetails.getUsername(),
                userDetails.getAuthorities()
                        .stream()
                        .map(GrantedAuthority::getAuthority)
                        .toList()
        );

        return JwtResponse.builder()
                .username(userDetails.getUsername())
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    @Override
    public JwtResponse refresh(final String refreshToken) {
        log.info("Обновление токенов {}", refreshToken);

        JwtResponse jwtResponse;
        try {
            String username = jwtTokenService.getSubject(refreshToken);
            User user = userService.getByUsername(username);

            String newAccessToken = jwtTokenService.issueToken(
                    user.getUsername(),
                    user.getRoles()
                            .stream()
                            .map(Enum::toString)
                            .collect(Collectors.toList())
            );
            String newRefreshToken = jwtTokenService.issueRefreshToken(
                    user.getUsername(),
                    user.getRoles()
                            .stream()
                            .map(Enum::toString)
                            .collect(Collectors.toList())
            );

            jwtResponse = JwtResponse.builder()
                    .username(user.getUsername())
                    .accessToken(newAccessToken)
                    .refreshToken(newRefreshToken)
                    .build();
        } catch (Exception exception) {
            throw new IncorrectTokenException(exception.getMessage());
        }

        return jwtResponse;
    }
}
