package ru.yandex.practicum.comment;

import lombok.*;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@Getter
@Setter
@ToString
@AllArgsConstructor
public class CommentGetParam {
    final List<Long> events;
    final LocalDateTime rangeStart;
    final LocalDateTime rangeEnd;
    final Pageable pageable;
}
