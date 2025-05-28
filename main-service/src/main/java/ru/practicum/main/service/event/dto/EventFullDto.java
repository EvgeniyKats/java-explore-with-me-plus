package ru.practicum.main.service.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.main.service.category.dto.CategoryDto;
import ru.practicum.main.service.comment.dto.GetCommentDto;
import ru.practicum.main.service.event.enums.EventState;
import ru.practicum.main.service.event.model.Location;
import ru.practicum.main.service.user.dto.UserShortDto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static ru.practicum.main.service.Constants.DATE_PATTERN;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EventFullDto implements ResponseEvent {
    private Long id;

    @NotBlank
    private String title;

    @NotNull
    private UserShortDto initiator;

    @NotNull
    private Location location;

    @NotNull
    private Boolean paid;

    @NotBlank
    private String annotation;

    @NotNull
    private CategoryDto category;

    private int confirmedRequests;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATE_PATTERN)
    private LocalDateTime createdOn;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATE_PATTERN)
    private LocalDateTime publishedOn;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATE_PATTERN)
    private LocalDateTime eventDate;

    private int participantLimit = 0;

    private String description;

    private Boolean requestModeration = true;

    private EventState state;

    private long views;

    private List<GetCommentDto> comments = new ArrayList<>();
}
