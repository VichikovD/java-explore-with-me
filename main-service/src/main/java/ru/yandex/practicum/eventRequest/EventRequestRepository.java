package ru.yandex.practicum.eventRequest;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.yandex.practicum.eventRequest.model.EventRequest;
import ru.yandex.practicum.eventRequest.model.EventRequestStatus;

import java.util.Collection;
import java.util.List;

public interface EventRequestRepository extends JpaRepository<EventRequest, Long> {
    List<EventRequest> findByRequesterId(long requesterId);

    List<EventRequest> findByIdInAndEventId(List<Long> idList, long eventId);

    List<EventRequest> findByEventIdInAndStatus(Collection<Long> eventIdList, EventRequestStatus status);

    long countByEventIdAndStatus(long eventId, EventRequestStatus status);

    List<EventRequest> findByEventInitiatorIdAndEventId(long initiatorId, long eventId);

    List<EventRequest> findByStatus(EventRequestStatus status);

}
