package ru.yandex.practicum.category.service;

import ru.yandex.practicum.category.CategoryDto;

public interface CategoryServiceAdmin {
    CategoryDto create(CategoryDto categoryDto);

    CategoryDto patch(CategoryDto categoryDto, long catId);

    void delete(long catId);
}
