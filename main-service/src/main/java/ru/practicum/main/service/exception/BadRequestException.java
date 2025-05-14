package ru.practicum.main.service.exception;

import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

public class BadRequestException extends RuntimeException {
    ApiError error;
    public BadRequestException(String message) {
        super(message);
        error = new ApiError(
                message,
                "Incorrectly made request.",
                HttpStatus.BAD_REQUEST,
                LocalDateTime.now());
    }
}
