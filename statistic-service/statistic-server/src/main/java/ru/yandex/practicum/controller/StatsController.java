package ru.yandex.practicum.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.Statistic;
import ru.yandex.practicum.StatisticInfo;
import ru.yandex.practicum.model.StatisticFilter;
import ru.yandex.practicum.service.StatsService;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
    public void create(@Validated @RequestBody Statistic statistic) {
        log.info("POST \"/hit Body={}", statistic);
        statsService.create(statistic);
        log.debug("Created");
    }

    @GetMapping("/stats")
    public List<StatisticInfo> getStatistic(@RequestParam(name = "start") String start,
                                            @RequestParam(name = "end") String end,
                                            @RequestParam(name = "uris", defaultValue = "") ArrayList<String> uris,
                                            @RequestParam(name = "unique", defaultValue = "false") boolean unique) {
        log.info("GET \"/stats?start={}&end={}&uris={}&unique={}\"", start, end, uris, unique);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime startLDT = LocalDateTime.parse(URLDecoder.decode(start, StandardCharsets.UTF_8), formatter);
        LocalDateTime endLDT = LocalDateTime.parse(URLDecoder.decode(end, StandardCharsets.UTF_8), formatter);
        StatisticFilter statisticFilter = StatisticFilter.builder()
                .start(startLDT)
                .end(endLDT)
                .uris(uris)
                .unique(unique)
                .build();

        List<StatisticInfo> statisticInfo = statsService.getAllByFilter(statisticFilter);
        log.debug("return: " + statisticInfo.toString());
        return statisticInfo;
    }
}
