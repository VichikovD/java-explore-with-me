package ru.yandex.practicum.comment.model;

import ru.yandex.practicum.comment.model.dto.EventCommentInfoDto;
import ru.yandex.practicum.comment.model.dto.EventCommentRequestDto;
import ru.yandex.practicum.event.model.Event;
import ru.yandex.practicum.user.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class EventCommentMapper {
    public static EventComment requestDtoToModel(EventCommentRequestDto requestDto, Event event, User author, boolean isModified) {
        return EventComment.builder()
                .createdOn(LocalDateTime.now())
                .author(author)
                .text(requestDto.getText())
                .event(event)
                .isModified(isModified)
                .build();
    }

    public static EventCommentInfoDto modelToInfoDto(EventComment eventComment) {
        return EventCommentInfoDto.builder()
                .id(eventComment.getId())
                .createdOn(eventComment.getCreatedOn())
                .authorName(eventComment.getAuthor().getName())
                .text(eventComment.getText())
                .event(eventComment.getEvent().getId())
                .isModified(eventComment.isModified())
                .build();
    }

    public static List<EventCommentInfoDto> modelListToInfoDtoList(List<EventComment> eventCommentList) {
        List<EventCommentInfoDto> infoDtoList = new ArrayList<>();
        for (EventComment comment : eventCommentList) {
            EventCommentInfoDto infoDto = EventCommentMapper.modelToInfoDto(comment);
            infoDtoList.add(infoDto);
        }
        return infoDtoList;
    }

    public static void updateModelByRequestDto(EventComment eventComment, EventCommentRequestDto requestDto) {
        eventComment.setText(requestDto.getText());
        eventComment.setModified(true);
    }
}
