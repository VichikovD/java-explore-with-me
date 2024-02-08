package ru.yandex.practicum.event.model.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.domain.Pageable;
import ru.yandex.practicum.event.model.PublishState;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@Getter
@Setter
@ToString
public class GetFullEventsRequest {
    List<Long> users;
    List<PublishState> publishState;
    List<Long> categories;
    LocalDateTime rangeStart;
    LocalDateTime rangeEnd;
    Pageable pageable;
}
