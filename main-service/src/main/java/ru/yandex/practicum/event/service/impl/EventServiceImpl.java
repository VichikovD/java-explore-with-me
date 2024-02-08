package ru.yandex.practicum.event.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.StatisticClient;
import ru.yandex.practicum.StatisticInfo;
import ru.yandex.practicum.category.Category;
import ru.yandex.practicum.category.CategoryRepository;
import ru.yandex.practicum.dto.StatisticFilterDto;
import ru.yandex.practicum.event.model.Event;
import ru.yandex.practicum.event.model.Location;
import ru.yandex.practicum.event.model.PublishState;
import ru.yandex.practicum.event.model.dto.*;
import ru.yandex.practicum.event.model.mapper.EventMapper;
import ru.yandex.practicum.event.repository.EventRepository;
import ru.yandex.practicum.event.repository.LocationRepository;
import ru.yandex.practicum.event.service.EventService;
import ru.yandex.practicum.eventRequest.EventRequestRepository;
import ru.yandex.practicum.eventRequest.model.*;
import ru.yandex.practicum.exception.NotFoundException;
import ru.yandex.practicum.user.User;
import ru.yandex.practicum.user.UserRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {
    final EventRepository eventRepository;
    final UserRepository userRepository;
    final CategoryRepository categoryRepository;
    final LocationRepository locationRepository;
    final EventRequestRepository eventRequestRepository;
    final StatisticClient statisticClient;
    static final String APPLICATION_NAME = "ewm-main-service";
    final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");


    @Override
    public EventFullInfoDto updateWithoutValidation(long eventId, EventRequestAdminDto eventRequestDto) {
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
        if (location != null) {
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
        event.setPublishedOn(LocalDateTime.now());

        EventMapper.updateModelWithUpdateDtoNotNullFields(event, eventRequestDto, category, location);
        Event eventUpdated = eventRepository.save(event);
        EventFullInfoDto eventFullInfoDto = EventMapper.toFullInfoDto(eventUpdated);

        long confirmedRequests = eventRequestRepository.countByEventIdAndStatus(eventId, EventRequestStatus.CONFIRMED);
        eventFullInfoDto.setConfirmedRequests(confirmedRequests);

        Map<Long, Long> eventIdToViews = getViewsMapForFullDtos(List.of(eventFullInfoDto));
        EventMapper.updateViewsToFullDtos(List.of(eventFullInfoDto), eventIdToViews);

        return eventFullInfoDto;
    }

    @Override
    public List<EventFullInfoDto> findFullDtosFiltered(List<Long> users, List<PublishState> states, List<Long> categories,
                                                       LocalDateTime rangeStart, LocalDateTime rangeEnd, Pageable pageable) {
        List<Event> eventList = eventRepository.findFilteredAsAdmin(users, states, categories, rangeStart, rangeEnd, pageable);
        List<EventFullInfoDto> eventFullInfoDtoList = EventMapper.modelListToFullInfoDtoList(eventList);


        Map<Long, Long> eventIdToConfirmedRequests = getRequestsMapForFullDtos(eventFullInfoDtoList);
        EventMapper.updateConfirmedRequestsToFullDtos(eventFullInfoDtoList, eventIdToConfirmedRequests);

        Map<Long, Long> eventIdToViews = getViewsMapForFullDtos(eventFullInfoDtoList);
        EventMapper.updateViewsToFullDtos(eventFullInfoDtoList, eventIdToViews);

        return eventFullInfoDtoList;
    }

    @Override
    public List<EventShortInfoDto> findShortDtosFiltered(String text, List<Long> categories, Boolean paid,
                                                         LocalDateTime rangeStart, LocalDateTime rangeEnd,
                                                         boolean onlyAvailable, Pageable pageable, Sort sort,
                                                         String address, String uri) {

        List<Event> eventList = eventRepository.findFilteredAsUser(text, categories, paid, rangeStart, rangeEnd, pageable);
        List<EventShortInfoDto> eventShortInfoDtoList = EventMapper.modelListToShortInfoDtoList(eventList);

        if (onlyAvailable) {
            eventShortInfoDtoList = eventShortInfoDtoList.stream()
                    .filter((eventShortInfoDto -> eventShortInfoDto.getParticipantLimit() > eventShortInfoDto.getConfirmedRequests()))
                    .collect(Collectors.toList());
        }

        Map<Long, Long> eventIdToConfirmedRequests = getRequestsMapForShortDtos(eventShortInfoDtoList);
        EventMapper.updateConfirmedRequestsToShortDtos(eventShortInfoDtoList, eventIdToConfirmedRequests);

        Map<Long, Long> eventIdToViews = getViewsMapForShortDtos(eventShortInfoDtoList);
        EventMapper.updateViewsToShortDtos(eventShortInfoDtoList, eventIdToViews);

        return eventShortInfoDtoList;
    }

    @Override
    public EventFullInfoDto getPublishedById(int eventId, String address, String uri) {
        Event event = eventRepository.findByIdAndState(eventId, PublishState.PUBLISHED)
                .orElseThrow(() -> new NotFoundException("Event with id=" + eventId + " was not found"));
        EventFullInfoDto eventFullInfoDto = EventMapper.toFullInfoDto(event);

        eventFullInfoDto.setConfirmedRequests(eventRequestRepository.countByEventIdAndStatus(eventId, EventRequestStatus.CONFIRMED));

        Map<Long, Long> eventIdToViews = getViewsMapForFullDtos(List.of(eventFullInfoDto));
        EventMapper.updateViewsToFullDtos(List.of(eventFullInfoDto), eventIdToViews);

        return eventFullInfoDto;
    }

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

        eventFullInfoDto.setConfirmedRequests(0);
        eventFullInfoDto.setViews(0);
        return eventFullInfoDto;
    }

    @Override
    public EventFullInfoDto update(long initiatorId, long eventId, EventUpdateDto eventUpdateDto) {
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

        Map<Long, Long> eventIdToViews = getViewsMapForFullDtos(List.of(eventFullInfoDto));
        EventMapper.updateViewsToFullDtos(List.of(eventFullInfoDto), eventIdToViews);

        return eventFullInfoDto;
    }

    @Override
    public List<EventShortInfoDto> getByInitiatorIdFiltered(long initiatorId, Pageable pageable) {
        // В спецификации написано только "В случае, если по заданным фильтрам не найдено ни одного события, возвращает пустой список"
        // про валидацию id не написано, но мне кажется, она здесь нужна
        userRepository.findById(initiatorId)
                .orElseThrow(() -> new NotFoundException("User with id=" + initiatorId + " was not found"));

        List<Event> eventList = eventRepository.findByInitiatorId(initiatorId, pageable);
        List<EventShortInfoDto> eventShortInfoDtoList = EventMapper.modelListToShortInfoDtoList(eventList);

        Map<Long, Long> eventIdToConfirmedRequests = getRequestsMapForShortDtos(eventShortInfoDtoList);
        EventMapper.updateConfirmedRequestsToShortDtos(eventShortInfoDtoList, eventIdToConfirmedRequests);

        Map<Long, Long> eventIdToViews = getViewsMapForShortDtos(eventShortInfoDtoList);
        EventMapper.updateViewsToShortDtos(eventShortInfoDtoList, eventIdToViews);

        return eventShortInfoDtoList;
    }

    @Override
    public EventFullInfoDto getByIdAndInitiatorId(long initiatorId, long eventId) {
        Event event = eventRepository.findByIdAndInitiatorId(eventId, initiatorId)
                .orElseThrow(() -> new NotFoundException("Event with id=" + eventId + " was not found for initiator with id=" + initiatorId));
        EventFullInfoDto eventFullInfoDto = EventMapper.toFullInfoDto(event);

        long confirmedRequests = eventRequestRepository.countByEventIdAndStatus(eventId, EventRequestStatus.CONFIRMED);
        eventFullInfoDto.setConfirmedRequests(confirmedRequests);

        Map<Long, Long> eventIdToViews = getViewsMapForFullDtos(List.of(eventFullInfoDto));
        EventMapper.updateViewsToFullDtos(List.of(eventFullInfoDto), eventIdToViews);

        return eventFullInfoDto;
    }

    @Override
    public List<EventRequestInfoDto> getEventRequestsByInitiatorIdAndEventId(long initiatorId, long eventId) {
        userRepository.findById(initiatorId)
                .orElseThrow(() -> new NotFoundException("User with id=" + initiatorId + " was not found"));
        List<EventRequest> eventRequestList = eventRequestRepository.findByEventInitiatorIdAndEventId(initiatorId, eventId);
        return EventRequestMapper.listModelToListInfoDto(eventRequestList);
    }

    @Override
    public EventRequestStatusResult updateEventRequests(long initiatorId,
                                                        long eventId,
                                                        EventRequestStatusChanger eventRequestStatusChanger) {
        Event event = eventRepository.findByIdAndInitiatorId(eventId, initiatorId)
                .orElseThrow(() -> new NotFoundException("Event with id=" + eventId + " was not found for initiator with id=" + initiatorId));

        List<Long> eventRequestIdList = eventRequestStatusChanger.getRequestIds();
        List<EventRequest> eventRequestList = eventRequestRepository.findByIdInAndEventId(eventRequestIdList, eventId);
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

        // keep in mind that totalConfirmedRequestsQuantity works only if newStatus == confirmed
        long totalConfirmedRequestsQuantity = confirmedRequests + eventRequestIdList.size();
        if (EventRequestStatus.CONFIRMED.equals(newStatus)) {
            if (participantLimit < totalConfirmedRequestsQuantity) {
                long freeSpaceQuantity = participantLimit - confirmedRequests;
                throw new DataIntegrityViolationException("Confirmation rejected. Free spaces: " + freeSpaceQuantity +
                        " but intended to confirm quantity: " + intendedToConfirmQuantity);
            }
        }

        List<EventRequest> savedRequestList = eventRequestRepository.saveAll(eventRequestList);
        List<EventRequestInfoDto> savedRequestDtoList = EventRequestMapper.listModelToListInfoDto(savedRequestList);

        EventRequestStatusResult requestStatusResult = new EventRequestStatusResult();
        if (EventRequestStatus.CONFIRMED.equals(newStatus)) {
            requestStatusResult.addAllConfirmed(savedRequestDtoList);

            if (participantLimit == totalConfirmedRequestsQuantity) {
                rejectAllPendingRequests();
            }
        } else {
            requestStatusResult.addAllRejected(savedRequestDtoList);
        }
        return requestStatusResult;
    }

    private void rejectAllPendingRequests() {
        List<EventRequest> requests = eventRequestRepository.findByStatus(EventRequestStatus.PENDING);
        for (EventRequest request : requests) {
            request.setStatus(EventRequestStatus.REJECTED);
        }
        eventRequestRepository.saveAll(requests);
    }

    private Map<Long, Long> getViewsMapForShortDtos(Collection<EventShortInfoDto> eventCollection) {
        List<Long> eventIdList = eventCollection.stream()
                .map(EventShortInfoDto::getId)
                .collect(Collectors.toList());

        return getIdToViewsMapByIdCollection(eventIdList);
    }

    private Map<Long, Long> getViewsMapForFullDtos(Collection<EventFullInfoDto> eventCollection) {
        List<Long> eventIdList = eventCollection.stream()
                .map((EventFullInfoDto::getId))
                .collect(Collectors.toList());

        return getIdToViewsMapByIdCollection(eventIdList);
    }

    private Map<Long, Long> getIdToViewsMapByIdCollection(Collection<Long> eventIdCollection) {
        log.debug("eventIdCollection to send=" + eventIdCollection);
        ArrayList<String> uris = eventIdCollection.stream()
                .map((eventId) -> "/events/" + eventId)
                .collect(Collectors.toCollection(ArrayList::new));

        LocalDateTime start = LocalDateTime.of(2024, 1, 1, 0, 0, 0);
        LocalDateTime end = LocalDateTime.now().plusYears(100);
        StatisticFilterDto statisticFilterDto = StatisticFilterDto.builder()
                .start(start.format(formatter))
                .end(end.format(formatter))
                .uris(uris)
                .unique(true)
                .build();

        List<StatisticInfo> statisticInfoList = statisticClient.getAllByFilter(statisticFilterDto);
        ObjectMapper mapper = new ObjectMapper();
        List<StatisticInfo> actualStatisticInfoList = mapper.convertValue(statisticInfoList, new TypeReference<List<StatisticInfo>>() {
        });
        Map<Long, Long> eventIdToViews = actualStatisticInfoList.stream()
                .filter(statisticInfo -> statisticInfo.getApp().equalsIgnoreCase(APPLICATION_NAME) &&
                        statisticInfo.getUri().startsWith("/events/"))
                .collect(Collectors.toMap(statisticInfo -> Long.parseLong(statisticInfo.getUri().substring(8)),
                        StatisticInfo::getHits));
        log.debug("StatisticInfoList received=" + statisticInfoList);
        log.debug("eventIdToViewsMAP created=" + eventIdToViews);
        return eventIdToViews;
    }

    private Map<Long, Long> getRequestsMapForShortDtos(Collection<EventShortInfoDto> eventShortInfoDtoList) {
        List<Long> eventIdList = eventShortInfoDtoList.stream()
                .map(EventShortInfoDto::getId)
                .collect(Collectors.toList());

        return getIdToConfirmedRequestsMap(eventIdList);
    }

    private Map<Long, Long> getRequestsMapForFullDtos(Collection<EventFullInfoDto> eventFullInfoDtoList) {
        List<Long> eventIdList = eventFullInfoDtoList.stream()
                .map(EventFullInfoDto::getId)
                .collect(Collectors.toList());

        return getIdToConfirmedRequestsMap(eventIdList);
    }

    private Map<Long, Long> getIdToConfirmedRequestsMap(Collection<Long> eventIdList) {
        List<EventRequest> eventList = eventRequestRepository.findByEventIdInAndStatus(eventIdList, EventRequestStatus.CONFIRMED);

        Map<Long, Long> eventIdToConfirmedRequests = eventList.stream()
                .collect(Collectors.toMap((eventRequest -> eventRequest.getEvent().getId()),
                        eventRequest -> 1L,
                        (oldValue, newValue) -> oldValue + 1L));
        return eventIdToConfirmedRequests;
    }
}
