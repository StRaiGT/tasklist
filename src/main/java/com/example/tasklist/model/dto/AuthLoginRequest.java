package com.example.tasklist.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "Request for login")
public class AuthLoginRequest {
    @Schema(description = "User email",
            example = "william.emard@gmailRU.com")
    @NotBlank(message = "Username must be not blank")
    private String username;

    @Schema(description = "User password",
            example = "123456")
    @NotBlank(message = "Password must be not blank")
    private String password;
}
