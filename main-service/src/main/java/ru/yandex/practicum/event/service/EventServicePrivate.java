package ru.yandex.practicum.event.service;

import org.springframework.data.domain.Pageable;
import ru.yandex.practicum.event.model.dto.EventCreateDto;
import ru.yandex.practicum.event.model.dto.EventFullInfoDto;
import ru.yandex.practicum.event.model.dto.EventShortInfoDto;
import ru.yandex.practicum.event.model.dto.EventUpdateDto;
import ru.yandex.practicum.eventRequest.model.EventRequestInfoDto;
import ru.yandex.practicum.eventRequest.model.EventRequestStatusChanger;
import ru.yandex.practicum.eventRequest.model.EventRequestStatusResult;

import java.util.List;


public interface EventServicePrivate {
    EventFullInfoDto create(long initiatorId, EventCreateDto eventCreateDto);

    EventFullInfoDto updateAsInitiator(long initiatorId, long eventId, EventUpdateDto eventUpdateDto);

    List<EventShortInfoDto> getByInitiatorIdFiltered(long initiatorId, Pageable pageable);

    EventFullInfoDto getByIdAndInitiatorId(long initiatorId, long eventId);

    List<EventRequestInfoDto> getEventRequestsByInitiatorIdAndEventId(long initiatorId, long eventId);

    EventRequestStatusResult updateEventRequestsAsInitiator(long requesterId,
                                                            long eventId,
                                                            EventRequestStatusChanger eventRequestStatusChanger);
}
