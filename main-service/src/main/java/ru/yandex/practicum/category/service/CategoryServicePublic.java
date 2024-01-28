package ru.yandex.practicum.category.service;

import org.springframework.data.domain.Sort;
import ru.yandex.practicum.category.CategoryDto;

import java.util.List;


public interface CategoryServicePublic {
    List<CategoryDto> getFiltered(int offset, int size, Sort sort);

    CategoryDto getById(int compId);

}
