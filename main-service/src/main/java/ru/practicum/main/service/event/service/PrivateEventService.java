package ru.practicum.main.service.event.service;

import ru.practicum.main.service.event.dto.*;
import ru.practicum.main.service.request.dto.ParticipationRequestDto;

import java.util.List;

public interface PrivateEventService {

    List<EventFullDto> getAllUsersEvents(Long userId, Integer from, Integer size);

    EventFullDto addNewEvent(Long userId, NewEventDto eventDto);

    EventFullDto getEventById(Long userId, Long eventId);

    EventFullDto updateEvent(Long userId, Long eventId, UpdateEventUserRequest updateEventDto);

    List<ParticipationRequestDto> getEventRequests(Long userId, Long eventId);

    EventRequestStatusUpdateResult updateEventRequests(Long userId, Long eventId, EventRequestStatusUpdateRequest updateRequest);

}
