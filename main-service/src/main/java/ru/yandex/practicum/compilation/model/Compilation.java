package ru.yandex.practicum.compilation.model;

import lombok.*;
import ru.yandex.practicum.event.model.Event;

import javax.persistence.*;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
@Builder
@Entity
@Table(name = "compilations")
public class Compilation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "compilation_id")
    long id;

    @Column(name = "pinned")
    boolean pinned;

    @Column(name = "title")
    String title;

    @ManyToMany
    @JoinTable(
            name = "compilations_events",
            joinColumns = @JoinColumn(name = "compilation_id"),
            inverseJoinColumns = @JoinColumn(name = "event_id")
    )
    Set<Event> events;
}
