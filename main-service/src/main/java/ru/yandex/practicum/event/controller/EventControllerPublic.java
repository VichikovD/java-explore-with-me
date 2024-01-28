package ru.yandex.practicum.event.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.event.EventDto;
import ru.yandex.practicum.event.EventSort;
import ru.yandex.practicum.event.service.EventService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/events")
@RequiredArgsConstructor
public class EventControllerPublic {
    final EventService eventService;

    @GetMapping
    public List<EventDto> getFiltered(@RequestParam(name = "text") String text,
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

        List<EventDto> EventList = eventService.getFiltered(text, categories, paid, rangeStart, rangeEnd, onlyAvailable,
                eventSort, offset, size);
        log.debug("EventList = " + EventList);
        return EventList;
    }

    @GetMapping("/{eventId}")
    public EventDto getById(@PathVariable(name = "eventId") int eventId) {
        log.info("GET \"/events/{}", eventId);
        EventDto event = eventService.getById(eventId);
        log.debug("event = " + event);
        return event;
    }
}
