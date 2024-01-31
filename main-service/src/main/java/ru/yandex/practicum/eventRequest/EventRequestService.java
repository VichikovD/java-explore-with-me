package ru.yandex.practicum.eventRequest;

import ru.yandex.practicum.eventRequest.model.EventRequestInfoDto;

import java.util.List;

public interface EventRequestService {
    EventRequestInfoDto create(long requesterId, long eventId);
    List<EventRequestInfoDto> getAllByRequesterId(long requesterId);
}
