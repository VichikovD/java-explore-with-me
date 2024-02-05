package ru.yandex.practicum.compilation.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.compilation.model.dto.CompilationCreateDto;
import ru.yandex.practicum.compilation.model.dto.CompilationInfoDto;
import ru.yandex.practicum.compilation.model.dto.CompilationRequestDto;

@Service
public interface CompilationServiceAdmin {
    CompilationInfoDto create(CompilationCreateDto compilationDto);

    CompilationInfoDto update(CompilationRequestDto compilationRequestDto, long compilationId);

    void delete(long compilationId);
}
