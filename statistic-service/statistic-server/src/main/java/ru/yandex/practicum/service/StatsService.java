package ru.yandex.practicum.service;

import ru.yandex.practicum.StatisticInfo;
import ru.yandex.practicum.StatisticRequestDto;
import ru.yandex.practicum.model.StatisticFilter;

import java.util.List;

public interface StatsService {
    void create(StatisticRequestDto statisticRequestDto);

    List<StatisticInfo> getAllByFilter(StatisticFilter statisticFilter);
}
