package com.example.tasklist.model.dto;

import com.example.tasklist.model.validation.OnCreate;
import com.example.tasklist.model.validation.OnUpdate;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
@Schema(description = "User DTO")
public class UserDto {
    @Schema(description = "User id",
            example = "1")
    @NotNull(groups = OnUpdate.class,
            message = "Id must be not null")
    private Long id;

    @Schema(description = "User name",
            example = "William Emard")
    @NotBlank(groups = {OnCreate.class, OnUpdate.class},
            message = "Name must be not blank")
    @Length(max = 255,
            groups = {OnCreate.class, OnUpdate.class},
            message = "Name length must be less than 255 symbols")
    private String name;

    @Schema(description = "User email",
            example = "william.emard@gmailRU.com")
    @NotBlank(groups = {OnCreate.class, OnUpdate.class},
            message = "Username must be not blank")
    @Length(max = 255,
            groups = {OnCreate.class, OnUpdate.class},
            message = "Username length must be less than 255 symbols")
    private String username;

    @Schema(description = "User password",
            example = "123456")
    @NotBlank(groups = {OnCreate.class, OnUpdate.class},
            message = "Password must be not blank")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @Schema(description = "User password confirmation",
            example = "123456")
    @NotBlank(groups = OnCreate.class,
            message = "Password must be not blank")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String passwordConfirmation;
}
