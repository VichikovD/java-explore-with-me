package ru.yandex.practicum;

import lombok.*;

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
