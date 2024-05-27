package com.example.tasklist.security;

import com.example.tasklist.security.config.JwtTokenProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class JwtTokenService {
    private final JwtTokenProperties jwtTokenProperties;
    private SecretKey secretKey;

    @PostConstruct
    public void init() {
        this.secretKey = Keys.hmacShaKeyFor(jwtTokenProperties.getSecretKey()
                .getBytes());
    }

    public String issueToken(
            final String username,
            final List<String> scopes
    ) {
        return issueToken(username, Map.of("scopes", scopes));
    }

    public String issueToken(
            final String username,
            final Map<String, Object> claims
    ) {
        return Jwts.builder()
                .claims(claims)
                .subject(username)
                .issuer("https://tasklist.example.com")
                .issuedAt(Date.from(Instant.now()))
                .expiration(
                        Date.from(Instant.now()
                                .plus(
                                        jwtTokenProperties.getTokenExpiration()
                                                .toMillis(),
                                        ChronoUnit.MILLIS)
                        )
                )
                .signWith(secretKey)
                .compact();
    }

    public String issueRefreshToken(
            final String username,
            final List<String> scopes
    ) {
        return issueRefreshToken(username, Map.of("scopes", scopes));
    }

    public String issueRefreshToken(
            final String username,
            final Map<String, Object> claims
    ) {
        return Jwts.builder()
                .claims(claims)
                .subject(username)
                .issuer("https://tasklist.example.com")
                .issuedAt(Date.from(Instant.now()))
                .expiration(
                        Date.from(Instant.now()
                                .plus(
                                        jwtTokenProperties
                                                .getRefreshTokenExpiration()
                                                .toMillis(),
                                        ChronoUnit.MILLIS
                                )
                        )
                )
                .signWith(secretKey)
                .compact();
    }

    private Claims getClaims(final String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public String getSubject(final String token) {
        return getClaims(token).getSubject();
    }

    public boolean isTokenValid(
            final String token,
            final String username
    ) {
        String subject = getSubject(token);

        // Проверка Expiration токена происходит в методе parseSignedClaims()
        return subject.equals(username);
    }
}
