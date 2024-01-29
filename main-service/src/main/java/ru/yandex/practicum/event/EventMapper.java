package ru.yandex.practicum.event;

import ru.yandex.practicum.event.dto.EventInfoDto;

import java.util.ArrayList;
import java.util.List;

public class EventMapper {
    public static EventInfoDto toDto(Event event) {
        return EventInfoDto.builder()
                .id(event.getId())
                .build();
    }

    public static Event toModel(EventInfoDto eventInfoDto) {
        return Event.builder()
                .id(eventInfoDto.getId())
                .build();
    }

    public static List<EventInfoDto> modelListToDtoList(List<Event> eventList) {
        List<EventInfoDto> dtoList = new ArrayList<>();
        for (Event event : eventList) {
            dtoList.add(toDto(event));
        }
        return dtoList;
    }
}
