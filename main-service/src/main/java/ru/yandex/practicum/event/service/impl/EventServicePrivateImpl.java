package ru.yandex.practicum.event.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.StatisticClient;
import ru.yandex.practicum.category.Category;
import ru.yandex.practicum.category.CategoryRepository;
import ru.yandex.practicum.event.model.Event;
import ru.yandex.practicum.event.model.Location;
import ru.yandex.practicum.event.model.PublishState;
import ru.yandex.practicum.event.model.dto.EventCreateDto;
import ru.yandex.practicum.event.model.dto.EventFullInfoDto;
import ru.yandex.practicum.event.model.dto.EventShortInfoDto;
import ru.yandex.practicum.event.model.dto.EventUpdateDto;
import ru.yandex.practicum.event.model.mapper.EventMapper;
import ru.yandex.practicum.event.repository.EventRepository;
import ru.yandex.practicum.event.repository.LocationRepository;
import ru.yandex.practicum.event.service.EventServicePrivate;
import ru.yandex.practicum.eventRequest.EventRequestRepository;
import ru.yandex.practicum.eventRequest.model.*;
import ru.yandex.practicum.exception.NotFoundException;
import ru.yandex.practicum.user.User;
import ru.yandex.practicum.user.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
    public EventFullInfoDto create(long initiatorId, EventCreateDto eventCreateDto) {
        Location location = eventCreateDto.getLocation();
        long categoryId = eventCreateDto.getCategory();

        Location locationWithId = locationRepository.save(location);
        User initiator = userRepository.findById(initiatorId)
                .orElseThrow(() -> new NotFoundException("User with id=" + initiatorId + " was not found"));
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new NotFoundException("Category with id=" + categoryId + " was not found"));


        eventCreateDto.setLocation(locationWithId);
        Event eventToCreate = EventMapper.requestDtoToModel(eventCreateDto, category, initiator);
        Event eventCreated = eventRepository.save(eventToCreate);
        EventFullInfoDto eventFullInfoDto = EventMapper.toFullInfoDto(eventCreated);

        // eventFullInfoDto + views + (confirmedRequests and if limit reached -> rest requests rejected!)
        eventFullInfoDto.setConfirmedRequests(0);
        eventFullInfoDto.setViews(0);
        return eventFullInfoDto;
    }

    @Override
    public EventFullInfoDto updateAsInitiator(long initiatorId, long eventId, EventUpdateDto eventUpdateDto) {
        userRepository.findById(initiatorId)
                .orElseThrow(() -> new NotFoundException("User with id=" + initiatorId + " was not found"));
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Event with id=" + eventId + " was not found"));

        PublishState eventState = event.getState();
        if (eventState != PublishState.PENDING && eventState != PublishState.REJECTED && eventState != PublishState.CANCELED) {
            throw new DataIntegrityViolationException("Only pending, rejected or canceled events can be changed");
        }

        Long categoryId = eventUpdateDto.getCategory();
        Category category = null;
        if (categoryId != null) {
            category = categoryRepository.findById(categoryId)
                    .orElseThrow(() -> new NotFoundException("Category with id=" + categoryId + " was not found"));
        }

        Location location = eventUpdateDto.getLocation();
        if (location != null) {
            location.setLocationId(null);  // just in case user gave id (but we want it to be generated by DB)
            location = locationRepository.save(location);
        }

        PublishState.StateAction stateAction = eventUpdateDto.getStateAction();
        if (stateAction != null) {
            if (!(stateAction.equals(PublishState.StateAction.SEND_TO_REVIEW) && (eventState.equals(PublishState.REJECTED) /*|| eventState.equals(PublishState.CANCELED)*/)) &&
                    !(stateAction.equals(PublishState.StateAction.CANCEL_REVIEW) && eventState.equals(PublishState.PENDING))) {
                throw new DataIntegrityViolationException("Forbidden action: " + stateAction + ". Event is not in the right state: " + eventState.name());
            }
        }

        EventMapper.updateModelWithUpdateDtoNotNullFields(event, eventUpdateDto, category, location);
        Event eventUpdated = eventRepository.save(event);
        EventFullInfoDto eventFullInfoDto = EventMapper.toFullInfoDto(eventUpdated);

        long confirmedRequests = eventRequestRepository.countByEventIdAndStatus(eventId, EventRequestStatus.CONFIRMED);
        eventFullInfoDto.setConfirmedRequests(confirmedRequests);
        // setViews TODO
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
        setViewsToAll(eventShortInfoDtoList); TODO
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
        eventFullInfoDto.setViews(-1); /*TODO*/
        return eventFullInfoDto;
        // Обратите внимание:\n- событие должно быть опубликовано - информация о событии должна включать в себя
        // количество просмотров и количество подтвержденных запросов - информацию о том, что по этому эндпоинту был осуществлен
        // и обработан запрос, нужно сохранить в сервисе статистики В случае, если события с заданным id не найдено, возвращает статус код 404
    }

    @Override
    public List<EventRequestInfoDto> getEventRequestsByInitiatorIdAndEventId(long initiatorId, long eventId) {
        userRepository.findById(initiatorId)
                .orElseThrow(() -> new NotFoundException("User with id=" + initiatorId + " was not found"));
        List<EventRequest> eventRequestList = eventRequestRepository.findAllByEventInitiatorIdAndEventId(initiatorId, eventId);
        return EventRequestMapper.listModelToListInfoDto(eventRequestList);
    }

    @Override
    public EventRequestStatusResult updateEventRequestsAsInitiator(long initiatorId,
                                                                   long eventId,
                                                                   EventRequestStatusChanger eventRequestStatusChanger) {
        Event event = eventRepository.findByIdAndInitiatorId(eventId, initiatorId)
                .orElseThrow(() -> new NotFoundException("Event with id=" + eventId + " was not found for initiator with id=" + initiatorId));

        List<Long> eventRequestIdList = eventRequestStatusChanger.getRequestIds();
        List<EventRequest> eventRequestList = eventRequestRepository.findAllByIdInAndEventId(eventRequestIdList, eventId);
        long intendedToConfirmQuantity = eventRequestIdList.size();
        if (eventRequestList.size() < intendedToConfirmQuantity) {
            List<Long> missingRequestIdList = new ArrayList<>();
            List<Long> foundRequestIdList = eventRequestList.stream()
                    .map(EventRequest::getId)
                    .collect(Collectors.toList());
            for (long requestId : eventRequestIdList) {
                if (!foundRequestIdList.contains(requestId)) {
                    missingRequestIdList.add(requestId);
                }
            }
            throw new NotFoundException("Event requests with ids=" + missingRequestIdList + "not found for event with id=" + eventId);
        }

        long participantLimit = event.getParticipantLimit();
        long confirmedRequests = eventRequestRepository.countByEventIdAndStatus(eventId, EventRequestStatus.CONFIRMED);
        if (participantLimit == 0 || !event.isRequestModeration()) {
            // No action. Only return EventRequestStatusResult
            return new EventRequestStatusResult();
        }

        // participantLimit <= confirmedRequests не строго равно с заделом не ситуацию с багом,
        // когда confirmedRequests > participantLimit
        if (participantLimit <= confirmedRequests) {
            rejectAllPendingRequests();
            throw new DataIntegrityViolationException("Participant limit (" + participantLimit + ") is reached");
        }

        EventRequestStatus newStatus = eventRequestStatusChanger.getStatus();
        if (!EventRequestStatus.CONFIRMED.equals(newStatus) && !EventRequestStatus.REJECTED.equals(newStatus)) {
            throw new DataIntegrityViolationException("Not available new status: " + newStatus + ". " +
                    "New status can be only CONFIRMED or REJECTED.");
        }

        for (EventRequest request : eventRequestList) {
            List<Long> wrongStatusIdList = new ArrayList<>();
            EventRequestStatus status = request.getStatus();
            if (!EventRequestStatus.PENDING.equals(status)) {
                wrongStatusIdList.add(request.getId());
            }
            if (!wrongStatusIdList.isEmpty()) {
                throw new DataIntegrityViolationException("Not available action because requests with ids: " +
                        wrongStatusIdList + " are not with status \"PENDING\"");
            }

            request.setStatus(newStatus);
        }

        if (EventRequestStatus.CONFIRMED.equals(newStatus)) {
            long totalConfirmedRequestsQuantity = confirmedRequests + eventRequestIdList.size();
            if (participantLimit < totalConfirmedRequestsQuantity) {
                long freeSpaceQuantity = participantLimit - confirmedRequests;
                throw new DataIntegrityViolationException("Confirmation rejected. Free spaces: " + freeSpaceQuantity +
                        " but intended to confirm quantity: " + intendedToConfirmQuantity);

            } else if (participantLimit == totalConfirmedRequestsQuantity) {
                rejectAllPendingRequests();
            }
        }

        List<EventRequest> savedRequestList = eventRequestRepository.saveAll(eventRequestList);
        List<EventRequestInfoDto> savedRequestDtoList = EventRequestMapper.listModelToListInfoDto(savedRequestList);

        EventRequestStatusResult requestStatusResult = new EventRequestStatusResult();
        if (EventRequestStatus.CONFIRMED.equals(newStatus)) {
            requestStatusResult.addAllConfirmed(savedRequestDtoList);
        } else {
            requestStatusResult.addAllRejected(savedRequestDtoList);
        }
        /*List<EventRequestStatus> confirmAndRejectStatusList = List.of(EventRequestStatus.CONFIRMED, EventRequestStatus.REJECTED);
        List<EventRequest> confirmedAndRejectedRequestList = eventRequestRepository.findAllByStatusIn(confirmAndRejectStatusList);
        List<EventRequestInfoDto> confirmedAndRejectedRequestDtoList = EventRequestMapper.listModelToListInfoDto(confirmedAndRejectedRequestList);

        EventRequestStatusResult eventRequestStatusResult = new EventRequestStatusResult();
        for (EventRequestInfoDto dto : confirmedAndRejectedRequestDtoList) {
            if (dto.getStatus().equals(EventRequestStatus.REJECTED)) {
                eventRequestStatusResult.addRejected(dto);
            } else {
                eventRequestStatusResult.addConfirmed(dto);
            }
        }*/

        return requestStatusResult;
    }

    private void rejectAllPendingRequests() {
        List<EventRequest> requests = eventRequestRepository.findAllByStatus(EventRequestStatus.PENDING);
        for (EventRequest request : requests) {
            request.setStatus(EventRequestStatus.REJECTED);
        }
        eventRequestRepository.saveAll(requests);
    }
}