package ru.practicum.main.service.event.service;

import ru.practicum.main.service.event.dto.*;
import ru.practicum.main.service.event.enums.EventSortType;
import ru.practicum.main.service.event.enums.EventState;
import ru.practicum.main.service.request.dto.ParticipationRequestDto;

import java.time.LocalDateTime;
import java.util.List;

public interface EventService {

    List<EventFullDto> getEventsByAdmin(List<Long> users, List<EventState> states,
                                        List<Long> categories, LocalDateTime rangeStart,
                                        LocalDateTime rangeEnd, Integer from, Integer size);

    EventFullDto updateEventByAdmin(Long eventId, UpdateEventAdminRequest eventDto);

    List<EventShortDto> getAllUsersEvents(Long userId, Integer from, Integer size);

    EventFullDto addNewEvent(Long userId, NewEventDto eventDto);

    EventFullDto getEventsByUserIdByEventId(Long userId, Long eventId);

    EventFullDto updateEventByUser(Long userId, Long eventId, UpdateEventUserRequest updateEventDto);

    List<ParticipationRequestDto> getEventRequests(Long userId, Long eventId);

    EventRequestStatusUpdateResult updateEventRequests(Long userId, Long eventId, EventRequestStatusUpdateRequest updateRequest);

    List<EventShortDto> getEventsByUser(String text, List<Long> categories, Boolean paid, LocalDateTime rangeStart, LocalDateTime rangeEnd, Boolean onlyAvailable, EventSortType sort, Integer from, Integer size);

    EventFullDto getEventById(Long eventId);
}
