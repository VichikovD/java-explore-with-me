package ru.yandex.practicum;

import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class Test {
    public final StatisticClient statisticClient;

    public void postRequest() {
        System.out.println(statisticClient.create(getDto()));
    }

    public void getRequest() {
        System.out.println(statisticClient.getAllByFilter(
                LocalDateTime.now().minusDays(1),
                LocalDateTime.now().plusDays(1),
                (ArrayList<String>) List.of("uri"),
                false));
    }

    private StatisticRequestDto getDto() {
        return new StatisticRequestDto("app", "[uri]", "112.3123.1312", LocalDateTime.now());
    }
}
