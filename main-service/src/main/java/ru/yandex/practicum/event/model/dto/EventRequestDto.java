package ru.yandex.practicum.event.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import ru.yandex.practicum.event.model.Location;
import ru.yandex.practicum.event.validation.InTwoHours;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class EventRequestDto {
    Long id;

    @NotBlank(message = "Field: annotation. Error: must not be blank.")
    String annotation;

    @NotNull(message = "Field: category. Error: must not be blank.")
    Long category;

    @NotBlank(message = "Field: description. Error: must not be blank.")
    String description;

    @NotNull(message = "Field: eventDate. Error: must not be blank.")
    //DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    //@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @InTwoHours(message = "Field: eventDate. Error: Must contain time at least 2 hours ahead of now")
    LocalDateTime eventDate;


    @NotNull(message = "Field: location. Error: must not be blank.")
    Location location;

    @NotNull(message = "Field: paid. Error: must not be blank.")
    Boolean paid;

    @NotNull(message = "Field: participantLimit. Error: must not be blank.")
    Long participantLimit;

    @NotNull(message = "Field: requestModeration. Error: must not be blank.")
    Boolean requestModeration;

    @NotBlank(message = "Field: title. Error: must not be blank.")
    String title;
}
