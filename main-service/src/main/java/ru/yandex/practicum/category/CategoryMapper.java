package ru.yandex.practicum.category;

import java.util.ArrayList;
import java.util.List;

public class CategoryMapper {
    public static Category toModel(CategoryDto categoryDto) {
        return Category.builder()
                .name(categoryDto.getName())
                .build();
    }

    public static CategoryDto toDto(Category category) {
        return CategoryDto.builder()
                .id(category.getId())
                .name(category.getName())
                .build();
    }

    public static List<CategoryDto> listToDtoList(List<Category> categoryList) {
        List<CategoryDto> categoryDtoList = new ArrayList<>();
        for (Category category : categoryList) {
            categoryDtoList.add(toDto(category));
        }
        return categoryDtoList;
    }

    public static void updateByDto(Category category, CategoryDto compilationDto) {
        category.setName(compilationDto.getName());
    }
}
