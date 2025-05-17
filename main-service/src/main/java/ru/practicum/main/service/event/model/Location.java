package ru.practicum.main.service.event.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "locations")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Location {

    @Id
    @Column(name = "event_id", nullable = false)
    Long eventId;

    @Column(nullable = false)
    Double latitude;

    @Column(nullable = false)
    Double longitude;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "eventId")
    Event event;

}
