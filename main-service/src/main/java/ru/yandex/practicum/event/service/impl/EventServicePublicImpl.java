package ru.yandex.practicum.event.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.event.model.Event;
import ru.yandex.practicum.event.model.PublishState;
import ru.yandex.practicum.event.model.dto.EventFullInfoDto;
import ru.yandex.practicum.event.model.dto.EventShortInfoDto;
import ru.yandex.practicum.event.model.mapper.EventMapper;
import ru.yandex.practicum.event.repository.EventRepository;
import ru.yandex.practicum.event.service.EventServicePublic;
import ru.yandex.practicum.eventRequest.EventRequestRepository;
import ru.yandex.practicum.eventRequest.model.EventRequestStatus;
import ru.yandex.practicum.exception.NotFoundException;
import ru.yandex.practicum.util.EventRequestsManager;
import ru.yandex.practicum.util.StatisticsManager;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EventServicePublicImpl implements EventServicePublic {
    final EventRepository eventRepository;
    final EventRequestRepository eventRequestRepository;
    final StatisticsManager statisticsManager;
    final EventRequestsManager eventRequestsManager;

    @Override
    public List<EventShortInfoDto> getFiltered(String text, List<Long> categories, Boolean paid, LocalDateTime rangeStart,
                                               LocalDateTime rangeEnd, boolean onlyAvailable, Pageable pageable, Sort sort,
                                               HttpServletRequest request) {
        validateStartAndEndTime(rangeStart, rangeEnd);

        List<Event> eventList = eventRepository.findAllFilteredAsUser(text, categories, paid, rangeStart, rangeEnd, pageable);
        List<EventShortInfoDto> eventShortInfoDtoList = EventMapper.modelListToShortInfoDtoList(eventList);

        eventRequestsManager.updateConfirmedRequestsToShortDtos(eventShortInfoDtoList);

        if (onlyAvailable) {
            eventShortInfoDtoList = eventShortInfoDtoList.stream()
                    .filter((eventShortInfoDto -> eventShortInfoDto.getParticipantLimit() > eventShortInfoDto.getConfirmedRequests()))
                    .collect(Collectors.toList());
        }

        statisticsManager.updateViewsToShortInfoDtos(eventShortInfoDtoList);
        statisticsManager.sendStatistic(request);
        return eventShortInfoDtoList;

        //Обратите внимание: \n- это публичный эндпоинт, соответственно в выдаче должны быть только опубликованные события
        // - текстовый поиск (по аннотации и подробному описанию) должен быть без учета регистра букв
        // - если в запросе не указан диапазон дат [rangeStart-rangeEnd], то нужно выгружать события,
        // которые произойдут позже текущей даты и времени - информация о каждом событии должна включать в себя количество просмотров
        // и количество уже одобренных заявок на участие - информацию о том, что по этому эндпоинту был осуществлен и обработан запрос,
        // нужно сохранить в сервисе статистики В случае, если по заданным фильтрам не найдено ни одного события, возвращает пустой список
    }

    @Override
    public EventFullInfoDto getPublishedById(int eventId, HttpServletRequest request) {
        Event event = eventRepository.findByIdAndState(eventId, PublishState.PUBLISHED)
                .orElseThrow(() -> new NotFoundException("Event with id=" + eventId + " was not found"));
        EventFullInfoDto eventFullInfoDto = EventMapper.toFullInfoDto(event);
        eventFullInfoDto.setConfirmedRequests(eventRequestRepository.countByEventIdAndStatus(eventId, EventRequestStatus.CONFIRMED));
        statisticsManager.updateViewsToFullInfoDtos(List.of(eventFullInfoDto));
        statisticsManager.sendStatistic(request);
        return eventFullInfoDto;
    }

    private void validateStartAndEndTime(LocalDateTime start, LocalDateTime end) {
        if (start != null && end != null) {
            if (start.isAfter(end)) {
                throw new IllegalArgumentException("Start can't be after end");
            }
        }
    }
}
