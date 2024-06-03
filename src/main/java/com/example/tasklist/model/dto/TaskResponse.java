package com.example.tasklist.model.dto;

import com.example.tasklist.model.enums.Status;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "Response task data")
public class TaskResponse {
    @Schema(description = "Task id",
            example = "1")
    private Long id;

    @Schema(description = "Task title",
            example = "Call cleaning")
    private String title;

    @Schema(description = "Task title",
            example = "Negotiate a service")
    private String description;

    @Schema(description = "Task status",
            example = "TO_DO")
    private Status status;

    @Schema(description = "Task expiration date",
            example = "2024-12-19 10:00:00")
    @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime expirationDate;
}
