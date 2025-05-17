package ru.practicum.main.service.request.service;

import org.springframework.stereotype.Service;
import ru.practicum.main.service.request.dto.ParticipationRequestDto;
import ru.practicum.main.service.request.enums.RequestStatus;

import java.util.List;

@Service
public class RequestServiceImpl implements RequestService {
    @Override
    public List<ParticipationRequestDto> getParticipationRequests(Long userId) {
        // TODO
        return List.of();
    }

    @Override
    public ParticipationRequestDto createParticipationRequest(Long userId, Long eventId) {
        // TODO
        ParticipationRequestDto dto = new ParticipationRequestDto();
        dto.setId(userId);
        dto.setEvent(eventId);
        return dto;
    }

    @Override
    public ParticipationRequestDto updateParticipationRequest(Long userId, Long requestId) {
        // TODO
        ParticipationRequestDto dto = new ParticipationRequestDto();
        dto.setId(userId);
        dto.setEvent(requestId);
        dto.setStatus(RequestStatus.REJECTED);
        return dto;
    }
}
