package ru.yandex.practicum;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.dto.StatisticFilterDto;

import java.util.ArrayList;
import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
public class TestController {
    private final StatisticClient statisticClient;

    @PostMapping("/hitTest")
    public void create(@RequestBody Statistic statistic) {
        log.info("TEST-POST \"/hit Body={}", statistic);
        statisticClient.create(statistic);
    }

    @GetMapping("/statsTest")
    public List<StatisticInfo> getAllByFilter(@RequestParam(name = "start") String start,
                                              @RequestParam(name = "end") String end,
                                              @RequestParam(name = "uris", defaultValue = "") ArrayList<String> uris,
                                              @RequestParam(name = "unique", defaultValue = "false") boolean unique) {
        log.info("TEST-GET \"/stats?start={}&end={}&uris={}&unique={}\"", start, end, uris, unique);
        System.out.println("start: " + start + "\n" + "end: " + end);
        List<StatisticInfo> response = statisticClient.getAllByFilter(new StatisticFilterDto(start, end, uris, unique));
        log.debug("return: " + response.toString());
        return response;
    }
}
