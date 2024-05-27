package com.example.tasklist.exception;

public class IncorrectTokenException extends RuntimeException {
    public IncorrectTokenException(final String message) {
        super(message);
    }
}
