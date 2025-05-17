package ru.practicum.main.service.request.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import ru.practicum.main.service.request.enums.RequestStatus;

import java.time.LocalDateTime;

@Getter
@Setter
public class ParticipationRequestDto {
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private LocalDateTime created;
    private Long event;
    private Long id;
    private Long requester;
    private RequestStatus status;
}
