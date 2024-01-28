package ru.yandex.practicum.category.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.category.CategoryDto;
import ru.yandex.practicum.category.CategoryRepository;
import ru.yandex.practicum.category.service.CategoryServicePublic;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryServicePublic {
    final CategoryRepository categoryRepository;

    @Override
    public List<CategoryDto> getFiltered(int offset, int size, Sort sort) {
        return null;
    }

    @Override
    public CategoryDto getById(int compId) {
        return null;
    }
}
