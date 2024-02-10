package ru.yandex.practicum.event.model.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class LocationInfoDto {
    private float lat;
    private float lon;
}
