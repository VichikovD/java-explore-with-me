package ru.yandex.practicum.event.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.StatisticClient;
import ru.yandex.practicum.category.Category;
import ru.yandex.practicum.category.CategoryRepository;
import ru.yandex.practicum.event.model.Event;
import ru.yandex.practicum.event.model.Location;
import ru.yandex.practicum.event.model.dto.EventFullInfoDto;
import ru.yandex.practicum.event.model.dto.EventRequestDto;
import ru.yandex.practicum.event.model.dto.EventShortInfoDto;
import ru.yandex.practicum.event.model.mapper.EventMapper;
import ru.yandex.practicum.event.repository.EventRepository;
import ru.yandex.practicum.event.repository.LocationRepository;
import ru.yandex.practicum.event.service.EventServicePrivate;
import ru.yandex.practicum.eventRequest.EventRequestRepository;
import ru.yandex.practicum.eventRequest.model.EventRequestStatus;
import ru.yandex.practicum.exception.NotFoundException;
import ru.yandex.practicum.user.User;
import ru.yandex.practicum.user.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EventServicePrivateImpl implements EventServicePrivate {
    final EventRepository eventRepository;
    final EventRequestRepository eventRequestRepository;
    final UserRepository userRepository;
    final CategoryRepository categoryRepository;
    final LocationRepository locationRepository;
    final StatisticClient statisticClient;

    @Override
    public EventFullInfoDto create(long initiatorId, EventRequestDto eventRequestDto) {
        Location location = eventRequestDto.getLocation();
        long categoryId = eventRequestDto.getCategory();

        Location locationWithId = locationRepository.save(location);
        User initiator = userRepository.findById(initiatorId)
                .orElseThrow(() -> new NotFoundException("User with id=" + initiatorId + " was not found"));
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new NotFoundException("Category with id=" + categoryId + " was not found"));


        eventRequestDto.setLocation(locationWithId);
        Event eventToCreate = EventMapper.requestDtoToModel(eventRequestDto, category, initiator);
        Event eventCreated = eventRepository.save(eventToCreate);
        EventFullInfoDto eventFullInfoDto = EventMapper.toFullInfoDto(eventCreated);

        // eventFullInfoDto + views + (confirmedRequests and if limit reached -> rest requests rejected!)
        eventFullInfoDto.setConfirmedRequests(0);
        eventFullInfoDto.setViews(0);
        return eventFullInfoDto;
    }

    @Override
    public List<EventShortInfoDto> getByInitiatorIdFiltered(long initiatorId, Pageable pageable) {
        // В спецификации написано только "В случае, если по заданным фильтрам не найдено ни одного события, возвращает пустой список"
        // про валидацию id не написано, но мне кажется, она здесь нужна
        userRepository.findById(initiatorId)
                .orElseThrow(() -> new NotFoundException("User with id=" + initiatorId + " was not found"));

        List<Event> eventList = eventRepository.findAllByInitiatorId(initiatorId, pageable);
        List<EventShortInfoDto> eventShortInfoDtoList = EventMapper.modelListToShortInfoDtoList(eventList);
        // Всем событиям добавить просмотры и подтвержденные запросы на участие \/
        /*
        setConfirmedRequestsToAll(eventShortInfoDtoList);
        setViewsToAll(eventShortInfoDtoList);
        */
        return eventShortInfoDtoList;
    }

    @Override
    public EventFullInfoDto getByIdAndInitiatorId(long initiatorId, long eventId) {
        Event event = eventRepository.findByIdAndInitiatorId(eventId, initiatorId)
                .orElseThrow(() -> new NotFoundException("Event with id=" + eventId + " was not found for initiator with id=" + initiatorId));
        EventFullInfoDto eventFullInfoDto = EventMapper.toFullInfoDto(event);

        long confirmedRequests = eventRequestRepository.countByEventIdAndStatus(eventId, EventRequestStatus.CONFIRMED);
        eventFullInfoDto.setConfirmedRequests(confirmedRequests);
        eventFullInfoDto.setViews(-1);
        return eventFullInfoDto;
        // Обратите внимание:\n- событие должно быть опубликовано - информация о событии должна включать в себя
        // количество просмотров и количество подтвержденных запросов - информацию о том, что по этому эндпоинту был осуществлен
        // и обработан запрос, нужно сохранить в сервисе статистики В случае, если события с заданным id не найдено, возвращает статус код 404
    }
}
