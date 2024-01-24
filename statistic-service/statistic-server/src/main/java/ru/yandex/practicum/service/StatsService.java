package ru.yandex.practicum.service;

import ru.yandex.practicum.Statistic;
import ru.yandex.practicum.StatisticInfo;
import ru.yandex.practicum.model.StatisticFilter;

import java.util.List;

public interface StatsService {
    void create(Statistic Statistic);

    List<StatisticInfo> getAllByFilter(StatisticFilter statisticFilter);
}
