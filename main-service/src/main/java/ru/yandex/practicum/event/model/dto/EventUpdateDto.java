package ru.yandex.practicum.event.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import ru.yandex.practicum.event.model.Location;
import ru.yandex.practicum.event.model.PublishState;
import ru.yandex.practicum.validation.InTwoHours;
import ru.yandex.practicum.validation.NotEmptyIfNotNull;

import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class EventUpdateDto {
    Long id;

    @Size(min = 20, max = 2000)
    @NotEmptyIfNotNull(message = "Field: annotation. Error: must not be empty if not null.")
    String annotation;

    Long category;

    @Size(min = 20, max = 7000)
    @NotEmptyIfNotNull(message = "Field: description. Error: must not be empty if not null.")
    String description;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @InTwoHours(message = "Field: eventDate. Error: Must contain time at least 2 hours ahead of now")
    LocalDateTime eventDate;

    Location location;

    Boolean paid;

    @PositiveOrZero(message = "Field: participantLimit. Error: must be positive")
    Long participantLimit;

    Boolean requestModeration;

    PublishState.StateAction stateAction;

    @Size(min = 3, max = 120)
    @NotEmptyIfNotNull(message = "Field: title. Error: must not be empty if not null.")
    String title;
}
