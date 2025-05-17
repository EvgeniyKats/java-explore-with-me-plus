package ru.practicum.main.service.request;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import ru.practicum.main.service.request.dto.ParticipationRequestDto;
import ru.practicum.main.service.request.model.Request;

import java.util.List;

public interface RequestRepository extends JpaRepository<Request, Long>, QuerydslPredicateExecutor<Request> {

    @Query("SELECT new ru.practicum.main.service.request.dto.ParticipationRequestDto(r.created, r.event.id, r.id, " +
            "r.requester.id, r.status) " +
            "FROM Request r WHERE r.requester.id = ?1")
    List<ParticipationRequestDto> findAllByUserId(Long userId);

    boolean existsByEventIdAndRequesterId(Long eventId, Long requesterId);

    Long countByEventId(Long eventId);
}
