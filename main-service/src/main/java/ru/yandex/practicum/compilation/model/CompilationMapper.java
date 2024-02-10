package ru.yandex.practicum.compilation.model;

import ru.yandex.practicum.compilation.model.dto.CompilationCreateDto;
import ru.yandex.practicum.compilation.model.dto.CompilationInfoDto;
import ru.yandex.practicum.compilation.model.dto.CompilationRequestDto;
import ru.yandex.practicum.event.model.Event;
import ru.yandex.practicum.event.model.dto.EventShortInfoDto;
import ru.yandex.practicum.event.model.mapper.EventMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class CompilationMapper {
    public static CompilationInfoDto toInfoDto(Compilation compilation, Set<EventShortInfoDto> events) {
        return CompilationInfoDto.builder()
                .id(compilation.getId())
                .pinned(compilation.isPinned())
                .title(compilation.getTitle())
                .events(events)
                .build();
    }

    public static Compilation toModel(CompilationCreateDto compilationInfoDto, Set<Event> events) {
        return Compilation.builder()
                .pinned(compilationInfoDto.isPinned())
                .title(compilationInfoDto.getTitle())
                .events(events)
                .build();
    }

    public static void updateModelWithRequestDtoNotNullFields(Compilation compilation, CompilationRequestDto requestDto,
                                                              Set<Event> eventSet) {
        Boolean pinned = requestDto.getPinned();
        if (pinned != null) {
            compilation.setPinned(pinned);
        }

        String title = requestDto.getTitle();
        if (title != null) {
            compilation.setTitle(title);
        }

        if (eventSet != null) {
            compilation.setEvents(eventSet);
        }
    }

    public static List<CompilationInfoDto> modelListToInfoDtoList(List<Compilation> compilationList,
                                                                  Map<Long, Long> viewsMap,
                                                                  Map<Long, Long> requestsMap) {
        List<CompilationInfoDto> compilationInfoDtoList = new ArrayList<>();
        for (Compilation compilation : compilationList) {
            Set<Event> eventSet = compilation.getEvents();
            Set<EventShortInfoDto> eventShortInfoDtoSet = EventMapper.modelSetToShortInfoDtoSet(eventSet);
            EventMapper.updateViewsToShortDtos(eventShortInfoDtoSet, viewsMap);
            EventMapper.updateConfirmedRequestsToShortDtos(eventShortInfoDtoSet, requestsMap);
            compilationInfoDtoList.add(toInfoDto(compilation, eventShortInfoDtoSet));
        }
        return compilationInfoDtoList;
    }
}
