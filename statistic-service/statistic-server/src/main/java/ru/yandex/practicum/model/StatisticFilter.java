package ru.yandex.practicum.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;

@Builder
@Getter
@Setter
public class StatisticFilter {
    LocalDateTime start;
    LocalDateTime end;
    ArrayList<String> uris;
    boolean unique;
}
