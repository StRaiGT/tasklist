package com.example.tasklist.exception;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema(description = "Pattern for error")
public record ApiError(
        @Schema(example = "Error status")
        String status,

        @Schema(example = "Error reason")
        String reason,

        @Schema(example = "Error message")
        String message,

        @Schema(example = "Errors stacktrace")
        String errors,

        @Schema(example = "Error timestamp")
        LocalDateTime timestamp
) {

}
