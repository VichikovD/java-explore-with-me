package ru.yandex.practicum.event.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.event.model.dto.EventFullInfoDto;
import ru.yandex.practicum.event.model.dto.EventRequestAdminDto;
import ru.yandex.practicum.event.service.EventServiceAdmin;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/events")
public class EventControllerAdmin {
    final EventServiceAdmin eventServiceAdmin;

    @PatchMapping("/{eventId}")
    public EventFullInfoDto updateAsAdmin(@PathVariable(name = "eventId") long eventId,
                                          @RequestBody EventRequestAdminDto eventRequestDto) {
        log.info("PATCH \"/admin/events/{}\" Body={}", eventId, eventRequestDto);
        EventFullInfoDto eventFullInfoDto = eventServiceAdmin.updateAsAdmin(eventId, eventRequestDto);
        log.debug("EventFullInfoDtoList=" + eventFullInfoDto);
        return eventFullInfoDto;
    }

    // список id пользователей, чьи события нужно найти
    // список состояний в которых находятся искомые события
    // список id категорий в которых будет вестись поиск
    // дата и время не раньше которых должно произойти событие
    // дата и время не позже которых должно произойти  событие
    // количество событий, которые нужно пропустить для формирования текущего набора
    @GetMapping
    public List<EventFullInfoDto> getFullFiltered(@RequestParam(name = "users") int[] users,
                                                  @RequestParam(name = "states") String[] states,
                                                  @RequestParam(name = "categories") int[] categories,
                                                  @RequestParam(name = "rangeStart") LocalDateTime rangeStart,
                                                  @RequestParam(name = "rangeEnd") LocalDateTime rangeEnd,
                                                  @RequestParam(name = "from", defaultValue = "0") int offset,
                                                  @RequestParam(name = "size", defaultValue = "10") int limit) {
        log.info("GET \"/admin/events?users={}&={}&={}&={}&={}&={}&={}\"",
                users, states, categories, rangeStart, rangeEnd, offset, limit);
        List<EventFullInfoDto> eventFullInfoDtoList = eventServiceAdmin.getFullFiltered();
        log.debug("EventFullInfoDtoList=" + eventFullInfoDtoList);
        return eventFullInfoDtoList;
    }
}
