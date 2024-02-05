package ru.yandex.practicum.compilation.service;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.compilation.model.dto.CompilationInfoDto;

import java.util.List;


@Service
public interface CompilationServicePublic {
    List<CompilationInfoDto> getFiltered(Boolean pinned, Pageable pageable, Sort sort);

    CompilationInfoDto getById(long compId);

}
