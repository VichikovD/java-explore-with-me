package ru.yandex.practicum.compilation.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.compilation.CompilationDto;
import ru.yandex.practicum.compilation.CompilationRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CompilationServiceImpl implements CompilationService {
    final CompilationRepository compilationRepository;

    @Override
    public List<CompilationDto> getFiltered(boolean pinned, int offset, int size, Sort sort) {
        return null;
    }

    @Override
    public CompilationDto getById(int compId) {
        return null;
    }
}
