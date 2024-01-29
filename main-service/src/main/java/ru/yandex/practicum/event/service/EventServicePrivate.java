package ru.yandex.practicum.event.service;

import org.springframework.data.domain.Pageable;
import ru.yandex.practicum.event.dto.EventInfoDto;
import ru.yandex.practicum.event.dto.EventRequestDto;

import java.util.List;


public interface EventServicePrivate {
    EventInfoDto create(long initiatorId, EventRequestDto eventRequestDto);

    List<EventInfoDto> getByInitiatorIdFiltered(long initiatorId, Pageable pageable);

    EventInfoDto getById(int compId);

}
