package com.example.tasklist.model.dto;

import com.example.tasklist.model.validation.OnCreate;
import com.example.tasklist.model.validation.OnUpdate;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
public class UserDto {
    @NotNull(groups = OnUpdate.class,
            message = "Id must be not null")
    private Long id;

    @NotBlank(groups = {OnCreate.class, OnUpdate.class},
            message = "Name must be not null")
    @Length(max = 255,
            groups = {OnCreate.class, OnUpdate.class},
            message = "Name length must be less than 255 symbols")
    private String name;

    @NotBlank(groups = {OnCreate.class, OnUpdate.class},
            message = "Username must be not null")
    @Length(max = 255,
            groups = {OnCreate.class, OnUpdate.class},
            message = "Username length must be less than 255 symbols")
    private String username;

    @NotBlank(groups = {OnCreate.class, OnUpdate.class},
            message = "Password must be not null")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @NotBlank(groups = OnCreate.class,
            message = "Password must be not null")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String passwordConfirmation;
}
