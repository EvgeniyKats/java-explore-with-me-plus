package ru.practicum.main.service.event.util;

import client.StatParam;
import client.StatsClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.practicum.main.service.event.MapperEvent;
import ru.practicum.main.service.event.dto.EventFullDto;
import ru.practicum.main.service.event.dto.EventShortDto;
import ru.practicum.main.service.event.dto.ResponseEvent;
import ru.practicum.main.service.event.model.Event;
import ru.practicum.main.service.request.RequestRepository;
import ru.practicum.main.service.request.enums.RequestStatus;
import ru.practicum.main.service.request.model.ConfirmedRequests;
import ru.practicum.stats.dto.ViewStatsDto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static ru.practicum.main.service.Constants.MIN_START_DATE;

@Component
@RequiredArgsConstructor
@Slf4j
public class ResponseEventBuilder {
    private final MapperEvent eventMapper;
    private final RequestRepository requestRepository;
    private final StatsClient statsClient;

    public <T extends ResponseEvent> T buildOneEventResponseDto(Event event, Class<T> type) {
        T dto;

        if (type == EventFullDto.class) {
            EventFullDto dtoTemp = eventMapper.toEventFullDto(event);
            dto = type.cast(dtoTemp);
        } else {
            EventShortDto dtoTemp = eventMapper.toEventShortDto(event);
            dto = type.cast(dtoTemp);
        }

        long eventId = event.getId();
        LocalDateTime created = event.getCreatedOn();

        dto.setConfirmedRequests(getOneEventConfirmedRequests(eventId));
        dto.setViews(getOneEventViews(created, eventId));
        return dto;
    }

    public <T extends ResponseEvent> List<T> buildManyEventResponseDto(List<Event> events, Class<T> type) {
        Map<Long, T> dtoById = new HashMap<>();

        for (Event event : events) {
            if (type == EventFullDto.class) {
                EventFullDto dtoTemp = eventMapper.toEventFullDto(event);
                dtoById.put(event.getId(), type.cast(dtoTemp));
            } else {
                EventShortDto dtoTemp = eventMapper.toEventShortDto(event);
                dtoById.put(event.getId(), type.cast(dtoTemp));
            }
        }

        getManyEventsConfirmedRequests(dtoById.keySet()).forEach(req ->
                dtoById.get(req.eventId()).setConfirmedRequests(req.countRequests()));


        getManyEventsViews(dtoById.keySet()).forEach(stats -> {
            Long id = Long.parseLong(stats.getUri().replace("/events/", ""));
            dtoById.get(id).setViews(stats.getHits());
        });

        return new ArrayList<>(dtoById.values());
    }

    private int getOneEventConfirmedRequests(long eventId) {
        return requestRepository.countByEventIdAndStatus(eventId, RequestStatus.CONFIRMED);
    }

    private long getOneEventViews(LocalDateTime created, long eventId) {
        StatParam statParam = new StatParam();
        statParam.setStart(created.minusMinutes(1));
        statParam.setEnd(LocalDateTime.now().plusMinutes(1));
        statParam.setUnique(true);
        statParam.setUris(List.of("/events/" + eventId));

        List<ViewStatsDto> viewStats = statsClient.getStat(statParam);
        log.debug("Получен {} одиночный от статистики по запросу uris = {}, start = {}",
                created,
                statParam.getUris(),
                statParam.getStart());
        return viewStats.isEmpty() ? 0 : viewStats.getFirst().getHits();
    }

    private List<ConfirmedRequests> getManyEventsConfirmedRequests(Collection<Long> eventIds) {
        return requestRepository.getConfirmedRequests(eventIds, RequestStatus.CONFIRMED);
    }

    private List<ViewStatsDto> getManyEventsViews(Collection<Long> eventIds) {
        StatParam statParam = new StatParam();
        statParam.setStart(MIN_START_DATE);
        statParam.setEnd(LocalDateTime.now().plusMinutes(1));
        statParam.setUnique(true);
        List<String> uris = eventIds.stream()
                .map(id -> "/events/" + id)
                .toList();

        statParam.setUris(uris);

        List<ViewStatsDto> viewStats = statsClient.getStat(statParam);
        log.debug("Получен ответ size = {}, массовый от статистики по запросу uris = {}, start = {}, end = {}",
                viewStats.size(),
                statParam.getUris(),
                statParam.getStart(),
                statParam.getEnd());
        return viewStats;
    }
}
