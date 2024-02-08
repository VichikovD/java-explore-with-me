package ru.yandex.practicum.category.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.category.CategoryDto;
import ru.yandex.practicum.category.service.CategoryService;
import ru.yandex.practicum.util.OffsetPageable;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
public class CategoryPublicController {
    final CategoryService categoryService;

    @GetMapping
    public List<CategoryDto> getFiltered(@RequestParam(name = "from", defaultValue = "0") int offset,
                                         @RequestParam(name = "size", defaultValue = "10") int limit) {
        log.info("GET \"/categories?from={}&size={}\"", offset, limit);
        Sort sort = Sort.by(Sort.Direction.ASC, "id");
        Pageable pageable = new OffsetPageable(offset, limit, sort);
        List<CategoryDto> categoryList = categoryService.getFiltered(pageable);
        log.debug("categoryList = " + categoryList);
        return categoryList;
    }

    @GetMapping("/{catId}")
    public CategoryDto getById(@PathVariable(name = "catId") long catId) {
        log.info("GET \"/categories/{}", catId);
        CategoryDto category = categoryService.getById(catId);
        log.debug("category = " + category);
        return category;
    }
}
