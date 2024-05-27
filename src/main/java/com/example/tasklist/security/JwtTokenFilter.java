package com.example.tasklist.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtTokenFilter extends OncePerRequestFilter {
    private final JwtTokenService jwtTokenService;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(
            @NonNull final HttpServletRequest request,
            @NonNull final HttpServletResponse response,
            @NonNull final FilterChain filterChain
    ) throws ServletException, IOException {
        try {
            String bearerToken = getToken(request);
            String subject = jwtTokenService.getSubject(bearerToken);

            if (subject != null
                    && SecurityContextHolder.getContext()
                    .getAuthentication() == null
            ) {
                UserDetails userDetails =
                        userDetailsService.loadUserByUsername(subject);

                if (jwtTokenService.isTokenValid(bearerToken,
                        userDetails.getUsername())
                ) {
                    UsernamePasswordAuthenticationToken authenticationToken =
                            new UsernamePasswordAuthenticationToken(
                                    userDetails,
                                    null,
                                    userDetails.getAuthorities()
                            );

                    authenticationToken.setDetails(
                            new WebAuthenticationDetailsSource()
                                    .buildDetails(request)
                    );

                    SecurityContextHolder.getContext()
                            .setAuthentication(authenticationToken);
                }
            }
        } catch (Exception exception) {
            log.error("Cannot set user authentication {}",
                    exception.getMessage());
        }

        filterChain.doFilter(request, response);
    }

    private String getToken(final HttpServletRequest request) {
        String headerAuth = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (StringUtils.hasText(headerAuth)
                && headerAuth.startsWith("Bearer ")
        ) {
            return headerAuth.substring(7);
        }
        return null;
    }
}
