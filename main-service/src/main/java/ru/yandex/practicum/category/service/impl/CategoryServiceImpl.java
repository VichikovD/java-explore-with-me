package ru.yandex.practicum.category.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.category.Category;
import ru.yandex.practicum.category.CategoryDto;
import ru.yandex.practicum.category.CategoryMapper;
import ru.yandex.practicum.category.CategoryRepository;
import ru.yandex.practicum.category.service.CategoryService;
import ru.yandex.practicum.exception.NotFoundException;

import java.util.List;

@RequiredArgsConstructor
@Service
public class CategoryServiceImpl implements CategoryService {
    final CategoryRepository categoryRepository;

    @Override
    public CategoryDto create(CategoryDto categoryDto) {
        Category categoryToSave = CategoryMapper.toModel(categoryDto);
        Category categorySaved = categoryRepository.save(categoryToSave);
        return CategoryMapper.toDto(categorySaved);
    }

    @Override
    public CategoryDto patch(CategoryDto categoryDto, long catId) {
        Category categoryToChange = categoryRepository.findById(catId)
                .orElseThrow(() -> new NotFoundException("Category with id=" + catId + " was not found"));
        CategoryMapper.updateByDto(categoryToChange, categoryDto);
        Category categorySaved = categoryRepository.save(categoryToChange);
        return CategoryMapper.toDto(categorySaved);
    }

    @Override
    public void delete(long catId) {
        categoryRepository.findById(catId)
                .orElseThrow(() -> new NotFoundException("Category with id=" + catId + " was not found"));
        categoryRepository.deleteById(catId);
    }

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
