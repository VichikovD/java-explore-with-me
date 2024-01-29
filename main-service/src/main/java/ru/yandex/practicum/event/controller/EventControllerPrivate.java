package ru.yandex.practicum.event.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.event.dto.EventInfoDto;
import ru.yandex.practicum.event.dto.EventRequestDto;
import ru.yandex.practicum.event.service.EventServicePrivate;
import ru.yandex.practicum.util.OffsetPageable;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/user/{userId}/events")
@RequiredArgsConstructor
@Validated
public class EventControllerPrivate {
    final EventServicePrivate eventServicePrivate;

    @GetMapping
    public List<EventInfoDto> getByInitiatorIdFiltered(@PathVariable(name = "userId") long initiatorId,
                                                       @RequestParam(name = "from", defaultValue = "0") int offset,
                                                       @RequestParam(name = "size", defaultValue = "10") int limit) {
        log.info("GET \"/user/{}/events&from={}&size={}\"", initiatorId, offset, limit);
        Sort sort = Sort.by(Sort.Direction.DESC, "views");
        Pageable pageable = new OffsetPageable(offset, limit, sort);
        List<EventInfoDto> eventList = eventServicePrivate.getByInitiatorIdFiltered(initiatorId, pageable);
        log.debug("EventList = " + eventList);
        return eventList;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EventInfoDto create(@PathVariable(name = "userId") long initiatorId,
                               @RequestBody @Validated EventRequestDto eventRequestDto) {
        log.info("POST \"/user/{}/events\" Body={}", initiatorId, eventRequestDto);
        EventInfoDto eventToReturn = eventServicePrivate.create(initiatorId, eventRequestDto);
        return null;
    }

    @GetMapping("/{eventId}")
    public EventInfoDto getById(@PathVariable(name = "eventId") int eventId) {
        log.info("GET \"/events/{}", eventId);
        EventInfoDto event = eventServicePrivate.getById(eventId);
        log.debug("event = " + event);
        return event;
    }
}
