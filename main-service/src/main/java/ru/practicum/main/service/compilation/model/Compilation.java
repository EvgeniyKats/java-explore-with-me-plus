package ru.practicum.main.service.compilation.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.main.service.event.model.Event;

import java.util.Set;

@Entity
@Table(name = "compilations")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Compilation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "compilation_id")
    Long id;

    @Column(nullable = false)
    Boolean pinned;

    @Column(name = "compilation_title", nullable = false, unique = true, length = 50)
    String title;

    @ManyToMany
    @JoinTable(name = "events_compilations",
            joinColumns = {@JoinColumn(name = "compilation_id", referencedColumnName = "compilation_id")},
            inverseJoinColumns = {@JoinColumn(name = "event_id", referencedColumnName = "event_id")})
    Set<Event> events;
}










