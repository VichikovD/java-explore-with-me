package ru.yandex.practicum;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;

@RestController
@Slf4j
@RequiredArgsConstructor
public class TestController {
    private final StatisticClient statisticClient;

    @PostMapping("/hitTest")
    public ResponseEntity<Object> create(@RequestBody StatisticRequestDto statisticRequestDto) {
        log.info("TEST-POST \"/hit Body={}", statisticRequestDto);
        ResponseEntity<Object> response = statisticClient.create(statisticRequestDto);
        log.debug("Created: " + response.toString());
        return response;
    }

    @GetMapping("/statsTest")
    public ResponseEntity<Object> getAllByFilter(@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss", iso = DateTimeFormat.ISO.DATE_TIME)
                                               @RequestParam(name = "start") LocalDateTime start,
                                               @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss", iso = DateTimeFormat.ISO.DATE_TIME)
                                               @RequestParam(name = "end") LocalDateTime end,
                                               @RequestParam(name = "uris", defaultValue = "") ArrayList<String> uris,
                                               @RequestParam(name = "unique", defaultValue = "false") boolean unique) {
        log.info("TEST-GET \"/stats?start={}&end={}&uris={}&unique={}\"", start, end, uris, unique);
        ResponseEntity<Object> response = statisticClient.getAllByFilter(start,end, uris,unique);
        log.debug("return: " + response.toString());
        return response;
    }
}
