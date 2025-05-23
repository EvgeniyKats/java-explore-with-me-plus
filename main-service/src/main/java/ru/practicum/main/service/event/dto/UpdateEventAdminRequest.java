package ru.practicum.main.service.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.Setter;
import ru.practicum.main.service.event.model.Location;
import ru.practicum.main.service.validator.SizeAfterTrim;

import java.time.LocalDateTime;

import static ru.practicum.main.service.Constants.DATE_PATTERN;

@Getter
@Setter
public class UpdateEventAdminRequest {

    @SizeAfterTrim(min = 20, max = 2000)
    private String annotation;

    private Long category;

    @SizeAfterTrim(min = 20, max = 7000)
    private String description;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATE_PATTERN)
    private LocalDateTime eventDate;

    private Location location;

    private Boolean paid;

    @PositiveOrZero
    private Integer participantLimit;

    private Boolean requestModeration;

    private StateAction stateAction;

    @SizeAfterTrim(min = 3, max = 120)
    private String title;

    public enum StateAction {
        PUBLISH_EVENT, REJECT_EVENT
    }

    public boolean hasEventDate() {
        return eventDate != null;
    }

    public boolean hasStateAction() {
        return stateAction != null;
    }
}
