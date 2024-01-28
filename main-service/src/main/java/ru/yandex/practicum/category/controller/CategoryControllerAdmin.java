package ru.yandex.practicum.category.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.category.CategoryDto;
import ru.yandex.practicum.category.service.CategoryServiceAdmin;

@Slf4j
@RequestMapping("/admin/categories")
@RestController
@RequiredArgsConstructor
public class CategoryControllerAdmin {
    final CategoryServiceAdmin categoryService;

    @PostMapping
    public CategoryDto create(@RequestBody CategoryDto categoryDto) {
        log.info("POST \"/admin/categories\" Body={}", categoryDto);
        CategoryDto category = categoryService.create(categoryDto);
        log.debug("category = " + category);
        return category;
    }
}
