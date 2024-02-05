package ru.yandex.practicum.compilation.model.dto;

import lombok.*;
import ru.yandex.practicum.event.model.dto.EventShortInfoDto;

import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class CompilationInfoDto {
    long id;

    boolean pinned;

    String title;

    Set<EventShortInfoDto> events;
}
