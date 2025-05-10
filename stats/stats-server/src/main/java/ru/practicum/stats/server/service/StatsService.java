package ru.practicum.stats.server.service;

import ru.practicum.stats.dto.HitDto;
import ru.practicum.stats.dto.StatDto;
import ru.practicum.stats.dto.StatParamDto;

import java.util.List;

public interface StatsService {

    void saveHit(HitDto hitDto);

    List<StatDto> getStats(StatParamDto statParamDto);
}
