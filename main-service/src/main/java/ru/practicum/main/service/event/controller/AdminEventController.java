package ru.practicum.main.service.event.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.main.service.event.dto.EventFullDto;
import ru.practicum.main.service.event.dto.UpdateEventAdminRequest;
import ru.practicum.main.service.event.enums.EventState;
import ru.practicum.main.service.event.service.AdminEventService;

import java.time.LocalDateTime;
import java.util.List;

import static ru.practicum.main.service.Constants.DATE_PATTERN;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/events")
@Validated
public class AdminEventController {

    private final AdminEventService eventService;

    @GetMapping
    public ResponseEntity<List<EventFullDto>> getEventsAdmin(@RequestParam(name = "users", required = false) List<Long> users,
                                         @RequestParam(name = "states", required = false) List<EventState> states,
                                         @RequestParam(name = "categories", required = false) List<Long> categories,
                                         @RequestParam(name = "rangeStart", required = false) @DateTimeFormat(pattern = DATE_PATTERN) LocalDateTime rangeStart,
                                         @RequestParam(name = "rangeEnd", required = false) @DateTimeFormat(pattern = DATE_PATTERN) LocalDateTime rangeEnd,
                                         @RequestParam(name = "from", required = false, defaultValue = "0") @Min(0) Integer from,
                                         @RequestParam(name = "size", required = false, defaultValue = "10") @Min(1) Integer size) {
        log.info("Пришел GET запрос /admin/events на Admin Event Controller");
        List<EventFullDto> events = eventService.getEventsWithFilters(users, states, categories, rangeStart, rangeEnd, from, size);
        log.info("Отправлен ответ GET /admin/events с телом: {}", events);
        return ResponseEntity.ok(events);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<EventFullDto> updateEvent(@PathVariable(name = "id") Long id,
                                    @Valid @RequestBody UpdateEventAdminRequest eventDto) {
        log.info("Пришел PATCH запрос на /admin/events/{} на Admin Event Controller с телом: {}", id, eventDto);
        EventFullDto event = eventService.updateEvent(id, eventDto);
        log.info("Отправлен ответ PATCH /admin/events/{} с телом: {}", id, event);
        return ResponseEntity.ok(event);
    }
}
