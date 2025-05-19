package ru.practicum.main.service.event.controller;

import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.practicum.main.service.event.dto.EventFullDto;
import ru.practicum.main.service.event.dto.EventShortDto;
import ru.practicum.main.service.event.enums.EventSortType;
import ru.practicum.main.service.event.service.PublicEventService;

import java.time.LocalDateTime;
import java.util.List;

import static ru.practicum.main.service.Constants.DATE_PATTERN;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/events")
@Validated
public class PublicEventController {

    private final PublicEventService eventService;

    @GetMapping
    public ResponseEntity<List<EventShortDto>> getEventsByFilters(@RequestParam(name = "text", required = false) String text,
                                                                  @RequestParam(name = "categories", required = false) List<Long> categories,
                                                                  @RequestParam(name = "paid", required = false) Boolean paid,
                                                                  @RequestParam(name = "rangeStart", required = false) @DateTimeFormat(pattern = DATE_PATTERN) LocalDateTime rangeStart,
                                                                  @RequestParam(name = "rangeEnd", required = false) @DateTimeFormat(pattern = DATE_PATTERN) LocalDateTime rangeEnd,
                                                                  @RequestParam(name = "onlyAvailable", required = false, defaultValue = "false") Boolean onlyAvailable,
                                                                  @RequestParam(name = "sort", required = false) EventSortType sort,
                                                                  @RequestParam(name = "from", required = false, defaultValue = "0") @Min(0) Integer from,
                                                                  @RequestParam(name = "size", required = false, defaultValue = "10") @Min(1) Integer size) {
        log.info("Пришел GET запрос /events на Public Event Controller");
        List<EventShortDto> events = eventService.getEventsByFilters(text, categories, paid, rangeStart, rangeEnd, onlyAvailable, sort, from, size);
        log.info("Отправлен ответ на GET /events Public Event Controller с телом: {}", events);
        return ResponseEntity.ok(events);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EventFullDto> getEventById(@PathVariable Long id) {
        log.info("Пришел GET запрос на /events/{} Public Event Controller", id);
        EventFullDto event = eventService.getEventById(id);
        log.info("Отправлен ответ на GET /events/{} c телом: {}", id, event);
        return ResponseEntity.ok(event);
    }
}
