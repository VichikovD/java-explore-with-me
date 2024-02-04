package ru.yandex.practicum.eventRequest.model;

import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@ToString
@Setter
@Getter
public class EventRequestStatusChanger {
    @NotEmpty(message = "Field: requestIds. Error: must not be null or empty.")
    List<Long> requestIds;

    @NotNull(message = "Field: status. Error: must not be null.")
    EventRequestStatus status;
}
