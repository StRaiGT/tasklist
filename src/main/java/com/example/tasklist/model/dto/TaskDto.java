package com.example.tasklist.model.dto;

import com.example.tasklist.model.enums.Status;
import com.example.tasklist.model.validation.OnCreate;
import com.example.tasklist.model.validation.OnUpdate;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Getter
@Setter
public class TaskDto {
    @NotNull(groups = OnUpdate.class,
            message = "Id must be not null")
    private Long id;

    @NotBlank(groups = {OnCreate.class, OnUpdate.class},
            message = "Title must be not blank")
    @Length(max = 255,
            groups = {OnCreate.class, OnUpdate.class},
            message = "Title length must be less than 255 symbols")
    private String title;

    @Length(max = 255,
            groups = {OnCreate.class, OnUpdate.class},
            message = "Title length must be less than 255 symbols")
    private String description;

    private Status status;

    @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime expirationDate;
}
