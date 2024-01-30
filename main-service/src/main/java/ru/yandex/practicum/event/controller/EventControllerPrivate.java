package ru.yandex.practicum.event.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.event.model.dto.EventFullInfoDto;
import ru.yandex.practicum.event.model.dto.EventRequestDto;
import ru.yandex.practicum.event.model.dto.EventShortInfoDto;
import ru.yandex.practicum.event.service.EventServicePrivate;
import ru.yandex.practicum.util.OffsetPageable;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/users/{userId}/events")
@RequiredArgsConstructor
@Validated
public class EventControllerPrivate {
    final EventServicePrivate eventServicePrivate;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EventFullInfoDto create(@PathVariable(name = "userId") long initiatorId,
                                   @RequestBody @Validated EventRequestDto eventRequestDto) {
        log.info("POST \"/user/{}/events\" Body={}", initiatorId, eventRequestDto);
        EventFullInfoDto eventToReturn = eventServicePrivate.create(initiatorId, eventRequestDto);
        log.debug("Event=" + eventToReturn);
        return eventToReturn;
    }

    @GetMapping
    public List<EventShortInfoDto> getByInitiatorIdFiltered(@PathVariable(name = "userId") long initiatorId,
                                                            @RequestParam(name = "from", defaultValue = "0") int offset,
                                                            @RequestParam(name = "size", defaultValue = "10") int limit) {
        log.info("GET \"/user/{}/events&from={}&size={}\"", initiatorId, offset, limit);
        Pageable pageable = new OffsetPageable(offset, limit);
        List<EventShortInfoDto> eventList = eventServicePrivate.getByInitiatorIdFiltered(initiatorId, pageable);
        log.debug("EventList=" + eventList);
        return eventList;
    }

    @GetMapping("/{eventId}")
    public EventFullInfoDto getByIdAndInitiatorId(@PathVariable(name = "userId") long initiatorId,
                                                  @PathVariable(name = "eventId") long eventId) {
        log.info("GET \"/user/{}/events/{}", initiatorId, eventId);
        EventFullInfoDto event = eventServicePrivate.getByIdAndInitiatorId(initiatorId, eventId);
        log.debug("event=" + event);
        return event;
    }
}
