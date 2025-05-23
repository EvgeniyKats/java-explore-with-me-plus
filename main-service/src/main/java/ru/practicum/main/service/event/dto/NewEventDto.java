package ru.practicum.main.service.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.Setter;
import ru.practicum.main.service.event.enums.EventState;
import ru.practicum.main.service.event.model.Location;
import ru.practicum.main.service.validator.SizeAfterTrim;

import java.time.LocalDateTime;

import static ru.practicum.main.service.Constants.DATE_PATTERN;

@Getter
@Setter
public class NewEventDto {

    @NotBlank
    @SizeAfterTrim(min = 20, max = 2000)
    private String annotation;

    @NotNull
    private Long category;

    @NotBlank
    @SizeAfterTrim(min = 20, max = 7000)
    private String description;

    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATE_PATTERN)
    private LocalDateTime eventDate;

    @NotNull
    private Location location;

    private Boolean paid = false;

    @PositiveOrZero
    private Integer participantLimit = 0;

    private Boolean requestModeration = true;

    @NotBlank
    @SizeAfterTrim(min = 3, max = 120)
    private String title;

    @JsonIgnore
    LocalDateTime createdOn = LocalDateTime.now();

    @JsonIgnore
    EventState state = EventState.PENDING;
}
