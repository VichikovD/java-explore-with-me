package ru.yandex.practicum;

import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class StatisticInfoDto {
    private String app;
    private String uri;
    private long hits;
}
