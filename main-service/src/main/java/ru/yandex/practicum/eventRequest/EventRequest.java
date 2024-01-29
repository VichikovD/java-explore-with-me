package ru.yandex.practicum.eventRequest;

import ru.yandex.practicum.event.Event;
import ru.yandex.practicum.user.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "event_requests")
public class EventRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "event_request_id")
    Long id;

    @ManyToOne
    @JoinColumn(name = "event_id", nullable = false)
    Event event;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    User requester;

    @Column(name = "status")
    EventRequestStatus status;

    @Column(name = "created")
    LocalDateTime created;
}
