package ru.yandex.practicum.event.model.mapper;

import ru.yandex.practicum.category.Category;
import ru.yandex.practicum.category.CategoryMapper;
import ru.yandex.practicum.event.model.Event;
import ru.yandex.practicum.event.model.Location;
import ru.yandex.practicum.event.model.PublishState;
import ru.yandex.practicum.event.model.dto.*;
import ru.yandex.practicum.user.User;
import ru.yandex.practicum.user.UserMapper;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class EventMapper {
    public static EventFullInfoDto toFullInfoDto(Event event) {
        return EventFullInfoDto.builder()
                .id(event.getId())
                .annotation(event.getAnnotation())
                .category(CategoryMapper.toDto(event.getCategory()))
                .createdOn(event.getCreatedOn())
                .description(event.getDescription())
                .eventDate(event.getEventDate())
                .initiator(UserMapper.toDto(event.getInitiator()))
                .location(LocationMapper.toInfoDto(event.getLocation()))
                .paid(event.isPaid())
                .participantLimit(event.getParticipantLimit())
                .publishedOn(event.getPublishedOn())
                .requestModeration(event.isRequestModeration())
                .state(event.getState())
                .title(event.getTitle())
                .build();
    }

    public static EventShortInfoDto toShortInfoDto(Event event) {
        return EventShortInfoDto.builder()
                .id(event.getId())
                .annotation(event.getAnnotation())
                .category(CategoryMapper.toDto(event.getCategory()))
                .eventDate(event.getEventDate())
                .initiator(UserMapper.toDto(event.getInitiator()))
                .paid(event.isPaid())
                .title(event.getTitle())
                .build();
    }

    public static Event requestDtoToModel(EventCreateDto eventCreateDto, Category category, User initiator) {
        return Event.builder()
                .annotation(eventCreateDto.getAnnotation())
                .category(category)
                .createdOn(LocalDateTime.now())
                .description(eventCreateDto.getDescription())
                .eventDate(eventCreateDto.getEventDate())
                .initiator(initiator)
                .location(eventCreateDto.getLocation())
                .paid(eventCreateDto.getPaid())
                .participantLimit(eventCreateDto.getParticipantLimit())
                .publishedOn(null)
                .requestModeration(eventCreateDto.getRequestModeration())
                .state(PublishState.PENDING)
                .title(eventCreateDto.getTitle())
                .build();
    }

    public static List<EventFullInfoDto> modelListToFullInfoDtoList(List<Event> eventList) {
        List<EventFullInfoDto> dtoList = new ArrayList<>();
        for (Event event : eventList) {
            dtoList.add(toFullInfoDto(event));
        }
        return dtoList;
    }

    public static List<EventShortInfoDto> modelListToShortInfoDtoList(List<Event> eventList) {
        List<EventShortInfoDto> dtoList = new ArrayList<>();
        for (Event event : eventList) {
            dtoList.add(toShortInfoDto(event));
        }
        return dtoList;
    }

    public static void updateModelWithRequestAdminDtoNotNullFields(Event event, EventRequestAdminDto eventRequestDto, Category category, Location location) {
        String annotation = eventRequestDto.getAnnotation();
        if (annotation != null) {
            event.setAnnotation(annotation);
        }

        if (category != null) {
            event.setCategory(category);
        }

        String description = eventRequestDto.getDescription();
        if (description != null) {
            event.setDescription(description);
        }

        LocalDateTime eventDate = eventRequestDto.getEventDate();
        if (eventDate != null) {
            event.setEventDate(eventDate);
        }


        if (location != null) {
            event.setLocation(location);
        }

        Boolean paid = eventRequestDto.getPaid();
        if (paid != null) {
            event.setPaid(paid);
        }

        Long participantLimit = eventRequestDto.getParticipantLimit();
        if (participantLimit != null) {
            event.setParticipantLimit(participantLimit);
        }

        Boolean requestModeration = eventRequestDto.getRequestModeration();
        if (requestModeration != null) {
            event.setRequestModeration(requestModeration);
        }

        PublishState.StateAction stateAction = eventRequestDto.getStateAction();
        if (stateAction != null) {
            event.setState(PublishState.StateAction.stateFromAction(stateAction));
        }

        String title = eventRequestDto.getTitle();
        if (title != null) {
            event.setTitle(title);
        }
    }

    public static void updateModelWithRequestAdminDtoNotNullFields(Event event, EventUpdateDto eventRequestDto, Category category, Location location) {
        String annotation = eventRequestDto.getAnnotation();
        if (annotation != null) {
            event.setAnnotation(annotation);
        }

        if (category != null) {
            event.setCategory(category);
        }

        String description = eventRequestDto.getDescription();
        if (description != null) {
            event.setDescription(description);
        }

        LocalDateTime eventDate = eventRequestDto.getEventDate();
        if (eventDate != null) {
            event.setEventDate(eventDate);
        }


        if (location != null) {
            event.setLocation(location);
        }

        Boolean paid = eventRequestDto.getPaid();
        if (paid != null) {
            event.setPaid(paid);
        }

        Long participantLimit = eventRequestDto.getParticipantLimit();
        if (participantLimit != null) {
            event.setParticipantLimit(participantLimit);
        }

        Boolean requestModeration = eventRequestDto.getRequestModeration();
        if (requestModeration != null) {
            event.setRequestModeration(requestModeration);
        }

        PublishState.StateAction stateAction = eventRequestDto.getStateAction();
        if (stateAction != null) {
            event.setState(PublishState.StateAction.stateFromAction(stateAction));
        }

        String title = eventRequestDto.getTitle();
        if (title != null) {
            event.setTitle(title);
        }
    }
}
