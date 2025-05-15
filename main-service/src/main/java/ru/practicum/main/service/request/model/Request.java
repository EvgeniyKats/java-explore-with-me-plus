package ru.practicum.main.service.request.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import ru.practicum.main.service.event.model.Event;
import ru.practicum.main.service.request.enums.RequestStatus;
import ru.practicum.main.service.user.model.User;

import java.time.LocalDateTime;

@Entity
@Table(name = "requests")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Request {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "request_id")
    Long id;

    @ManyToOne
    @JoinColumn(name = "requester_id", nullable = false)
    User requester;

    @ManyToOne
    @JoinColumn(name = "event_id", nullable = false)
    Event event;

    @Column(nullable = false, length = 120)
    @Enumerated(EnumType.ORDINAL)
    RequestStatus status;

    @Column(nullable = false)
    LocalDateTime created;
}










