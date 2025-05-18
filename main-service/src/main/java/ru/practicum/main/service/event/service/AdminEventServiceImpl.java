package ru.practicum.main.service.event.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.main.service.Constants;
import ru.practicum.main.service.event.EventRepository;
import ru.practicum.main.service.event.MapperEvent;
import ru.practicum.main.service.event.dto.EventFullDto;
import ru.practicum.main.service.event.dto.UpdateEventAdminRequest;
import ru.practicum.main.service.event.enums.EventState;
import ru.practicum.main.service.event.model.Event;
import ru.practicum.main.service.exception.ConflictException;
import ru.practicum.main.service.exception.NotFoundException;


import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AdminEventServiceImpl implements AdminEventService {

    private final MapperEvent eventMapper;
    private final EventRepository eventRepository;

    @Override
    public List<EventFullDto> getEventsWithFilters(List<Long> users, List<EventState> states, List<Long> categories, LocalDateTime rangeStart, LocalDateTime rangeEnd, Integer from, Integer size) {
        Pageable pageable = PageRequest.of(from, size);
        Page<Event> events = eventRepository.getEventsWithFiltersAdmin(users, states, categories, rangeStart, rangeEnd, pageable);
        return events.map(eventMapper::toEventFullDto).getContent();
    }

    @Override
    @Transactional
    public EventFullDto updateEvent(Long eventId, UpdateEventAdminRequest eventDto) {
        Event eventFromDb = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException(Constants.EVENT_NOT_FOUND));
        if (eventDto.getStateAction() == UpdateEventAdminRequest.StateAction.PUBLISH_EVENT && eventFromDb.getState() != EventState.PENDING) {
            throw new ConflictException("Нельзя опубликовать событие, так как оно не находится в статусе ожидания публикации");
        }
        if (eventDto.getStateAction() == UpdateEventAdminRequest.StateAction.REJECT_EVENT && eventFromDb.getState() == EventState.PUBLISHED) {
            throw new ConflictException("Нельзя отклонить событие, так как оно уже опубликовано");
        }
        if (eventDto.getEventDate().minusMinutes(59).isBefore(eventFromDb.getCreatedOn())) {
            throw new ConflictException("Дата начала изменяемого события должна быть не ранее чем за час от даты публикации");
        }
        Event update = eventMapper.toEvent(eventDto);
        return eventMapper.toEventFullDto(eventRepository.save(update));
    }
}
