package ru.yandex.practicum.eventComment;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@Getter
@Setter
@ToString
public class GetEventCommentsRequest {
    List<Long> events;
    LocalDateTime rangeStart;
    LocalDateTime rangeEnd;
    int offset;
    int limit;
    Pageable pageable;
}
