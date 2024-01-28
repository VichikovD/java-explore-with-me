package ru.yandex.practicum.category.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.category.Category;
import ru.yandex.practicum.category.CategoryDto;
import ru.yandex.practicum.category.CategoryMapper;
import ru.yandex.practicum.category.CategoryRepository;
import ru.yandex.practicum.category.service.CategoryServiceAdmin;

@RequiredArgsConstructor
@Service
public class CategoryServiceAdminImpl implements CategoryServiceAdmin {
    final CategoryRepository categoryRepository;

    @Override
    public CategoryDto create(CategoryDto categoryDto) {
        Category categoryToSave = CategoryMapper.toModel(categoryDto);
        Category categorySaved = categoryRepository.save(categoryToSave);
        return CategoryMapper.toDto(categorySaved);
    }
}
