package ru.practicum.main.service.event.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.main.service.Constants;
import ru.practicum.main.service.category.model.Category;
import ru.practicum.main.service.category.repository.CategoryRepository;
import ru.practicum.main.service.event.EventRepository;
import ru.practicum.main.service.event.LocationRepository;
import ru.practicum.main.service.event.MapperEvent;
import ru.practicum.main.service.event.dto.*;
import ru.practicum.main.service.event.enums.EventSortType;
import ru.practicum.main.service.event.enums.EventState;
import ru.practicum.main.service.event.model.Event;
import ru.practicum.main.service.event.util.ResponseEventBuilder;
import ru.practicum.main.service.exception.ConflictException;
import ru.practicum.main.service.exception.NotFoundException;
import ru.practicum.main.service.request.MapperRequest;
import ru.practicum.main.service.request.RequestRepository;
import ru.practicum.main.service.request.dto.ParticipationRequestDto;
import ru.practicum.main.service.request.model.Request;
import ru.practicum.main.service.user.UserRepository;
import ru.practicum.main.service.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

import static ru.practicum.main.service.Constants.CATEGORY_NOT_FOUND;
import static ru.practicum.main.service.Constants.EVENT_NOT_FOUND;
import static ru.practicum.main.service.event.dto.UpdateEventUserRequest.StateAction.SEND_TO_REVIEW;
import static ru.practicum.main.service.event.enums.EventState.CANCELED;
import static ru.practicum.main.service.event.enums.EventState.PENDING;
import static ru.practicum.main.service.event.util.ValidatorEventTime.isEventTimeBad;
import static ru.practicum.main.service.event.dto.UpdateEventAdminRequest.StateAction.PUBLISH_EVENT;
import static ru.practicum.main.service.event.enums.EventState.PUBLISHED;
import static ru.practicum.main.service.event.enums.EventState.REJECTED;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EventServiceImpl implements EventService {
    private final EventRepository eventRepository;
    private final MapperEvent eventMapper;
    private final RequestRepository requestRepository;
    private final MapperRequest requestMapper;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final LocationRepository locationRepository;
    private final ResponseEventBuilder responseEventBuilder;

    @Override
    public List<EventShortDto> getAllUsersEvents(Long userId, Integer from, Integer size) {
        Pageable pageable = PageRequest.of(from, size);
        List<Event> events = eventRepository.getUserEvents(userId, pageable);

        return responseEventBuilder.buildManyEventResponseDto(events, EventShortDto.class);
    }

    @Override
    @Transactional
    public EventFullDto addNewEvent(Long userId, NewEventDto eventDto) {
        Event event = eventMapper.toEvent(eventDto);

        if (eventDto.getEventDate().minusHours(1).minusMinutes(59).isBefore(LocalDateTime.now())) {
            throw new ConflictException("Дата и время на которые намечено событие не может быть раньше, чем через два часа от текущего момента");
        }

        Category category = categoryRepository.findById(eventDto.getCategory()).orElseThrow(
                () -> new NotFoundException(CATEGORY_NOT_FOUND));
        event.setCategory(category);

        User initiator = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(Constants.USER_NOT_FOUND));
        event.setInitiator(initiator);

        event.getLocation().setEvent(event);
        locationRepository.save(event.getLocation());

        event = eventRepository.save(event);
        return responseEventBuilder.buildOneEventResponseDto(event, EventFullDto.class);
    }

    @Override
    public EventFullDto getEventsByUserIdByEventId(Long userId, Long eventId) {
        Event event = eventRepository.findByIdAndInitiatorId(eventId, userId)
                .orElseThrow(() -> new NotFoundException(EVENT_NOT_FOUND));
        return responseEventBuilder.buildOneEventResponseDto(event, EventFullDto.class);
    }

    @Override
    @Transactional
    public EventFullDto updateEventByUser(Long userId, Long eventId, UpdateEventUserRequest updateDto) {
        Event event = eventRepository.findByIdAndInitiatorId(eventId, userId)
                .orElseThrow(() -> new NotFoundException(EVENT_NOT_FOUND));

        if (updateDto.hasStateAction()) {
            EventState state = event.getState();
            if (state != PENDING && state != CANCELED) {
                throw new ConflictException("Изменить можно только отмененные события или события в состоянии ожидания модерации");
            }

            if (updateDto.getStateAction().equals(SEND_TO_REVIEW)) {
                event.setState(PENDING);
            } else {
                event.setState(CANCELED);
            }
        }

        if (updateDto.hasEventDate()) {
            if (isEventTimeBad(updateDto.getEventDate(), 2)) {
                throw new ConflictException("Дата начала изменяемого события должна быть не ранее чем за 2 часа от даты публикации");
            }
            event.setEventDate(updateDto.getEventDate());
        }

        UpdateEventParam param = eventMapper.toUpdateParam(updateDto);
        updateEvent(event, param);

        return responseEventBuilder.buildOneEventResponseDto(event, EventFullDto.class);
    }

    @Override
    public List<ParticipationRequestDto> getEventRequests(Long userId, Long eventId) {
        List<Request> requests = requestRepository.findAllByEventId(eventId);
        return requests.stream().map(requestMapper::toParticipationRequestDto).toList();
    }

    @Override
    @Transactional
    public EventRequestStatusUpdateResult updateEventRequests(Long userId, Long eventId, EventRequestStatusUpdateRequest updateRequest) {
//        EventRequestStatusUpdateResult result = new EventRequestStatusUpdateResult();
//        List<ParticipationRequestDto> confirmed = new ArrayList<>();
//        List<ParticipationRequestDto> rejected = new ArrayList<>();
//        EventFullDto eventFromDb = getEventsByUserId(userId, eventId);
//        if (Objects.equals(eventFromDb.getConfirmedRequests(), Long.valueOf(eventFromDb.getParticipantLimit()))) {
//            throw new ConflictException("Достигнут лимит заявок на событие");
//        }
//        List<Request> requests = requestRepository.findAllById(updateRequest.getRequestIds());
//        if (!requests.stream().filter(r -> r.getStatus() != RequestStatus.PENDING).toList().isEmpty()) {
//            throw new ConflictException("Статус можно изменить только у заявок, находящихся в состоянии ожидания");
//        }
//        if ((eventFromDb.getParticipantLimit() == 0 || !eventFromDb.getRequestModeration()) && updateRequest.getStatus() == EventRequestStatusUpdateRequest.Status.CONFIRMED) {
//            throw new ConflictException("Лимит участников данного события равен 0, либо отключена премодерация заявок. Подтверждение заявок не требуется");
//        }
//        for (int i = 0; i < updateRequest.getRequestIds().size(); i++) {
//            Request currentRequest = requests.get(i);
//            if (!eventFromDb.getConfirmedRequests().equals(Long.valueOf(eventFromDb.getParticipantLimit()))) {
//                confirmed.add(requestMapper.toParticipationRequestDto(currentRequest));
//                currentRequest.setStatus(requestMapper.statusFromUpdateRequestStatus(updateRequest.getStatus()));
//                requestRepository.save(currentRequest);
//            } else {
//                rejected.add(requestMapper.toParticipationRequestDto(currentRequest));
//                currentRequest.setStatus(RequestStatus.REJECTED);
//                requestRepository.save(currentRequest);
//            }
//        }
//        result.setConfirmedRequests(confirmed);
//        result.setRejectedRequests(rejected);
//        return result;

        //TODO
        return null;
    }

    @Override
    public List<EventShortDto> getEventsByFilters(String text, List<Long> categories, Boolean paid, LocalDateTime rangeStart, LocalDateTime rangeEnd, Boolean onlyAvailable, EventSortType sortType, Integer from, Integer size) {
        Sort sort = switch (sortType) {
            case EVENT_DATE -> Sort.by("createdOn").ascending();
            case VIEWS -> Sort.by("views").ascending();
        };
        Pageable pageable = PageRequest.of(from, size, sort);
        List<Event> events = eventRepository.getEventsWithFilters(text, categories, paid, rangeStart, rangeEnd, onlyAvailable, pageable);

        return responseEventBuilder.buildManyEventResponseDto(events, EventShortDto.class);
    }

    @Override
    public EventFullDto getEventById(Long eventId) {
        Event eventDomain = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException(Constants.EVENT_NOT_FOUND));
        return responseEventBuilder.buildOneEventResponseDto(eventDomain, EventFullDto.class);
    }

    @Override
    public List<EventFullDto> getEventsWithFilters(List<Long> users, List<EventState> states, List<Long> categories, LocalDateTime rangeStart, LocalDateTime rangeEnd, Integer from, Integer size) {
        Pageable pageable = PageRequest.of(from, size);
        List<Event> events = eventRepository.getEventsWithFiltersAdmin(users, states, categories, rangeStart, rangeEnd, pageable);

        return responseEventBuilder.buildManyEventResponseDto(events, EventFullDto.class);
    }

    @Override
    @Transactional
    public EventFullDto updateEventByAdmin(Long eventId, UpdateEventAdminRequest updateDto) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException(Constants.EVENT_NOT_FOUND));

        if (updateDto.hasStateAction()) {
            if (event.getState() != EventState.PENDING) {
                throw new ConflictException("Изменить можно только отмененные события или события в состоянии ожидания модерации");
            }

            EventState state;

            if (updateDto.getStateAction() == PUBLISH_EVENT) {
                state = PUBLISHED;
                event.setPublishedOn(LocalDateTime.now());
            } else {
                state = REJECTED;
            }

            event.setState(state);
        }

        if (updateDto.hasEventDate()) {
            if (isEventTimeBad(updateDto.getEventDate(), 1)) {
                throw new ConflictException("Дата начала изменяемого события должна быть не ранее чем за час от даты публикации");
            }
            event.setEventDate(updateDto.getEventDate());
        }

        UpdateEventParam param = eventMapper.toUpdateParam(updateDto);
        updateEvent(event, param);

        return responseEventBuilder.buildOneEventResponseDto(event, EventFullDto.class);
    }

    private void updateEvent(Event event, UpdateEventParam param) {
        if (param.hasCategory()) {
            Category category = categoryRepository.findById(param.getCategory())
                    .orElseThrow(() -> new NotFoundException(CATEGORY_NOT_FOUND));
            event.setCategory(category);
        }

        if (param.hasAnnotation()) {
            event.setAnnotation(param.getAnnotation());
        }

        if (param.hasDescription()) {
            event.setDescription(param.getDescription());
        }

        if (param.hasLocation()) {
            event.setLocation(param.getLocation());
        }

        if (param.hasPaid()) {
            event.setPaid(param.getPaid());
        }

        if (param.hasParticipantLimit()) {
            event.setParticipantLimit(param.getParticipantLimit());
        }

        if (param.hasRequestModeration()) {
            event.setRequestModeration(param.getRequestModeration());
        }

        if (param.hasTitle()) {
            event.setTitle(param.getTitle());
        }
    }
}
