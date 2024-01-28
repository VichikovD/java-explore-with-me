package ru.yandex.practicum.compilation.service;

import org.springframework.data.domain.Sort;
import ru.yandex.practicum.compilation.CompilationDto;

import java.util.List;


public interface CompilationService {
    List<CompilationDto> getFiltered(boolean pinned, int offset, int size, Sort sort);

    CompilationDto getById(int compId);

}
