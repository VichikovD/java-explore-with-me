package ru.yandex.practicum.eventRequest.model;

import java.util.ArrayList;
import java.util.List;

public class EventRequestMapper {
    public static EventRequestInfoDto toInfoDto(EventRequest eventRequest) {
        return EventRequestInfoDto.builder()
                .id(eventRequest.getId())
                .requester(eventRequest.getRequester().getId())
                .event(eventRequest.getEvent().getId())
                .created(eventRequest.getCreated())
                .status(eventRequest.getStatus())
                .build();
    }

    public static List<EventRequestInfoDto> listModelToListInfoDto(List<EventRequest> eventRequestList) {
        List<EventRequestInfoDto> eventRequestInfoDtoList = new ArrayList<>();
        for(EventRequest eventRequest : eventRequestList) {
            eventRequestInfoDtoList.add(toInfoDto(eventRequest));
        }
        return eventRequestInfoDtoList;
    }
}
