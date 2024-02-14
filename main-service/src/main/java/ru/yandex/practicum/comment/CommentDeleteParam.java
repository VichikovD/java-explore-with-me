package ru.yandex.practicum.comment;

import lombok.*;

@Builder
@Getter
@Setter
@ToString
@AllArgsConstructor
public class CommentDeleteParam {
    Long authorId;
    long commentId;
}
