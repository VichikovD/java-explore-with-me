package ru.yandex.practicum.event.service;

import ru.yandex.practicum.event.model.EventSort;
import ru.yandex.practicum.event.model.dto.EventFullInfoDto;

import java.util.List;


public interface EventServicePublic {
    List<EventFullInfoDto> getFiltered(String text, int[] categories, boolean paid, String rangeStart, String rangeEnd,
                                       boolean onlyAvailable, EventSort eventSort, int offset, int size);

    EventFullInfoDto getById(int compId);

}
