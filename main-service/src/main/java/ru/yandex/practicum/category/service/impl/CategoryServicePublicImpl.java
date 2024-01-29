package ru.yandex.practicum.category.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.category.Category;
import ru.yandex.practicum.category.CategoryDto;
import ru.yandex.practicum.category.CategoryMapper;
import ru.yandex.practicum.category.CategoryRepository;
import ru.yandex.practicum.category.service.CategoryServicePublic;
import ru.yandex.practicum.exception.NotFoundException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServicePublicImpl implements CategoryServicePublic {
    final CategoryRepository categoryRepository;

    @Override
    public List<CategoryDto> getFiltered(Pageable pageable) {
        List<Category> categoryList = categoryRepository.findAll(pageable).getContent();
        return CategoryMapper.listToDtoList(categoryList);
    }

    @Override
    public CategoryDto getById(long compId) {
        Category category = categoryRepository.findById(compId)
                .orElseThrow(() -> new NotFoundException("Category with id=" + compId + " was not found"));
        return CategoryMapper.toDto(category);
    }
}
