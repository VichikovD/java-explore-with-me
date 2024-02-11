package ru.yandex.practicum.event.model.dto;

import lombok.*;
import ru.yandex.practicum.eventComment.model.EventComment;

import java.util.List;
import java.util.Map;

@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class EventUpdateParam {
    Map<Long, Long> eventIdToConfirmedRequests;
    Map<Long, Long> eventIdToViews;
    Map<Long, List<EventComment>> eventIdToComments;
}
