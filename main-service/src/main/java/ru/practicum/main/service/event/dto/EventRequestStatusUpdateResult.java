package ru.practicum.main.service.event.dto;

import lombok.Getter;
import lombok.Setter;
import ru.practicum.main.service.request.dto.ParticipationRequestDto;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class EventRequestStatusUpdateResult {
    private List<ParticipationRequestDto> confirmedRequests = new ArrayList<>();

    private List<ParticipationRequestDto> rejectedRequests = new ArrayList<>();
}
