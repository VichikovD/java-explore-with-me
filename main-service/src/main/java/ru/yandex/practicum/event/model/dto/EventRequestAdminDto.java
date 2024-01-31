package ru.yandex.practicum.event.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;
import ru.yandex.practicum.event.model.Location;
import ru.yandex.practicum.event.model.PublishState;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class EventRequestAdminDto {
    Long id;

    String annotation;

    Long category;

    String description;

    //DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime eventDate;

    Location location;

    Boolean paid;

    Long participantLimit;

    Boolean requestModeration;

    PublishState.StateAction stateAction;

    String title;
}
