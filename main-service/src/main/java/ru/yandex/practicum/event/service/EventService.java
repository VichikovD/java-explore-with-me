package ru.yandex.practicum.event.service;

import ru.yandex.practicum.event.EventDto;
import ru.yandex.practicum.event.EventSort;

import java.util.List;


public interface EventService {
    List<EventDto> getFiltered(String text, int[] categories, boolean paid, String rangeStart, String rangeEnd,
                               boolean onlyAvailable, EventSort eventSort, int offset, int size);

    EventDto getById(int compId);

}
