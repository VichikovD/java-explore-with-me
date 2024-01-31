package ru.yandex.practicum.eventRequest;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.yandex.practicum.eventRequest.model.EventRequest;
import ru.yandex.practicum.eventRequest.model.EventRequestStatus;

import java.util.List;

public interface EventRequestRepository extends JpaRepository<EventRequest, Long> {
    List<EventRequest> findAllByRequesterId(long requesterId);

    long countByEventIdAndStatus(long eventId, EventRequestStatus status);
}
