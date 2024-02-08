package ru.yandex.practicum.compilation.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.compilation.model.dto.CompilationCreateDto;
import ru.yandex.practicum.compilation.model.dto.CompilationInfoDto;
import ru.yandex.practicum.compilation.model.dto.CompilationRequestDto;
import ru.yandex.practicum.compilation.service.CompilationService;

@Slf4j
@RestController
@RequestMapping("/admin/compilations")
@RequiredArgsConstructor
@Validated
public class CompilationAdminController {
    final CompilationService compilationService;

    @PostMapping
    @ResponseStatus(value = HttpStatus.CREATED)
    public CompilationInfoDto create(@RequestBody @Validated CompilationCreateDto compilationDto) {
        log.info("POST \"/admin/compilations\" Body={}", compilationDto);
        CompilationInfoDto compilation = compilationService.create(compilationDto);
        log.debug("Compilation created= " + compilation);
        return compilation;
    }

    @PatchMapping("/{compilationId}")
    public CompilationInfoDto update(@PathVariable(name = "compilationId") long compilationId,
                                     @RequestBody @Validated CompilationRequestDto compilationDto) {
        log.info("PATCH \"/admin/compilations/{}\" Body={}", compilationId, compilationDto);
        CompilationInfoDto compilation = compilationService.update(compilationDto, compilationId);
        log.debug("Compilation updated= " + compilation);
        return compilation;
    }

    @DeleteMapping("/{compilationId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable(name = "compilationId") long compilationId) {
        log.info("DELETE \"/admin/compilations/{}\"", compilationId);
        compilationService.delete(compilationId);
        log.debug("Compilation deleted= " + compilationId);
    }
}