package ru.yandex.practicum.event.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.event.model.EventSort;
import ru.yandex.practicum.event.model.dto.EventFullInfoDto;
import ru.yandex.practicum.event.model.dto.EventShortInfoDto;
import ru.yandex.practicum.event.service.EventServicePublic;
import ru.yandex.practicum.util.OffsetPageable;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/events")
@RequiredArgsConstructor
public class EventControllerPublic {
    final EventServicePublic eventService;

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
        log.info("GET \"/events?text={}&categories={}&paid={}&rangeStart={}&rangeEnd={}&onlyAvailable={}&sort={}&from={}&size={}\" " +
                        "Address={}, URI={}", text, categories, paid, rangeStart, rangeEnd, onlyAvailable, stringSort,
                offset, limit, request.getRemoteAddr(), request.getRequestURI());
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


        List<EventShortInfoDto> eventList = eventService.getFiltered(text, categories, paid, rangeStart, rangeEnd,
                onlyAvailable, pageable, sort, request);
        log.debug("EventList found= " + eventList);
        return eventList;
    }

    // "информацию о том, что по этому эндпоинту был осуществлен и обработан запрос, нужно сохранить в сервисе статистики"
    @GetMapping("/{eventId}")
    public EventFullInfoDto getPublishedById(@PathVariable(name = "eventId") int eventId,
                                             HttpServletRequest request) {
        String address = request.getRemoteAddr();
        String uri = request.getRequestURI();
        log.info("GET \"/events/{} Address={}, URI={}", eventId, address, uri);

        EventFullInfoDto event = eventService.getPublishedById(eventId, request);
        log.debug("Event found= " + event);
        return event;
    }
}
