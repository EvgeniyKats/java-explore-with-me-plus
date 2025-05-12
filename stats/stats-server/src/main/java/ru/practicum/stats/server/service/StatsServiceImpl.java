package ru.practicum.stats.server.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.stats.dto.EndpointHitDto;
import ru.practicum.stats.dto.ViewStatsDto;
import ru.practicum.stats.server.mapper.StatsMapper;
import ru.practicum.stats.server.model.EndpointHit;
import ru.practicum.stats.server.repository.StatsRepository;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
public class StatsServiceImpl implements StatsService {

    private final StatsMapper statsMapper;
    private final StatsRepository statsRepository;

    @Transactional
    @Override
    public void saveHit(EndpointHitDto hitDto, String userIp) {
        EndpointHit endpointHit = statsMapper.mapToModel(hitDto);
        endpointHit.setTimestamp(Timestamp.from(Instant.now()));
        endpointHit.setIp(userIp);
        statsRepository.save(endpointHit);
    }

    @Override
    public List<ViewStatsDto> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique) {
        if (unique) {
            return statsRepository.findStatsWithUnique(Timestamp.valueOf(start), Timestamp.valueOf(end), Set.copyOf(uris));
        } else {
            return statsRepository.getAllStats(Timestamp.valueOf(start), Timestamp.valueOf(end), uris);
        }
    }

}
