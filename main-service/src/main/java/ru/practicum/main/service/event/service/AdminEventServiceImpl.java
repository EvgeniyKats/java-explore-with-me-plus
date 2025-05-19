package ru.practicum.main.service.event.service;

import client.StatParam;
import client.StatsClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.main.service.Constants;
import ru.practicum.main.service.category.model.Category;
import ru.practicum.main.service.category.repository.CategoryRepository;
import ru.practicum.main.service.event.EventRepository;
import ru.practicum.main.service.event.MapperEvent;
import ru.practicum.main.service.event.dto.EventFullDto;
import ru.practicum.main.service.event.dto.UpdateEventAdminRequest;
import ru.practicum.main.service.event.enums.EventState;
import ru.practicum.main.service.event.model.Event;
import ru.practicum.main.service.exception.ConflictException;
import ru.practicum.main.service.exception.NotFoundException;
import ru.practicum.main.service.request.RequestRepository;
import ru.practicum.main.service.request.enums.RequestStatus;
import ru.practicum.stats.dto.ViewStatsDto;


import java.time.LocalDateTime;
import java.util.List;

import static ru.practicum.main.service.Constants.CATEGORY_NOT_FOUND;
import static ru.practicum.main.service.event.ValidatorEventTime.isEventTimeBad;
import static ru.practicum.main.service.event.dto.UpdateEventAdminRequest.StateAction.PUBLISH_EVENT;
import static ru.practicum.main.service.event.enums.EventState.PUBLISHED;
import static ru.practicum.main.service.event.enums.EventState.REJECTED;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AdminEventServiceImpl implements AdminEventService {

    private final MapperEvent eventMapper;
    private final EventRepository eventRepository;
    private final RequestRepository requestRepository;
    private final CategoryRepository categoryRepository;
    private final StatsClient statsClient;

    @Override
    public List<EventFullDto> getEventsWithFilters(List<Long> users, List<EventState> states, List<Long> categories, LocalDateTime rangeStart, LocalDateTime rangeEnd, Integer from, Integer size) {
        Pageable pageable = PageRequest.of(from, size);
        List<Event> events = eventRepository.getEventsWithFiltersAdmin(users, states, categories, rangeStart, rangeEnd, pageable);
        
        return events.stream()
                .map(eventMapper::toEventFullDto)
                .peek(this::fillEventFullDto)
                .toList();
    }

    @Override
    @Transactional
    public EventFullDto updateEvent(Long eventId, UpdateEventAdminRequest updateDto) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException(Constants.EVENT_NOT_FOUND));

        if (updateDto.hasStateAction()) {
            if (event.getState() != EventState.PENDING) {
                throw new ConflictException("Изменить можно только отмененные события или события в состоянии ожидания модерации");
            }
            EventState state = updateDto.getStateAction() == PUBLISH_EVENT ? PUBLISHED : REJECTED;
            event.setState(state);
        }

        if (updateDto.hasEventDate()) {
            if (isEventTimeBad(updateDto.getEventDate(), 1)) {
                throw new ConflictException("Дата начала изменяемого события должна быть не ранее чем за час от даты публикации");
            }
            event.setEventDate(updateDto.getEventDate());
        }

        if (updateDto.hasCategory()) {
            Category category = categoryRepository.findById(updateDto.getCategory())
                    .orElseThrow(() -> new NotFoundException(CATEGORY_NOT_FOUND));
            event.setCategory(category);
        }

        if (updateDto.hasAnnotation()) {
            event.setAnnotation(updateDto.getAnnotation());
        }

        if (updateDto.hasDescription()) {
            event.setDescription(updateDto.getDescription());
        }

        if (updateDto.hasLocation()) {
            event.setLocation(updateDto.getLocation());
        }

        if (updateDto.hasPaid()) {
            event.setPaid(updateDto.getPaid());
        }

        if (updateDto.hasParticipantLimit()) {
            event.setParticipantLimit(updateDto.getParticipantLimit());
        }

        if (updateDto.hasRequestModeration()) {
            event.setRequestModeration(updateDto.getRequestModeration());
        }

        if (updateDto.hasTitle()) {
            event.setTitle(updateDto.getTitle());
        }

        EventFullDto eventFullDto = eventMapper.toEventFullDto(event);
        fillEventFullDto(eventFullDto);
        return eventFullDto;
    }

    private void fillEventFullDto(EventFullDto eventFullDto) {
        long countConfirmedRequest = requestRepository.countByEventIdAndStatus(eventFullDto.getId(),
                RequestStatus.CONFIRMED);
        log.debug("event = {}, countConfirmedRequest = {}", eventFullDto.getId(), countConfirmedRequest);
        eventFullDto.setConfirmedRequests(countConfirmedRequest);

        StatParam statParam = new StatParam();
        statParam.setStart(eventFullDto.getCreatedOn());
        statParam.setEnd(LocalDateTime.now());
        statParam.setUris(List.of("events/" + eventFullDto.getId()));

        List<ViewStatsDto> viewStats = statsClient.getStat(statParam);
        long viewsCount = viewStats.isEmpty() ? 0 : viewStats.getFirst().getHits();
        eventFullDto.setViews(viewsCount);
    }
}
