package ru.yandex.practicum.event.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.Statistic;
import ru.yandex.practicum.StatisticClient;
import ru.yandex.practicum.event.model.EventSort;
import ru.yandex.practicum.event.model.dto.EventFullInfoDto;
import ru.yandex.practicum.event.model.dto.EventShortInfoDto;
import ru.yandex.practicum.event.service.EventService;
import ru.yandex.practicum.util.OffsetPageable;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/events")
@RequiredArgsConstructor
public class EventPublicController {
    final EventService eventService;

    final StatisticClient statisticClient;
    static final String APPLICATION_NAME = "ewm-main-service";

    @GetMapping
    public List<EventShortInfoDto> getFiltered(@RequestParam(name = "text", required = false) String text,
                                               @RequestParam(name = "categories", required = false) List<Long> categories,
                                               @RequestParam(name = "paid", required = false) Boolean paid,
                                               @RequestParam(name = "rangeStart", required = false)
                                               @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeStart,
                                               @RequestParam(name = "rangeEnd", required = false)
                                               @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeEnd,
                                               @RequestParam(name = "onlyAvailable", defaultValue = "false") boolean onlyAvailable,
                                               @RequestParam(name = "sort", defaultValue = "EVENT_DATE") String stringSort,
                                               @RequestParam(name = "from", defaultValue = "0") int offset,
                                               @RequestParam(name = "size", defaultValue = "10") int limit,
                                               HttpServletRequest request) {
        if (rangeStart != null && rangeEnd != null) {
            if (rangeStart.isAfter(rangeEnd)) {
                throw new IllegalArgumentException("Start can't be after end");
            }
        }

        String address = request.getRemoteAddr();
        String uri = request.getRequestURI();
        log.info("GET \"/events?text={}&categories={}&paid={}&rangeStart={}&rangeEnd={}&onlyAvailable={}&sort={}&from={}&size={}\" " +
                        "Address={}, URI={}", text, categories, paid, rangeStart, rangeEnd, onlyAvailable, stringSort,
                offset, limit, address, uri);
        EventSort eventSort = EventSort.from(stringSort);
        Sort sort;
        if (EventSort.EVENT_DATE.equals(eventSort)) {
            sort = Sort.by(Sort.Direction.ASC, "eventDate");
        } else if (EventSort.VIEWS.equals(eventSort)) {
            sort = Sort.by(Sort.Direction.DESC, "views");
        } else {
            throw new RuntimeException("Sort not supported");
        }
        Pageable pageable = new OffsetPageable(offset, limit);

        List<EventShortInfoDto> eventList = eventService.findShortDtosFiltered(text, categories, paid, rangeStart, rangeEnd,
                onlyAvailable, pageable, sort, address, uri);
        sendStatistic(address, uri);
        log.debug("EventList found= " + eventList);
        return eventList;
    }

    @GetMapping("/{eventId}")
    public EventFullInfoDto getPublishedById(@PathVariable(name = "eventId") int eventId,
                                             HttpServletRequest request) {
        String address = request.getRemoteAddr();
        String uri = request.getRequestURI();
        log.info("GET \"/events/{} Address={}, URI={}", eventId, address, uri);

        EventFullInfoDto event = eventService.getPublishedById(eventId, address, uri);
        sendStatistic(address, uri);
        log.debug("Event found= " + event);
        return event;
    }

    private void sendStatistic(String address, String uri) {
        Statistic statisticToSend = Statistic.builder()
                .app(APPLICATION_NAME)
                .uri(uri)
                .ip(address)
                .timestamp(LocalDateTime.now())
                .build();
        log.debug("Statistic sent=" + statisticToSend);
        statisticClient.create(statisticToSend);
    }
}
