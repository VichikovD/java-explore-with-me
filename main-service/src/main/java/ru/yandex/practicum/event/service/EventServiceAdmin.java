package ru.yandex.practicum.event.service;

import org.springframework.data.domain.Pageable;
import ru.yandex.practicum.event.model.PublishState;
import ru.yandex.practicum.event.model.dto.EventFullInfoDto;
import ru.yandex.practicum.event.model.dto.EventRequestAdminDto;

import java.time.LocalDateTime;
import java.util.List;

public interface EventServiceAdmin {
    EventFullInfoDto updateAsAdmin(long eventId, EventRequestAdminDto eventRequestDto);

    List<EventFullInfoDto> getFullFiltered(List<Long> users, List<PublishState> publishStates, List<Long> categories,
                                           LocalDateTime rangeStart, LocalDateTime rangeEnd, Pageable pageable);
}
