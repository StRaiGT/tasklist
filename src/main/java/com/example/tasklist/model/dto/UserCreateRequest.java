package com.example.tasklist.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
@Schema(description = "Request for register new user")
public class UserCreateRequest {
    @Schema(description = "User name",
            example = "William Emard")
    @NotBlank(message = "Name must be not blank")
    @Length(max = 255,
            message = "Name length must be less than 255 symbols")
    private String name;

    @Schema(description = "User email",
            example = "william.emard@gmailRU.com")
    @NotBlank(message = "Username must be not blank")
    @Length(max = 255,
            message = "Username length must be less than 255 symbols")
    private String username;

    @Schema(description = "User password",
            example = "123456")
    @NotBlank(message = "Password must be not blank")
    private String password;

    @Schema(description = "User password confirmation",
            example = "123456")
    @NotBlank(message = "Password must be not blank")
    private String passwordConfirmation;
}
