package ru.yandex.practicum.event.model.mapper;

import ru.yandex.practicum.event.model.Location;
import ru.yandex.practicum.event.model.dto.LocationInfoDto;

public class LocationMapper {
    public static LocationInfoDto toInfoDto(Location location) {
        return LocationInfoDto.builder()
                .lat(location.getLat())
                .lon(location.getLon())
                .build();
    }
}
