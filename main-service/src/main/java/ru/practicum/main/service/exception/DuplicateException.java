package ru.practicum.main.service.exception;

import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

public class DuplicateException extends RuntimeException {
    ApiError error;

    public DuplicateException(String message) {
        super(message);
        error = new ApiError(
                message,
                "For the requested operation the conditions are not met.",
                HttpStatus.CONFLICT,
                LocalDateTime.now());
    }
}
