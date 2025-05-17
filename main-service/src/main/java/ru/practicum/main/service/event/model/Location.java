package ru.practicum.main.service.event.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
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
