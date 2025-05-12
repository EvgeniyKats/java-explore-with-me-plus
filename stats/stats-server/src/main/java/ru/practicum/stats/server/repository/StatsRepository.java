package ru.practicum.stats.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.stats.dto.ViewStatsDto;
import ru.practicum.stats.server.model.EndpointHit;

import java.sql.Timestamp;
import java.util.List;
import java.util.Set;

public interface StatsRepository extends JpaRepository<EndpointHit, Long> {

    @Query("SELECT s.app, s.uri, COUNT(s.ip) " +
            "FROM EndpointHit s " +
            "WHERE s.timestamp BETWEEN :start AND :end " +
            "AND (:uris IS NULL OR s.uri IN :uris) " +
            "GROUP BY s.app, s.uri " +
            "ORDER BY COUNT(s.ip) DESC")
    List<ViewStatsDto> getAllStats(@Param("start") Timestamp start,
                                   @Param("end") Timestamp end,
                                   @Param("uris") List<String> uris);

    @Query("SELECT s.app, s.uri, COUNT(DISTINCT s.ip) " +
            "FROM EndpointHit s " +
            "WHERE s.timestamp BETWEEN :start AND :end " +
            "AND (:uris IS NULL OR s.uri IN :uris) " +
            "GROUP BY s.app, s.uri " +
            "ORDER BY COUNT(DISTINCT s.ip) DESC")
    List<ViewStatsDto> findStatsWithUnique(@Param("start") Timestamp start,
                                           @Param("end") Timestamp end,
                                           @Param("uris") Set<String> uris);

    List<EndpointHit> findByUri(String uri);

    long countByTimestampBetween(Timestamp start, Timestamp end);
}
