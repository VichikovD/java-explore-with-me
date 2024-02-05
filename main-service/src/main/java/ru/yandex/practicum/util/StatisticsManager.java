package ru.yandex.practicum.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.Statistic;
import ru.yandex.practicum.StatisticClient;
import ru.yandex.practicum.StatisticInfo;
import ru.yandex.practicum.dto.StatisticFilterDto;
import ru.yandex.practicum.event.model.dto.EventFullInfoDto;
import ru.yandex.practicum.event.model.dto.EventShortInfoDto;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
public class StatisticsManager {
    final StatisticClient statisticClient;
    static final String APPLICATION_NAME = "ewm-main-service";
    final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public void sendStatistic(HttpServletRequest request) {
        Statistic statisticToSend = Statistic.builder()
                .app(APPLICATION_NAME)
                .uri(request.getRequestURI())
                .ip(request.getRemoteAddr())
                .timestamp(LocalDateTime.now())
                .build();
        System.out.println("Statistic sent=" + statisticToSend);
        log.debug("Statistic sent=" + statisticToSend);
        statisticClient.create(statisticToSend);
    }

    public void updateViewsToShortInfoDtos(Collection<EventShortInfoDto> eventCollection) {
        List<Long> eventIdList = eventCollection.stream()
                .map((EventShortInfoDto::getId))
                .collect(Collectors.toList());

        Map<Long, Long> idToViewsMap = getIdToViewsMapByIdCollection(eventIdList);
        for (EventShortInfoDto event : eventCollection) {
            event.setViews(idToViewsMap.getOrDefault(event.getId(), 0L));
        }
    }

    public void updateViewsToFullInfoDtos(Collection<EventFullInfoDto> eventCollection) {
        List<Long> eventIdList = eventCollection.stream()
                .map((EventFullInfoDto::getId))
                .collect(Collectors.toList());

        Map<Long, Long> idToViewsMap = getIdToViewsMapByIdCollection(eventIdList);
        for (EventFullInfoDto event : eventCollection) {
            event.setViews(idToViewsMap.getOrDefault(event.getId(), 0L));
        }
    }

    private Map<Long, Long> getIdToViewsMapByIdCollection(Collection<Long> eventIdCollection) {
        System.out.println("eventIdCollection to send=" + eventIdCollection);
        log.debug("eventIdCollection to send=" + eventIdCollection);
        ArrayList<String> uris = eventIdCollection.stream()
                .map((eventId) -> "/events/" + eventId)
                .collect(Collectors.toCollection(ArrayList::new));

        LocalDateTime start = LocalDateTime.of(2024, 1, 1, 0, 0, 0);
        LocalDateTime end = LocalDateTime.now().plusYears(100);
        /*LocalDateTime end = LocalDateTime.ofInstant(Instant.MAX, ZoneOffset.systemDefault());*/
        StatisticFilterDto statisticFilterDto = StatisticFilterDto.builder()
                .start(start.format(formatter))
                .end(end.format(formatter))
                .uris(uris)
                .unique(true)
                .build();

        List<StatisticInfo> statisticInfoList = statisticClient.getAllByFilter(statisticFilterDto);
        ObjectMapper mapper = new ObjectMapper();
        List<StatisticInfo> actualStatisticInfoList = mapper.convertValue(statisticInfoList, new TypeReference<List<StatisticInfo>>() {
        });
        Map<Long, Long> eventIdToViews = actualStatisticInfoList.stream()
                .filter(statisticInfo -> statisticInfo.getApp().equalsIgnoreCase(APPLICATION_NAME) &&
                        statisticInfo.getUri().startsWith("/events/"))
                .collect(Collectors.toMap(statisticInfo -> Long.parseLong(statisticInfo.getUri().substring(8)),
                        StatisticInfo::getHits));
        log.debug("StatisticInfoList received=" + statisticInfoList);
        System.out.println("StatisticInfoList received=" + statisticInfoList);
        log.debug("eventIdToViewsMAP received=" + eventIdToViews);
        System.out.println("eventIdToViewsMAP received=" + eventIdToViews);
        return eventIdToViews;
    }
}
