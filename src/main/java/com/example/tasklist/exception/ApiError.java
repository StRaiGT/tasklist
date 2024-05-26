package com.example.tasklist.exception;

import java.time.LocalDateTime;

public record ApiError(
        String status,
        String reason,
        String message,
        String errors,
        LocalDateTime timestamp
) {

}
