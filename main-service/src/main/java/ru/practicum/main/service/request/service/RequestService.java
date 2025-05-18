package ru.practicum.main.service.request.service;

import org.mapstruct.Named;
import ru.practicum.main.service.event.dto.EventRequestStatusUpdateRequest;
import ru.practicum.main.service.request.dto.ParticipationRequestDto;
import ru.practicum.main.service.request.enums.RequestStatus;

import java.util.List;

public interface RequestService {
    List<ParticipationRequestDto> getParticipationRequests(Long userId);

    ParticipationRequestDto createParticipationRequest(Long userId, Long eventId);

    ParticipationRequestDto cancelParticipationRequest(Long userId, Long requestId);
}
