package ru.practicum.main.service.event.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.main.service.Constants;
import ru.practicum.main.service.event.EventRepository;
import ru.practicum.main.service.event.MapperEvent;
import ru.practicum.main.service.event.dto.EventFullDto;
import ru.practicum.main.service.event.dto.EventShortDto;
import ru.practicum.main.service.event.enums.EventSortType;
import ru.practicum.main.service.event.model.Event;
import ru.practicum.main.service.exception.NotFoundException;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PublicEventServiceImpl implements PublicEventService {

    private final EventRepository eventRepository;
    private final MapperEvent eventMapper;

    @Override
    public List<EventShortDto> getEventsByFilters(String text, List<Long> categories, Boolean paid, LocalDateTime rangeStart, LocalDateTime rangeEnd, Boolean onlyAvailable, EventSortType sortType, Integer from, Integer size) {
        Sort sort = switch (sortType) {
            case EVENT_DATE -> Sort.by("createdOn").ascending();
            case VIEWS -> Sort.by("views").ascending();
        };
        Pageable pageable = PageRequest.of(from, size, sort);
        Page<Event> events = eventRepository.getEventsWithFilters(text, categories, paid, rangeStart, rangeEnd, onlyAvailable, pageable);
        return events.map(eventMapper::toEventShortDto).getContent();
    }

    @Override
    public EventFullDto getEventById(Long eventId) {
        Event eventDomain = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException(Constants.EVENT_NOT_FOUND));
        return eventMapper.toEventFullDto(eventDomain);
    }
}
