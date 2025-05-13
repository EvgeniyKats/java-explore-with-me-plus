package ru.practicum.stats.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.stats.dto.ViewStatsDto;
import ru.practicum.stats.server.model.EndpointHit;

import java.time.LocalDateTime;
import java.util.List;

public interface StatsRepository extends JpaRepository<EndpointHit, Long> {

    @Query("SELECT new ru.practicum.stats.dto.ViewStatsDto(s.app, s.uri, CAST(COUNT(s.ip) AS INTEGER)) " +
            "FROM EndpointHit s " +
            "WHERE s.created BETWEEN :start AND :end " +
            "AND (:uris IS NULL OR s.uri IN :uris) " +
            "GROUP BY s.app, s.uri " +
            "ORDER BY COUNT(s.ip) DESC")
    List<ViewStatsDto> getAllStats(@Param("start") LocalDateTime start,
                                   @Param("end") LocalDateTime end,
                                   @Param("uris") List<String> uris);

    @Query("SELECT new ru.practicum.stats.dto.ViewStatsDto(s.app, s.uri, CAST(COUNT(DISTINCT s.ip) AS INTEGER)) " +
            "FROM EndpointHit s " +
            "WHERE s.created BETWEEN :start AND :end " +
            "AND (:uris IS NULL OR s.uri IN :uris) " +
            "GROUP BY s.app, s.uri " +
            "ORDER BY COUNT(DISTINCT s.ip) DESC")
    List<ViewStatsDto> findStatsWithUnique(@Param("start") LocalDateTime start,
                                           @Param("end") LocalDateTime end,
                                           @Param("uris") List<String> uris);

    List<EndpointHit> findByUri(String uri);

    long countByCreatedBetween(LocalDateTime start, LocalDateTime end);
}
