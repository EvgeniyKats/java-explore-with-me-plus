package ru.practicum.main.service.exception;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ForbiddenException extends RuntimeException {
    ApiError error;
    public ForbiddenException(String message) {
        super(message);
        error = new ApiError(
                message,
                "Restriction of access for the client to the specified resource.",
                HttpStatus.CONFLICT,
                LocalDateTime.now());
    }
}
