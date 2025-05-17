package ru.practicum.main.service.event.dto;

import lombok.Getter;
import lombok.Setter;
import ru.practicum.main.service.request.dto.ParticipationRequestDto;

import java.util.List;

@Getter
@Setter
public class EventRequestStatusUpdateResult {
    private List<ParticipationRequestDto> confirmedRequests;

    private List<ParticipationRequestDto> rejectedRequests;
}
