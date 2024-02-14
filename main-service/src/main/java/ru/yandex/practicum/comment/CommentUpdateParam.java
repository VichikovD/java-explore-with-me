package ru.yandex.practicum.comment;

import lombok.*;
import ru.yandex.practicum.comment.model.dto.EventCommentRequestDto;

@Builder
@Getter
@Setter
@ToString
@AllArgsConstructor
public class CommentUpdateParam {
    final Long authorId;
    final long commentId;
    final EventCommentRequestDto eventCommentRequestDto;
}
