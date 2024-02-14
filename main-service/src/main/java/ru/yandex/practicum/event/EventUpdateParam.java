package ru.yandex.practicum.event;

import lombok.*;
import ru.yandex.practicum.comment.model.EventComment;

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
