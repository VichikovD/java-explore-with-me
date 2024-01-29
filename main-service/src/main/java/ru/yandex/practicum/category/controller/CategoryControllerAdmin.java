package ru.yandex.practicum.category.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.category.CategoryDto;
import ru.yandex.practicum.category.service.CategoryServiceAdmin;

@Slf4j
@RequestMapping("/admin/categories")
@RestController
@RequiredArgsConstructor
@Validated
public class CategoryControllerAdmin {
    final CategoryServiceAdmin categoryService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryDto create(@RequestBody @Validated CategoryDto categoryDto) {
        log.info("POST \"/admin/categories\" Body={}", categoryDto);
        CategoryDto categoryToReturn = categoryService.create(categoryDto);
        log.debug("Created category=" + categoryToReturn);
        return categoryToReturn;
    }

    @PatchMapping("/{catId}")
    public CategoryDto patch(@RequestBody @Validated CategoryDto categoryDto,
                             @PathVariable(name = "catId") long catId) {
        log.info("PATCH \"/admin/categories/{}\"", catId);
        CategoryDto categoryToReturn = categoryService.patch(categoryDto, catId);
        log.debug("Updated category with id=" + catId + System.lineSeparator() + "Category=" + categoryToReturn);
        return categoryToReturn;
    }

    @DeleteMapping("/{catId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable(name = "catId") long catId) {
        log.info("DELETE \"/admin/categories/{}\"", catId);
        categoryService.delete(catId);
        log.debug("Deleted category with id=" + catId);
    }
}
