package ru.practicum.main.service.exception;

import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

public class NotFoundException extends RuntimeException {
    ApiError error;
    public NotFoundException(String message) {
        super(message);
        error = new ApiError(
                message,
                "The required object was not found.",
                HttpStatus.NOT_FOUND,
                LocalDateTime.now());
    }
}
