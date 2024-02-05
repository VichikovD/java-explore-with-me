package ru.yandex.practicum.util;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.event.model.dto.EventFullInfoDto;
import ru.yandex.practicum.event.model.dto.EventShortInfoDto;
import ru.yandex.practicum.eventRequest.EventRequestRepository;
import ru.yandex.practicum.eventRequest.model.EventRequest;
import ru.yandex.practicum.eventRequest.model.EventRequestStatus;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
public class EventRequestsManager {
    final EventRequestRepository eventRequestRepository;

    public void updateConfirmedRequestsToShortDtos(Collection<EventShortInfoDto> eventShortInfoDtoList) {
        List<Long> eventIdList = eventShortInfoDtoList.stream()
                .map(EventShortInfoDto::getId)
                .collect(Collectors.toList());

        Map<Long, Long> eventIdToConfirmedRequests = getIdToConfirmedRequestsMap(eventIdList);

        for (EventShortInfoDto shortInfoDto : eventShortInfoDtoList) {
            Long views = eventIdToConfirmedRequests.getOrDefault(shortInfoDto.getId(), 0L);
            shortInfoDto.setConfirmedRequests(views);
        }
    }

    public void updateConfirmedRequestsToFullDtos(Collection<EventFullInfoDto> eventFullInfoDtoList) {
        log.debug("Collection<EventFullInfoDto>=" + eventFullInfoDtoList);
        List<Long> eventIdList = eventFullInfoDtoList.stream()
                .map(EventFullInfoDto::getId)
                .collect(Collectors.toList());

        Map<Long, Long> eventIdToConfirmedRequests = getIdToConfirmedRequestsMap(eventIdList);
        log.debug("eventIdToConfirmedRequests=" + eventIdToConfirmedRequests);

        for (EventFullInfoDto fullInfoDto : eventFullInfoDtoList) {
            Long views = eventIdToConfirmedRequests.getOrDefault(fullInfoDto.getId(), 0L);
            fullInfoDto.setConfirmedRequests(views);
        }
    }

    private Map<Long, Long> getIdToConfirmedRequestsMap(Collection<Long> eventIdList) {
        List<EventRequest> eventList = eventRequestRepository.findAllByEventIdInAndStatus(eventIdList, EventRequestStatus.CONFIRMED);

        Map<Long, Long> eventIdToConfirmedRequests = eventList.stream()
                .collect(Collectors.toMap((eventRequest -> eventRequest.getEvent().getId()),
                        eventRequest -> 1L,
                        (oldValue, newValue) -> oldValue + 1L));
        return eventIdToConfirmedRequests;
    }
}
