package ru.yandex.practicum.event.service;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import ru.yandex.practicum.event.model.dto.EventFullInfoDto;
import ru.yandex.practicum.event.model.dto.EventShortInfoDto;

import java.time.LocalDateTime;
import java.util.List;


public interface EventServicePublic {
    List<EventShortInfoDto> getFiltered(String text, List<Long> categories, boolean paid, LocalDateTime rangeStart,
                                        LocalDateTime rangeEnd, boolean onlyAvailable, Pageable pageable, Sort sort);

    EventFullInfoDto getPublishedById(int eventId, String address, String uri);

}
