package ru.yandex.practicum.mapper;

import ru.yandex.practicum.StatisticInfoDto;
import ru.yandex.practicum.StatisticRequestDto;
import ru.yandex.practicum.model.Statistic;

public class StatisticMapper {
    public static Statistic toModel(StatisticRequestDto statisticRequestDto) {
        return Statistic.builder()
                .uri(statisticRequestDto.getUri())
                .app(statisticRequestDto.getApp())
                .ip(statisticRequestDto.getIp())
                .created(statisticRequestDto.getTimestamp())
                .build();
    }

    /*public static StatisticInfoDto toInfoDto(Statistic statistic) {
        return StatisticInfoDto.builder()
                .id(statistic.getId())
                .uri(statistic.getUri())
                .app(statistic.getApp())
                .ip(statistic.getIp())
                .timestamp(statistic.getCreated())
                .build();
    }*/
}
