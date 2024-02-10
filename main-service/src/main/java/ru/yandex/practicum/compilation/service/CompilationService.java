package ru.yandex.practicum.compilation.service;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.compilation.model.dto.CompilationCreateDto;
import ru.yandex.practicum.compilation.model.dto.CompilationInfoDto;
import ru.yandex.practicum.compilation.model.dto.CompilationRequestDto;

import java.util.List;

@Service
public interface CompilationService {
    CompilationInfoDto create(CompilationCreateDto compilationDto);

    CompilationInfoDto update(CompilationRequestDto compilationRequestDto, long compilationId);

    void delete(long compilationId);

    List<CompilationInfoDto> getFiltered(Boolean pinned, Pageable pageable, Sort sort);

    CompilationInfoDto getById(long compId);
}
