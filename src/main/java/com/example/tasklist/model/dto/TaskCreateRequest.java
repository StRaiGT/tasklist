package com.example.tasklist.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Getter
@Setter
@Schema(description = "Request for create new task")
public class TaskCreateRequest {
    @Schema(description = "Task title",
            example = "Call cleaning")
    @NotBlank(message = "Title must be not blank")
    @Length(max = 255,
            message = "Title length must be less than 255 symbols")
    private String title;

    @Schema(description = "Task title",
            example = "Negotiate a service")
    @Length(max = 255,
            message = "Title length must be less than 255 symbols")
    private String description;

    @Schema(description = "Task expiration date",
            example = "2024-12-19 10:00:00")
    @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime expirationDate;
}
