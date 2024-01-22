package ru.yandex.practicum.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.StatisticInfoDto;
import ru.yandex.practicum.StatisticRequestDto;
import ru.yandex.practicum.model.StatisticFilter;
import ru.yandex.practicum.service.StatsService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestController
@Slf4j
@Validated
public class StatsController {
    private final StatsService statsService;

    @Autowired
    public StatsController(StatsService statsService) {
        this.statsService = statsService;
    }

    @PostMapping("/hit")
    @ResponseStatus(code = HttpStatus.CREATED)
    public void create(@Validated @RequestBody StatisticRequestDto statisticRequestDto) {
        log.info("POST \"/hit Body={}", statisticRequestDto);
        statsService.create(statisticRequestDto);
        log.debug("Created");
    }

    @GetMapping("/stats")
    public List<StatisticInfoDto> getStatistic(@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss", iso = DateTimeFormat.ISO.DATE_TIME)
                                                   @RequestParam(name = "start") LocalDateTime start,
                                               @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss", iso = DateTimeFormat.ISO.DATE_TIME)
                                                   @RequestParam(name = "end") LocalDateTime end,
                                               @RequestParam(name = "uris", defaultValue = "") ArrayList<String> uris,
                                               @RequestParam(name = "unique", defaultValue = "false") boolean unique) {
        log.info("GET \"/stats?start={}&end={}&uris={}&unique={}\"", start, end, uris, unique);
        StatisticFilter statisticFilter = StatisticFilter.builder()
                .start(start)
                .end(end)
                .uris(uris)
                .unique(unique)
                .build();
        List<StatisticInfoDto> statisticInfoDto = statsService.getAllByFilter(statisticFilter);
        log.debug("return: " + statisticInfoDto.toString());
        return statisticInfoDto;
    }
}
