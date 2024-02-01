package ru.yandex.practicum.event.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.StatisticClient;
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

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EventServicePublicImpl implements EventServicePublic {
    final EventRepository eventRepository;
    final EventRequestRepository eventRequestRepository;
    final StatisticClient statisticClient;

    @Override
    public List<EventShortInfoDto> getFiltered(String text, List<Long> categories, boolean paid, LocalDateTime rangeStart,
                                               LocalDateTime rangeEnd, boolean onlyAvailable, Pageable pageable, Sort sort) {
        List<Event> eventList;
        if (rangeStart == null || rangeEnd == null) {
            if (onlyAvailable) {
                eventList = eventRepository.findAllAvailableFilteredStartingFromToday(text, categories, paid, pageable);
            } else {
                eventList = eventRepository.findAllFilteredStartingFromToday(text, categories, paid, pageable);
            }
        } else {
            if (onlyAvailable) {
                eventList = eventRepository.findAllAvailableFiltered(text, categories, paid, rangeStart, rangeEnd, pageable);
            } else {
                eventList = eventRepository.findAllFiltered(text, categories, paid, rangeStart, rangeEnd, pageable);
            }
        }

        // eventList + confirmedRequests + views
        // + sorting as requested
        return EventMapper.modelListToShortInfoDtoList(eventList);

        //Обратите внимание: \n- это публичный эндпоинт, соответственно в выдаче должны быть только опубликованные события
        // - текстовый поиск (по аннотации и подробному описанию) должен быть без учета регистра букв
        // - если в запросе не указан диапазон дат [rangeStart-rangeEnd], то нужно выгружать события,
        // которые произойдут позже текущей даты и времени - информация о каждом событии должна включать в себя количество просмотров
        // и количество уже одобренных заявок на участие - информацию о том, что по этому эндпоинту был осуществлен и обработан запрос,
        // нужно сохранить в сервисе статистики В случае, если по заданным фильтрам не найдено ни одного события, возвращает пустой список
    }

    @Override
    public EventFullInfoDto getPublishedById(int eventId, String address, String uri) {
        Event event = eventRepository.findByIdAndState(eventId, PublishState.PUBLISHED)
                .orElseThrow(() -> new NotFoundException("Event with id=" + eventId + " was not found"));
        EventFullInfoDto eventFullInfoDto = EventMapper.toFullInfoDto(event);
        eventFullInfoDto.setConfirmedRequests(eventRequestRepository.countByEventIdAndStatus(eventId, EventRequestStatus.CONFIRMED));
        //  statistic recording TODO
        return eventFullInfoDto;
        // Обратите внимание:
        // - событие должно быть опубликовано.
        // - информация о событии должна включать в себя количество просмотров и количество подтвержденных запросов.
        // - информацию о том, что по этому эндпоинту был осуществлен и обработан запрос, нужно сохранить в сервисе статистики.
        // В случае, если события с заданным id не найдено, возвращает статус код 404
    }
}
