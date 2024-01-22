package ru.yandex.practicum.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.StatisticInfoDto;
import ru.yandex.practicum.StatisticRequestDto;
import ru.yandex.practicum.exception.ValidateException;
import ru.yandex.practicum.mapper.StatisticMapper;
import ru.yandex.practicum.model.Statistic;
import ru.yandex.practicum.model.StatisticFilter;
import ru.yandex.practicum.repository.StatisticRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StatsServiceImpl implements StatsService{
    final StatisticRepository statisticRepository;

    @Override
    public void create(StatisticRequestDto statisticRequestDto) {
        Statistic statisticToSave = StatisticMapper.toModel(statisticRequestDto);
        statisticRepository.save(statisticToSave);
    }

    @Override
    public List<StatisticInfoDto> getAllByFilter(StatisticFilter statisticFilter) {
        if(statisticFilter.getStart().isAfter(statisticFilter.getEnd())) {
            throw new ValidateException("Start should be before End");
        }

        LocalDateTime start = statisticFilter.getStart();
        LocalDateTime end = statisticFilter.getEnd();
        ArrayList<String> uris = statisticFilter.getUris();
        boolean isUnique = statisticFilter.isUnique();

        if(uris == null || uris.isEmpty()) {
            if (isUnique) {
                return statisticRepository.getAllStatisticUnique(start, end);
            } else {
                return statisticRepository.getAllStatistic(start, end);
            }
        } else {
            if (isUnique) {
                return statisticRepository.getStatisticInUrisUnique(start, end, uris);
            } else {
                return statisticRepository.getStatisticInUris(start, end, uris);
            }
        }
    }
}
