package ru.yandex.practicum.event.service;

import org.springframework.data.domain.Pageable;
import ru.yandex.practicum.event.model.dto.EventFullInfoDto;
import ru.yandex.practicum.event.model.dto.EventRequestDto;
import ru.yandex.practicum.event.model.dto.EventShortInfoDto;

import java.util.List;


public interface EventServicePrivate {
    EventFullInfoDto create(long initiatorId, EventRequestDto eventRequestDto);

    List<EventShortInfoDto> getByInitiatorIdFiltered(long initiatorId, Pageable pageable);

    EventFullInfoDto getByIdAndInitiatorId(long initiatorId, long eventId);

}
