package ru.yandex.practicum.eventRequest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.eventRequest.model.EventRequestInfoDto;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/users/{userId}/requests")
public class EventRequestController {
    final EventRequestService eventRequestService;

    @PostMapping
    public EventRequestInfoDto create(@PathVariable(name = "userId") long requesterId,
                                      @RequestParam(name = "eventId") long eventId) {
        log.info("POST \"/users/{}/requests\" eventId={}", requesterId, eventId);
        EventRequestInfoDto eventRequestInfoDto = eventRequestService.create(requesterId, eventId);
        log.debug("EventRequestInfoDto=" + eventRequestInfoDto);
        return eventRequestInfoDto;
    }

    @GetMapping
    public List<EventRequestInfoDto> getAllByRequesterId(@PathVariable(name = "userId") long requesterId) {
        log.info("GET \"/users/{}/requests\"", requesterId);
        List<EventRequestInfoDto> eventRequestInfoDtoList = eventRequestService.getAllByRequesterId(requesterId);
        log.debug("EventRequestInfoDtoList=" + eventRequestInfoDtoList);
        return eventRequestInfoDtoList;
    }
}
