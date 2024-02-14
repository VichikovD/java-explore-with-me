package ru.yandex.practicum.compilation.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.StatisticClient;
import ru.yandex.practicum.StatisticInfo;
import ru.yandex.practicum.compilation.CompilationRepository;
import ru.yandex.practicum.compilation.model.Compilation;
import ru.yandex.practicum.compilation.model.CompilationMapper;
import ru.yandex.practicum.compilation.model.dto.CompilationCreateDto;
import ru.yandex.practicum.compilation.model.dto.CompilationInfoDto;
import ru.yandex.practicum.compilation.model.dto.CompilationRequestDto;
import ru.yandex.practicum.compilation.service.CompilationService;
import ru.yandex.practicum.dto.StatisticFilterDto;
import ru.yandex.practicum.event.EventUpdateParam;
import ru.yandex.practicum.event.model.Event;
import ru.yandex.practicum.event.model.dto.EventShortInfoDto;
import ru.yandex.practicum.event.model.mapper.EventMapper;
import ru.yandex.practicum.event.repository.EventRepository;
import ru.yandex.practicum.eventRequest.EventRequestRepository;
import ru.yandex.practicum.eventRequest.model.EventRequest;
import ru.yandex.practicum.eventRequest.model.EventRequestStatus;
import ru.yandex.practicum.exception.NotFoundException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CompilationServiceImpl implements CompilationService {
    final CompilationRepository compilationRepository;
    final EventRepository eventRepository;
    final EventRequestRepository eventRequestRepository;
    final StatisticClient statisticClient;
    static final String APPLICATION_NAME = "ewm-main-service";
    final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public CompilationInfoDto create(CompilationCreateDto compilationDto) {
        Set<Long> eventIdList = compilationDto.getEvents();
        Set<Event> eventList = eventRepository.findByIdIn(eventIdList);

        validateEvents(eventIdList, eventList);

        Compilation compilationToSave = CompilationMapper.toModel(compilationDto, eventList);
        Compilation compilationSaved = compilationRepository.save(compilationToSave);

        Set<EventShortInfoDto> eventShortInfoDtoSet = EventMapper.modelSetToShortInfoDtoSet(eventList);

        Map<Long, Long> eventIdToConfirmedRequests = getRequestsMapForShortDtos(eventShortInfoDtoSet);
        Map<Long, Long> eventIdToViews = getViewsMapForShortDtos(eventShortInfoDtoSet);

        EventUpdateParam updateParam = EventUpdateParam.builder()
                .eventIdToConfirmedRequests(eventIdToConfirmedRequests)
                .eventIdToViews(eventIdToViews)
                .build();

        updateEventShortInfoDto(eventShortInfoDtoSet, updateParam);

        return CompilationMapper.toInfoDto(compilationSaved, eventShortInfoDtoSet);
    }

    @Override
    public CompilationInfoDto update(CompilationRequestDto compilationRequestDto, long compilationId) {
        Compilation compilation = compilationRepository.findById(compilationId)
                .orElseThrow(() -> new NotFoundException("Compilation with id=" + compilationId + " was not found"));

        Set<Long> eventIdList = compilationRequestDto.getEvents();
        Set<Event> eventSet = null;
        if (eventIdList != null) {
            eventSet = eventRepository.findByIdIn(eventIdList);
            validateEvents(eventIdList, eventSet);
        }

        CompilationMapper.updateModelWithRequestDtoNotNullFields(compilation, compilationRequestDto, eventSet);
        Compilation compilationSaved = compilationRepository.save(compilation);

        Set<EventShortInfoDto> eventShortInfoDtoSet = EventMapper.modelSetToShortInfoDtoSet(compilationSaved.getEvents());

        Map<Long, Long> eventIdToConfirmedRequests = getRequestsMapForShortDtos(eventShortInfoDtoSet);
        Map<Long, Long> eventIdToViews = getViewsMapForShortDtos(eventShortInfoDtoSet);

        EventUpdateParam updateParam = EventUpdateParam.builder()
                .eventIdToConfirmedRequests(eventIdToConfirmedRequests)
                .eventIdToViews(eventIdToViews)
                .build();

        updateEventShortInfoDto(eventShortInfoDtoSet, updateParam);
        return CompilationMapper.toInfoDto(compilationSaved, eventShortInfoDtoSet);
    }

    @Override
    public void delete(long compilationId) {
        compilationRepository.findById(compilationId)
                .orElseThrow(() -> new NotFoundException("Compilation with id=" + compilationId + " was not found"));
        compilationRepository.deleteById(compilationId);
    }

    private void validateEvents(Set<Long> eventIdList, Set<Event> eventList) {
        long eventsInCompilationQuantity = eventIdList.size();
        if (eventList.size() < eventsInCompilationQuantity) {
            List<Long> missingEventIdList = new ArrayList<>();
            List<Long> foundRequestIdList = eventList.stream()
                    .map(Event::getId)
                    .collect(Collectors.toList());
            for (long requestId : eventIdList) {
                if (!foundRequestIdList.contains(requestId)) {
                    missingEventIdList.add(requestId);
                }
            }
            throw new NotFoundException("Events with ids=" + missingEventIdList + " are not found");
        }
    }

    @Override
    public List<CompilationInfoDto> getFiltered(Boolean pinned, Pageable pageable, Sort sort) {
        List<Compilation> compilationList = compilationRepository.findFiltered(pinned, pageable);

        Map<Long, Long> idToRequestsMap = getRequestsMapForCollection(compilationList);
        Map<Long, Long> idToViewsMap = getViewsMapForCollection(compilationList);

        EventUpdateParam updateParam = EventUpdateParam.builder()
                .eventIdToConfirmedRequests(idToRequestsMap)
                .eventIdToViews(idToViewsMap)
                .build();

        return CompilationMapper.modelListToInfoDtoList(compilationList, updateParam);
    }

    @Override
    public CompilationInfoDto getById(long compId) {
        Compilation compilation = compilationRepository.findById(compId)
                .orElseThrow(() -> new NotFoundException("Compilation with id=" + compId + " was not found."));

        Set<EventShortInfoDto> eventShortInfoDtoSet = EventMapper.modelSetToShortInfoDtoSet(compilation.getEvents());

        Map<Long, Long> eventIdToConfirmedRequests = getRequestsMapForShortDtos(eventShortInfoDtoSet);
        Map<Long, Long> eventIdToViews = getViewsMapForShortDtos(eventShortInfoDtoSet);

        EventUpdateParam updateParam = EventUpdateParam.builder()
                .eventIdToConfirmedRequests(eventIdToConfirmedRequests)
                .eventIdToViews(eventIdToViews)
                .build();

        updateEventShortInfoDto(eventShortInfoDtoSet, updateParam);

        return CompilationMapper.toInfoDto(compilation, eventShortInfoDtoSet);
    }

    private Map<Long, Long> getViewsMapForCollection(Collection<Compilation> compilationInfoDtos) {
        Set<Long> eventIdSet = compilationInfoDtos.stream()
                .flatMap(compilation -> compilation.getEvents().stream())
                .map(Event::getId)
                .collect(Collectors.toSet());

        return getIdToViewsMapByIdCollection(eventIdSet);
    }

    private Map<Long, Long> getViewsMapForShortDtos(Collection<EventShortInfoDto> eventShortInfoDto) {
        List<Long> eventIdList = eventShortInfoDto.stream()
                .map(EventShortInfoDto::getId)
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

    private Map<Long, Long> getRequestsMapForCollection(Collection<Compilation> compilationInfoDtos) {
        Set<Long> eventIdSet = compilationInfoDtos.stream()
                .flatMap(compilation -> compilation.getEvents().stream())
                .map(Event::getId)
                .collect(Collectors.toSet());

        return getIdToConfirmedRequestsMap(eventIdSet);
    }

    private Map<Long, Long> getRequestsMapForShortDtos(Collection<EventShortInfoDto> eventShortInfoDto) {
        List<Long> eventIdList = eventShortInfoDto.stream()
                .map(EventShortInfoDto::getId)
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

    private void updateEventShortInfoDto(Collection<EventShortInfoDto> eventShortInfoDtoList, EventUpdateParam updateParam) {
        for (EventShortInfoDto fullInfoDto : eventShortInfoDtoList) {
            long eventId = fullInfoDto.getId();
            Map<Long, Long> eventIdToConfirmedRequests = updateParam.getEventIdToConfirmedRequests();
            Map<Long, Long> eventIdToViews = updateParam.getEventIdToViews();

            if (eventIdToConfirmedRequests != null) {
                fullInfoDto.setConfirmedRequests(eventIdToConfirmedRequests.getOrDefault(eventId, 0L));
            }

            if (eventIdToViews != null) {
                fullInfoDto.setViews(eventIdToViews.getOrDefault(eventId, 0L));
            }
        }
    }
}
