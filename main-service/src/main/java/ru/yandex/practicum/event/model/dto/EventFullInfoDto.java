package ru.yandex.practicum.event.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;
import ru.yandex.practicum.category.CategoryDto;
import ru.yandex.practicum.comment.model.dto.EventCommentInfoDto;
import ru.yandex.practicum.event.model.PublishState;
import ru.yandex.practicum.user.UserDto;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class EventFullInfoDto {
    Long id;

    String annotation;

    CategoryDto category;

    long confirmedRequests;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime createdOn;

    String description;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime eventDate;

    UserDto initiator;

    LocationInfoDto location;

    boolean paid;

    long participantLimit;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime publishedOn;

    boolean requestModeration;

    PublishState state;

    String title;

    long views;

    List<EventCommentInfoDto> comments;
}