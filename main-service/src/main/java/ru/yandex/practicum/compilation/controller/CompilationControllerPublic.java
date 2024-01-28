package ru.yandex.practicum.compilation.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.compilation.CompilationDto;
import ru.yandex.practicum.compilation.service.CompilationService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/compilations")
@RequiredArgsConstructor
public class CompilationControllerPublic {
    final CompilationService compilationService;

    @GetMapping
    public List<CompilationDto> getFiltered(@RequestParam(name = "pinned") boolean pinned,
                                            @RequestParam(name = "from", defaultValue = "0") int offset,
                                            @RequestParam(name = "size", defaultValue = "10") int size) {
        log.info("GET \"/compilations?pinned={}&from={}&size={}\"", pinned, offset, size);
        Sort sort = Sort.by(Sort.Direction.DESC, "views");
        List<CompilationDto> compilationList = compilationService.getFiltered(pinned, offset, size, sort);
        log.debug("compilationList = " + compilationList);
        return compilationList;
    }

    @GetMapping("/{compId}")
    public CompilationDto getById(@PathVariable(name = "compId") int compId) {
        log.info("GET \"/compilations/{}\"", compId);
        CompilationDto compilation = compilationService.getById(compId);
        log.debug("compilation = " + compilation);
        return compilation;
    }
}
