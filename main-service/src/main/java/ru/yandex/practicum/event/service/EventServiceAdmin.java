package ru.yandex.practicum.event.service;

import ru.yandex.practicum.event.model.dto.EventFullInfoDto;
import ru.yandex.practicum.event.model.dto.EventRequestAdminDto;

import java.util.List;

public interface EventServiceAdmin {
    EventFullInfoDto updateAsAdmin(long eventId, EventRequestAdminDto eventRequestDto);

    List<EventFullInfoDto> getFullFiltered();
}
