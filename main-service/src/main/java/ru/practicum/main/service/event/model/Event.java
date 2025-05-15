package ru.practicum.main.service.event.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import ru.practicum.main.service.category.model.Category;
import ru.practicum.main.service.event.enums.EventState;
import ru.practicum.main.service.user.model.User;

import java.time.LocalDateTime;

@Entity
@Table(name = "events")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "event_id")
    Long id;

    @Column(name = "event_title", nullable = false, length = 155)
    String title;

    @Column(nullable = false, length = 3000)
    String description;

    @Column(nullable = false, length = 500)
    String annotation;

    @ManyToOne
    @JoinColumn(name = "initiator_id")
    User initiator;

    @ManyToOne
    @JoinColumn(name = "category_id")
    Category category;

    Long views;

    @Column(nullable = false, length = 50)
    @Enumerated(EnumType.STRING)
    EventState state;

    @Column(name = "created_on")
    LocalDateTime createdOn;

    @Column(name = "event_date")
    LocalDateTime eventDate;

    @Column(name = "published_on")
    LocalDateTime publishedOn;

    @Column(nullable = false)
    Boolean paid;

    @Column(name = "confirmed_requests")
    Long confirmedRequests;

    @Column(name = "participant_limit")
    Integer participantLimit;

    @Column(name = "request_moderation")
    Boolean requestModeration;

}
