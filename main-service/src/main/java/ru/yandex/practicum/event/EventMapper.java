package ru.yandex.practicum.event;

import ru.yandex.practicum.category.Category;
import ru.yandex.practicum.category.CategoryMapper;
import ru.yandex.practicum.event.dto.EventInfoDto;
import ru.yandex.practicum.event.dto.EventRequestDto;
import ru.yandex.practicum.event.dto.PublishState;
import ru.yandex.practicum.user.User;
import ru.yandex.practicum.user.UserMapper;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class EventMapper {
    public static EventInfoDto toDto(Event event) {
        return EventInfoDto.builder()
                .id(event.getId())
                .annotation(event.getAnnotation())
                .category(CategoryMapper.toDto(event.getCategory()))
                .createdOn(event.getCreatedOn())
                .description(event.getDescription())
                .eventDate(event.getEventDate())
                .initiator(UserMapper.toDto(event.getInitiator()))
                .location(event.getLocation())
                .paid(event.isPaid())
                .participantLimit(event.getParticipantLimit())
                .publishedOn(event.getPublishedOn())
                .requestModeration(event.isRequestModeration())
                .state(event.getState())
                .title(event.getTitle())
                .build();
    }

    public static Event requestDtoToModel(EventRequestDto eventRequestDto, Category category, User initiator) {
        return Event.builder()
                .annotation(eventRequestDto.getAnnotation())
                .category(category)
                .createdOn(LocalDateTime.now())
                .description(eventRequestDto.getDescription())
                .eventDate(eventRequestDto.getEventDate())
                .initiator(initiator)
                .location(eventRequestDto.getLocation())
                .paid(eventRequestDto.getPaid())
                .participantLimit(eventRequestDto.getParticipantLimit())
                .publishedOn(null)
                .requestModeration(eventRequestDto.getRequestModeration())
                .state(PublishState.WAITING)
                .title(eventRequestDto.getTitle())
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
