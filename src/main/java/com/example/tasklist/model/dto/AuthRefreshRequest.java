package com.example.tasklist.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Schema(description = "Request for refresh jwt tokens")
public class AuthRefreshRequest {
    @Schema(description = "Refresh token",
            example = "eyJhbGciOiJIUzI1NiJ9.eyJzY29wZXMiOlsiUk9MRV9VU0VSIi"
                    + "wiUk9MRV9BRE1JTiJdLCJzdWIiOiJ3aWxsaWFtLmVtYXJkQGdtYWl"
                    + "sUlUuY29tIiwiaXNzIjoiaHR0cHM6Ly90YXNrbGlzdC5leGFtcGxl"
                    + "LmNvbSIsImlhdCI6MTcxNjk3NzkyOSwiZXhwIjoxNzE2OTc5NzI5f"
                    + "Q.hU3AXdTt0IHw4IG6h1HWhARgapTa5HjqVbwneSCA38U")
    @NotBlank(message = "Refresh token must be not blank")
    private String refreshToken;
}
