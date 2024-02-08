package ru.yandex.practicum.event.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import ru.yandex.practicum.event.model.Location;
import ru.yandex.practicum.validation.InTwoHours;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class EventCreateDto {
    Long id;

    @Size(min = 20, max = 2000)
    @NotBlank(message = "Field: annotation. Error: must not be blank.")
    String annotation;

    @NotNull(message = "Field: category. Error: must not be null.")
    Long category;

    @Size(min = 20, max = 7000)
    @NotBlank(message = "Field: description. Error: must not be blank.")
    String description;

    @NotNull(message = "Field: eventDate. Error: must not be null.")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @InTwoHours(message = "Field: eventDate. Error: Must contain time at least 2 hours ahead of now")
    LocalDateTime eventDate;


    @NotNull(message = "Field: location. Error: must not be null.")
    Location location;

    Boolean paid = false;

    @NotNull(message = "Field: participantLimit. Error: must not be null.")
    @PositiveOrZero(message = "Field: participantLimit. Error: must be positive")
    Long participantLimit = 0L;

    @NotNull(message = "Field: requestModeration. Error: must not be null.")
    Boolean requestModeration = true;

    @Size(min = 3, max = 120)
    @NotBlank(message = "Field: title. Error: must not be blank.")
    String title;
}
