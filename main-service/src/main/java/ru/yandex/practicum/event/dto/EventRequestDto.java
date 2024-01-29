package ru.yandex.practicum.event.dto;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;
import ru.yandex.practicum.util.Location;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
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

    @NotEmpty(message = "Field: category. Error: must not be blank.")
    Long category;

    @NotBlank(message = "Field: description. Error: must not be blank.")
    String description;

    @NotEmpty(message = "Field: eventDate. Error: must not be blank.")
    //DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime eventDate;

    @NotEmpty(message = "Field: location. Error: must not be blank.")
    Location location;

    @NotEmpty(message = "Field: paid. Error: must not be blank.")
    Boolean paid;

    @NotEmpty(message = "Field: participantLimit. Error: must not be blank.")
    Integer participantLimit;

    @NotEmpty(message = "Field: requestModeration. Error: must not be blank.")
    Boolean requestModeration;

    @NotBlank(message = "Field: title. Error: must not be blank.")
    String title;
}
