package ru.practicum.stats.server.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.stats.dto.HitDto;
import ru.practicum.stats.dto.StatDto;
import ru.practicum.stats.dto.StatParamDto;

import java.util.ArrayList;
import java.util.List;

@Transactional(readOnly = true)
@Service
public class StatsServiceImpl implements StatsService {

    private final StatsRepository statsRepository;

    @Transactional
    @Override
    public void saveHit(HitDto hitDto) {
        statsRepository.save(hitDto);
    }

    @Override
    public List<StatDto> getStats(StatParamDto statParamDto) {
        if (statParamDto.getUris().isEmpty() && !statParamDto.getUnique()) {
            return statsRepository.getAllStats(statParamDto.getStart(), statParamDto.getEnd());
        } else if (statParamDto.getUnique()) {
            return statsRepository.findStatsWithUnique(statParamDto.getStart(), statParamDto.getEnd(), statParamDto.getUris());
        } else {
            List<StatDto> statDtos = new ArrayList<>();
            for (String uri : statParamDto.getUris()) {
                statDtos.add(statsRepository.findByUri(uri));
            }
            return statDtos;
        }

    }
}
