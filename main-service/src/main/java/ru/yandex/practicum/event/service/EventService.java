package ru.yandex.practicum.event.service;

import org.springframework.data.domain.Pageable;
import ru.yandex.practicum.event.model.dto.*;
import ru.yandex.practicum.eventRequest.model.EventRequestInfoDto;
import ru.yandex.practicum.eventRequest.model.EventRequestStatusChanger;
import ru.yandex.practicum.eventRequest.model.EventRequestStatusResult;

import java.util.List;

public interface EventService {
    EventFullInfoDto create(long initiatorId, EventCreateDto eventCreateDto);

    EventFullInfoDto update(long initiatorId, long eventId, EventUpdateDto eventUpdateDto);

    EventFullInfoDto updateWithoutValidation(long eventId, EventRequestAdminDto eventRequestDto);

    List<EventFullInfoDto> findFullDtosFiltered(GetFullEventsRequest getFullEventsRequest);

    List<EventShortInfoDto> findShortDtosFiltered(GetShortEventsRequest getShortEventsRequest);

    EventFullInfoDto getPublishedById(int eventId, String address, String uri);

    List<EventShortInfoDto> getByInitiatorIdFiltered(long initiatorId, Pageable pageable);

    EventFullInfoDto getByIdAndInitiatorId(long initiatorId, long eventId);

    List<EventRequestInfoDto> getEventRequestsByInitiatorIdAndEventId(long initiatorId, long eventId);

    EventRequestStatusResult updateEventRequests(long requesterId,
                                                 long eventId,
                                                 EventRequestStatusChanger eventRequestStatusChanger);
}