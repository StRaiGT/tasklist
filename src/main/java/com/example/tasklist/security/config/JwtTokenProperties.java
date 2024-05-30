package com.example.tasklist.security.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
@Getter
@Setter
@ConfigurationProperties(prefix = "spring.security.jwt")
public class JwtTokenProperties {
    private String secretKey;
    private Duration tokenExpiration;
    private Duration refreshTokenExpiration;
}
