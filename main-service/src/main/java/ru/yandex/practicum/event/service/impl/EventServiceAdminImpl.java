package ru.yandex.practicum.event.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.category.Category;
import ru.yandex.practicum.category.CategoryRepository;
import ru.yandex.practicum.event.model.Event;
import ru.yandex.practicum.event.model.Location;
import ru.yandex.practicum.event.model.PublishState;
import ru.yandex.practicum.event.model.dto.EventFullInfoDto;
import ru.yandex.practicum.event.model.dto.EventRequestAdminDto;
import ru.yandex.practicum.event.model.mapper.EventMapper;
import ru.yandex.practicum.event.repository.EventRepository;
import ru.yandex.practicum.event.repository.LocationRepository;
import ru.yandex.practicum.event.service.EventServiceAdmin;
import ru.yandex.practicum.eventRequest.EventRequestRepository;
import ru.yandex.practicum.eventRequest.model.EventRequestStatus;
import ru.yandex.practicum.exception.NotFoundException;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EventServiceAdminImpl implements EventServiceAdmin {
    final EventRepository eventRepository;
    final CategoryRepository categoryRepository;
    final LocationRepository locationRepository;
    final EventRequestRepository eventRequestRepository;

    @Override
    public EventFullInfoDto updateAsAdmin(long eventId, EventRequestAdminDto eventRequestDto) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Event with id=" + eventId + " was not found"));

        Long categoryId = eventRequestDto.getCategory();
        Category category = null;
        if (categoryId != null) {
            category = categoryRepository.findById(categoryId)
                    .orElseThrow(() -> new NotFoundException("Category with id=" + categoryId + " was not found"));
        }

        LocalDateTime eventDate = eventRequestDto.getEventDate();
        if (eventDate != null) {
            if (eventDate.isBefore(LocalDateTime.now().plusHours(2))) {
                throw new DataIntegrityViolationException("Field: eventDate. Error: Must contain time at least 2 hours ahead of now");
            }
        }

        Location location = eventRequestDto.getLocation();
        if (location != null) {  // а если location.getLocationId() == null запрос просто не найдет Location?
            location = locationRepository.save(location);
        }

        PublishState.StateAction stateAction = eventRequestDto.getStateAction();
        if (stateAction != null) {
            PublishState actualState = event.getState();
            if (stateAction.equals(PublishState.StateAction.PUBLISH_EVENT)) {
                if (!actualState.equals(PublishState.PENDING)) {
                    throw new DataIntegrityViolationException("Cannot publish the event because it's not in the right state: " + actualState.name());
                }
            } else if (stateAction.equals(PublishState.StateAction.REJECT_EVENT)) {
                if (actualState.equals(PublishState.PUBLISHED)) {
                    throw new DataIntegrityViolationException("Cannot reject the event because it's not in the right state: " + actualState.name());
                }
            }
        }

        EventMapper.updateModelWithRequestAdminDtoNotNullFields(event, eventRequestDto, category, location);
        Event eventUpdated = eventRepository.save(event);
        EventFullInfoDto eventFullInfoDto = EventMapper.toFullInfoDto(eventUpdated);

        long confirmedRequests = eventRequestRepository.countByEventIdAndStatus(eventId, EventRequestStatus.CONFIRMED);
        eventFullInfoDto.setConfirmedRequests(confirmedRequests);
        // setViews TO DO
        return eventFullInfoDto;
    }

    @Override
    public List<EventFullInfoDto> findAllFilteredAsAdmin(List<Long> users, List<PublishState> states, List<Long> categories,
                                                         LocalDateTime rangeStart, LocalDateTime rangeEnd, Pageable pageable) {
        List<Event> eventList = eventRepository.findAllFilteredAsAdmin(users, states, categories, rangeStart, rangeEnd, pageable);
        List<EventFullInfoDto> eventFullInfoDtoList = EventMapper.modelListToFullInfoDtoList(eventList);
        // TODO eventFullInfoDtoList + confirmedRequests + views TODO
        return eventFullInfoDtoList;
    }
}
