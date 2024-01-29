package ru.yandex.practicum.event.service;

import ru.yandex.practicum.event.EventSort;
import ru.yandex.practicum.event.dto.EventInfoDto;

import java.util.List;


public interface EventServicePublic {
    List<EventInfoDto> getFiltered(String text, int[] categories, boolean paid, String rangeStart, String rangeEnd,
                                   boolean onlyAvailable, EventSort eventSort, int offset, int size);

    EventInfoDto getById(int compId);

}
