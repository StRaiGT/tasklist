package com.example.tasklist.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Response after login")
public class JwtResponse {
    @Schema(description = "User email",
            example = "william.emard@gmailRU.com")
    private String username;

    @Schema(description = "Access token",
            example = "eyJhbGciOiJIUzI1NiJ9.eyJzY29wZXMiOlsiUk9MRV9VU0VSIi"
                    + "wiUk9MRV9BRE1JTiJdLCJzdWIiOiJ3aWxsaWFtLmVtYXJkQGdtYWl"
                    + "sUlUuY29tIiwiaXNzIjoiaHR0cHM6Ly90YXNrbGlzdC5leGFtcGxl"
                    + "LmNvbSIsImlhdCI6MTcxNjk3NzkyOSwiZXhwIjoxNzE2OTc4MjI5f"
                    + "Q.l9ppHnFa4pTx99Q8mNkN6Bg4h0-Kf7E1pg7jQFlqaSc")
    private String accessToken;

    @Schema(description = "Refresh token",
            example = "eyJhbGciOiJIUzI1NiJ9.eyJzY29wZXMiOlsiUk9MRV9VU0VSIi"
                    + "wiUk9MRV9BRE1JTiJdLCJzdWIiOiJ3aWxsaWFtLmVtYXJkQGdtYWl"
                    + "sUlUuY29tIiwiaXNzIjoiaHR0cHM6Ly90YXNrbGlzdC5leGFtcGxl"
                    + "LmNvbSIsImlhdCI6MTcxNjk3NzkyOSwiZXhwIjoxNzE2OTc5NzI5f"
                    + "Q.hU3AXdTt0IHw4IG6h1HWhARgapTa5HjqVbwneSCA38U")
    private String refreshToken;
}
