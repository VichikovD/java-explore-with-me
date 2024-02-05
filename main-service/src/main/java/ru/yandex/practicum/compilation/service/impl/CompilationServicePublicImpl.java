package ru.yandex.practicum.compilation.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.compilation.CompilationRepository;
import ru.yandex.practicum.compilation.model.Compilation;
import ru.yandex.practicum.compilation.model.CompilationMapper;
import ru.yandex.practicum.compilation.model.dto.CompilationInfoDto;
import ru.yandex.practicum.compilation.service.CompilationServicePublic;
import ru.yandex.practicum.event.model.dto.EventShortInfoDto;
import ru.yandex.practicum.event.model.mapper.EventMapper;
import ru.yandex.practicum.exception.NotFoundException;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class CompilationServicePublicImpl implements CompilationServicePublic {
    final CompilationRepository compilationRepository;

    @Override
    public List<CompilationInfoDto> getFiltered(Boolean pinned, Pageable pageable, Sort sort) {
        List<Compilation> compilationList = compilationRepository.findAllFiltered(pinned, pageable);
        System.out.println("Compilation list from repository=" + compilationList);  // testing
        List<CompilationInfoDto> eventFullInfoDtoList = CompilationMapper.modelListToInfoDtoList(compilationList);
        // TODO eventFullInfoDtoList + confirmedRequests + views TODO + sort
        return eventFullInfoDtoList;
    }

    @Override
    public CompilationInfoDto getById(long compId) {
        Compilation compilation = compilationRepository.findById(compId)
                .orElseThrow(() -> new NotFoundException("Compilation with id=" + compId + " was not found."));

        Set<EventShortInfoDto> eventShortInfoDtoSet = EventMapper.modelSetToShortInfoDtoSet(compilation.getEvents());
        CompilationInfoDto compilationInfoDto = CompilationMapper.toInfoDto(compilation, eventShortInfoDtoSet);
        // TODO set for Collection confirmedRequests + views;
        return compilationInfoDto;
    }
}
