package ru.yandex.practicum.eventRequest.model;

import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class EventRequestInfoDto {
    long id;

    long event;

    long requester;

    EventRequestStatus status;

    LocalDateTime created;
}
