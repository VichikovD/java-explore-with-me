package ru.yandex.practicum.event.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.event.model.dto.EventCreateDto;
import ru.yandex.practicum.event.model.dto.EventFullInfoDto;
import ru.yandex.practicum.event.model.dto.EventShortInfoDto;
import ru.yandex.practicum.event.model.dto.EventUpdateDto;
import ru.yandex.practicum.event.service.EventService;
import ru.yandex.practicum.eventRequest.model.EventRequestInfoDto;
import ru.yandex.practicum.eventRequest.model.EventRequestStatusChanger;
import ru.yandex.practicum.eventRequest.model.EventRequestStatusResult;
import ru.yandex.practicum.util.OffsetPageable;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/users/{userId}/events")
@RequiredArgsConstructor
@Validated
public class EventPrivateController {
    final EventService eventService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EventFullInfoDto create(@PathVariable(name = "userId") long initiatorId,
                                   @RequestBody @Validated EventCreateDto eventCreateDto) {
        log.info("POST \"/user/{}/events\" Body={}", initiatorId, eventCreateDto);
        EventFullInfoDto eventToReturn = eventService.create(initiatorId, eventCreateDto);
        log.debug("Event created=" + eventToReturn);
        return eventToReturn;
    }

    @PatchMapping("/{eventId}")
    public EventFullInfoDto update(@PathVariable(name = "userId") long initiatorId,
                                   @PathVariable(name = "eventId") long eventId,
                                   @RequestBody @Validated EventUpdateDto eventUpdateDto) {
        log.info("PATCH \"/user/{}/events/{}\" Body={}", initiatorId, eventId, eventUpdateDto);
        EventFullInfoDto eventFullInfoDto = eventService.update(initiatorId, eventId, eventUpdateDto);
        log.debug("Event updated=" + eventFullInfoDto);
        return eventFullInfoDto;
    }

    @GetMapping
    public List<EventShortInfoDto> getByInitiatorIdFiltered(@PathVariable(name = "userId") long initiatorId,
                                                            @RequestParam(name = "from", defaultValue = "0") int offset,
                                                            @RequestParam(name = "size", defaultValue = "10") int limit) {
        log.info("GET \"/user/{}/events&from={}&size={}\"", initiatorId, offset, limit);
        Pageable pageable = new OffsetPageable(offset, limit);
        List<EventShortInfoDto> eventList = eventService.getByInitiatorIdFiltered(initiatorId, pageable);
        log.debug("EventList found=" + eventList);
        return eventList;
    }

    @GetMapping("/{eventId}")
    public EventFullInfoDto getByIdAndInitiatorId(@PathVariable(name = "userId") long initiatorId,
                                                  @PathVariable(name = "eventId") long eventId) {
        log.info("GET \"/user/{}/events/{}", initiatorId, eventId);
        EventFullInfoDto event = eventService.getByIdAndInitiatorId(initiatorId, eventId);
        log.debug("Event found=" + event);
        return event;
    }

    @GetMapping("/{eventId}/requests")
    public List<EventRequestInfoDto> getEventRequestsByInitiatorIdAndEventId(@PathVariable(name = "userId") long initiatorId,
                                                                             @PathVariable(name = "eventId") long eventId) {
        log.info("GET \"/user/{}/events/{}/requests", initiatorId, eventId);
        List<EventRequestInfoDto> eventRequestList = eventService.getEventRequestsByInitiatorIdAndEventId(initiatorId, eventId);
        log.debug("EventRequestList found=" + eventRequestList);
        return eventRequestList;
    }


    @PatchMapping("/{eventId}/requests")
    public EventRequestStatusResult updateEventRequests(@PathVariable(name = "userId") long initiatorId,
                                                        @PathVariable(name = "eventId") long eventId,
                                                        @RequestBody EventRequestStatusChanger eventRequestStatusChanger) {
        log.info("PATCH \"/user/{}/events/{}/requests Body={}", initiatorId, eventId, eventRequestStatusChanger);
        EventRequestStatusResult eventRequestList = eventService.updateEventRequests(initiatorId, eventId, eventRequestStatusChanger);
        log.debug("EventRequestList found=" + eventRequestList);
        return eventRequestList;
    }
}
