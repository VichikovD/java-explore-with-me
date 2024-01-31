package ru.yandex.practicum.event.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.event.model.EventSort;
import ru.yandex.practicum.event.model.dto.EventFullInfoDto;
import ru.yandex.practicum.event.service.EventServicePublic;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/events")
@RequiredArgsConstructor
public class EventControllerPublic {
    final EventServicePublic eventService;

    @GetMapping
    public List<EventFullInfoDto> getFiltered(@RequestParam(name = "text") String text,
                                              @RequestParam(name = "categories") int[] categories,
                                              @RequestParam(name = "paid") boolean paid,
                                              @RequestParam(name = "rangeStart") String rangeStart,
                                              @RequestParam(name = "rangeEnd") String rangeEnd,
                                              @RequestParam(name = "onlyAvailable") boolean onlyAvailable,
                                              @RequestParam(name = "sort") EventSort eventSort,
                                              @RequestParam(name = "from", defaultValue = "0") int offset,
                                              @RequestParam(name = "size", defaultValue = "10") int size) {
        log.info("GET \"/events?text={}&categories={}&paid={}&rangeStart={}&rangeEnd={}&onlyAvailable={}&sort={}&from={}&size={}\"",
                text, categories, paid, rangeStart, rangeEnd, onlyAvailable, eventSort, offset, size);

        Sort sort;
        if (EventSort.EVENT_DATE.equals(eventSort)) {
            sort = Sort.by(Sort.Direction.ASC, "eventDate");
        } else if (EventSort.VIEWS.equals(eventSort)) {
            sort = Sort.by(Sort.Direction.DESC, "views");
        } else {
            throw new RuntimeException("Sort not supported");
        }
        System.out.println(sort); // testing

        List<EventFullInfoDto> eventList = eventService.getFiltered(text, categories, paid, rangeStart, rangeEnd, onlyAvailable,
                eventSort, offset, size);
        log.debug("EventList = " + eventList);
        return eventList;
    }

    // "информацию о том, что по этому эндпоинту был осуществлен и обработан запрос, нужно сохранить в сервисе статистики"
    @GetMapping("/{eventId}")
    public EventFullInfoDto getById(@PathVariable(name = "eventId") int eventId,
                                    HttpServletRequest request) {
        String address = request.getRemoteAddr();
        String uri = request.getRequestURI();
        log.info("GET \"/events/{} Address={}, URI={}", eventId, address, uri);

        EventFullInfoDto event = eventService.getById(eventId, address, uri);
        log.debug("event = " + event);
        return event;
    }
}
