package ru.yandex.practicum.category.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.category.CategoryDto;
import ru.yandex.practicum.category.service.CategoryServicePublic;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
public class CategoryControllerPublic {
    final CategoryServicePublic categoryService;

    @GetMapping
    public List<CategoryDto> getFiltered(@RequestParam(name = "from", defaultValue = "0") int offset,
                                         @RequestParam(name = "size", defaultValue = "10") int size) {
        log.info("GET \"/categories?from={}&size={}\"", offset, size);
        Sort sort = Sort.by(Sort.Direction.DESC, "views");
        List<CategoryDto> categoryList = categoryService.getFiltered(offset, size, sort);
        log.debug("categoryList = " + categoryList);
        return categoryList;
    }

    @GetMapping("/{catId}")
    public CategoryDto getById(@PathVariable(name = "catId") int catId) {
        log.info("GET \"/categories/{}", catId);
        CategoryDto category = categoryService.getById(catId);
        log.debug("category = " + category);
        return category;
    }
}
