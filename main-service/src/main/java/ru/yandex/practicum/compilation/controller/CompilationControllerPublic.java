package ru.yandex.practicum.compilation.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.compilation.model.dto.CompilationInfoDto;
import ru.yandex.practicum.compilation.service.CompilationServicePublic;
import ru.yandex.practicum.util.OffsetPageable;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/compilations")
@RequiredArgsConstructor
public class CompilationControllerPublic {
    final CompilationServicePublic compilationService;

    @GetMapping
    public List<CompilationInfoDto> getFiltered(@RequestParam(name = "pinned", required = false) Boolean pinned,
                                                @RequestParam(name = "from", defaultValue = "0") int offset,
                                                @RequestParam(name = "size", defaultValue = "10") int limit) {
        log.info("GET \"/compilations?pinned={}&from={}&size={}\"", pinned, offset, limit);
        Sort sortEvents = Sort.by(Sort.Direction.DESC, "views");
        Sort sortCompilations = Sort.by(Sort.Direction.ASC, "id");
        Pageable pageable = new OffsetPageable(offset, limit, sortCompilations);
        List<CompilationInfoDto> compilationList = compilationService.getFiltered(pinned, pageable, sortEvents);
        log.debug("compilationList found= " + compilationList);
        return compilationList;
    }

    @GetMapping("/{compId}")
    public CompilationInfoDto getById(@PathVariable(name = "compId") long compId) {
        log.info("GET \"/compilations/{}\"", compId);
        CompilationInfoDto compilation = compilationService.getById(compId);
        log.debug("Compilation found= " + compilation);
        return compilation;
    }
}
