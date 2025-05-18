package ru.practicum.main.service.event.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.main.service.Constants;
import ru.practicum.main.service.event.EventRepository;
import ru.practicum.main.service.event.LocationRepository;
import ru.practicum.main.service.event.MapperEvent;
import ru.practicum.main.service.event.dto.*;
import ru.practicum.main.service.event.enums.EventState;
import ru.practicum.main.service.event.model.Event;
import ru.practicum.main.service.exception.ConflictException;
import ru.practicum.main.service.exception.NotFoundException;
import ru.practicum.main.service.request.MapperRequest;
import ru.practicum.main.service.request.RequestRepository;
import ru.practicum.main.service.request.dto.ParticipationRequestDto;
import ru.practicum.main.service.request.enums.RequestStatus;
import ru.practicum.main.service.request.model.Request;
import ru.practicum.main.service.user.UserRepository;
import ru.practicum.main.service.user.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PrivateEventServiceImpl implements PrivateEventService {

    private final EventRepository eventRepository;
    private final MapperEvent eventMapper;
    private final RequestRepository requestRepository;
    private final MapperRequest requestMapper;
    private final UserRepository userRepository;
    private final LocationRepository locationRepository;

    @Override
    public List<EventFullDto> getAllUsersEvents(Long userId, Integer from, Integer size) {
        Pageable pageable = PageRequest.of(from, size);
        Page<Event> events = eventRepository.getUserEvents(userId, pageable);
        return events.map(eventMapper::toEventFullDto).getContent();
    }

    @Override
    @Transactional
    public EventFullDto addNewEvent(Long userId, NewEventDto eventDto) {
        if (eventDto.getEventDate().minusHours(1).minusMinutes(59).isBefore(LocalDateTime.now())) {
            throw new ConflictException("Дата и время на которые намечено событие не может быть раньше, чем через два часа от текущего момента");
        }
        User initiator = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(Constants.USER_NOT_FOUND));
        Event newEvent = eventMapper.toEvent(eventDto);
        newEvent.setInitiator(initiator);
        newEvent.setState(EventState.PENDING);
        newEvent = eventRepository.save(newEvent);
        return eventMapper.toEventFullDto(newEvent);
    }

    @Override
    public EventFullDto getEventById(Long userId, Long eventId) {
        Event event = eventRepository.findByIdAndInitiatorId(eventId, userId)
                .orElseThrow(() -> new NotFoundException(Constants.EVENT_NOT_FOUND));
        return eventMapper.toEventFullDto(event);
    }

    @Override
    @Transactional
    public EventFullDto updateEvent(Long userId, Long eventId, UpdateEventUserRequest updateEventDto) {
        EventFullDto eventFromDb = getEventById(userId, eventId);
        if (eventFromDb.getState() != EventState.CANCELED && eventFromDb.getState() != EventState.REJECTED && eventFromDb.getState() != EventState.PENDING) {
            throw new ConflictException("Изменить можно только отмененные события или события в состоянии ожидания модерации");
        }
        if (updateEventDto.getEventDate().minusHours(1).minusMinutes(59).isBefore(LocalDateTime.now())) {
            throw new ConflictException("Дата и время на которые намечено событие не может быть раньше, чем через два часа от текущего момента");
        }
        Event updatedEvent = eventRepository.save(eventMapper.toEvent(updateEventDto));
        return eventMapper.toEventFullDto(updatedEvent);
    }

    @Override
    public List<ParticipationRequestDto> getEventRequests(Long userId, Long eventId) {
        List<Request> requests = requestRepository.findAllByEventId(eventId);
        return requests.stream().map(requestMapper::toParticipationRequestDto).toList();
    }

    @Override
    @Transactional
    public EventRequestStatusUpdateResult updateEventRequests(Long userId, Long eventId, EventRequestStatusUpdateRequest updateRequest) {
        EventRequestStatusUpdateResult result = new EventRequestStatusUpdateResult();
        List<ParticipationRequestDto> confirmed = new ArrayList<>();
        List<ParticipationRequestDto> rejected = new ArrayList<>();
        EventFullDto eventFromDb = getEventById(userId, eventId);
        if (Objects.equals(eventFromDb.getConfirmedRequests(), Long.valueOf(eventFromDb.getParticipantLimit()))) {
            throw new ConflictException("Достигнут лимит заявок на событие");
        }
        List<Request> requests = requestRepository.findAllById(updateRequest.getRequestIds());
        if (!requests.stream().filter(r -> r.getStatus() != RequestStatus.PENDING).toList().isEmpty()) {
            throw new ConflictException("Статус можно изменить только у заявок, находящихся в состоянии ожидания");
        }
        if ((eventFromDb.getParticipantLimit() == 0 || !eventFromDb.getRequestModeration()) && updateRequest.getStatus() == EventRequestStatusUpdateRequest.Status.CONFIRMED) {
            throw new ConflictException("Лимит участников данного события равен 0, либо отключена премодерация заявок. Подтверждение заявок не требуется");
        }
        for (int i = 0; i < updateRequest.getRequestIds().size(); i++) {
            Request currentRequest = requests.get(i);
            if (!eventFromDb.getConfirmedRequests().equals(Long.valueOf(eventFromDb.getParticipantLimit()))) {
                confirmed.add(requestMapper.toParticipationRequestDto(currentRequest));
                currentRequest.setStatus(requestMapper.statusFromUpdateRequestStatus(updateRequest.getStatus()));
                requestRepository.save(currentRequest);
            } else {
                rejected.add(requestMapper.toParticipationRequestDto(currentRequest));
                currentRequest.setStatus(RequestStatus.REJECTED);
                requestRepository.save(currentRequest);
            }
        }
        result.setConfirmedRequests(confirmed);
        result.setRejectedRequests(rejected);
        return result;
    }
}
