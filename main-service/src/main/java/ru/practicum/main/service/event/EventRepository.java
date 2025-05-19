package ru.practicum.main.service.event;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import ru.practicum.main.service.event.enums.EventState;
import ru.practicum.main.service.event.model.Event;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface EventRepository extends JpaRepository<Event, Long>, QuerydslPredicateExecutor<Event> {
    Boolean existsByCategoryId(Long catId);

    @Query(value = """
            SELECT e
            FROM Event e WHERE (:text IS NULL OR e.annotation LIKE :text OR e.description LIKE :text) AND (:categories IS NULL OR e.category.id IN :categories) AND (:paid IS NULL OR e.paid = paid)
            AND (:rangeStart IS NULL OR e.createdOn > :rangeStart) AND (:rangeEnd IS NULL OR e.createdOn < :rangeEnd) AND (:onlyAvailable = false OR e.confirmedRequests < e.participantLimit)
            """, nativeQuery = true)
    List<Event> getEventsWithFilters(String text, List<Long> categories, Boolean paid, LocalDateTime rangeStart, LocalDateTime rangeEnd, Boolean onlyAvailable, Pageable pageable);


    @Query(value = """
            SELECT e
            FROM Event e WHERE (:users IS NULL OR e.initiator.id IN :users) AND (:states IS NULL OR e.state IN :states) AND (:categories IS NULL OR e.category.id IN :categories) AND (:rangeStart IS NULL OR e.createdOn > :rangeStart)
            AND (:rangeEnd IS NULL OR e.createdOn < :rangeEnd)
            """)
    List<Event> getEventsWithFiltersAdmin(List<Long> users, List<EventState> states, List<Long> categories, LocalDateTime rangeStart, LocalDateTime rangeEnd, Pageable pageable);

    @Query(value = """
            SELECT e
            FROM Event e WHERE e.initiator.id = :userId
            """)
    List<Event> getUserEvents(Long userId, Pageable pageable);

    Optional<Event> findByIdAndInitiatorId(Long eventId, Long initiatorId);
}
