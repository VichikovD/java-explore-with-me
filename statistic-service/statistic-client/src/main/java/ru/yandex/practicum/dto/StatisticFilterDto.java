package ru.yandex.practicum.dto;

import lombok.*;

import java.util.ArrayList;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StatisticFilterDto {
    String start;
    String end;
    ArrayList<String> uris;
    boolean unique;
}