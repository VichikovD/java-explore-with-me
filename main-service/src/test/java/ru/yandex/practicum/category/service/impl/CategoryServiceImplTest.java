package ru.yandex.practicum.category.service.impl;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.yandex.practicum.category.Category;
import ru.yandex.practicum.category.CategoryDto;
import ru.yandex.practicum.category.CategoryRepository;

import static org.hamcrest.MatcherAssert.assertThat;

@ExtendWith(MockitoExtension.class)
class CategoryServiceImplTest {
    @Mock
    CategoryRepository categoryRepository;

    @InjectMocks
    CategoryServiceImpl categoryService;

    @Test
    void create() {
        CategoryDto categoryDtoNullId = getCategoryDtoNullId();
        Category categoryToReturn = getCategory();
        Mockito.when(categoryRepository.save(Mockito.any(Category.class)))
                .thenReturn(categoryToReturn);

        CategoryDto actualCategory = categoryService.create(categoryDtoNullId);

        assertThat(actualCategory.getId(), Matchers.is(1L));
        assertThat(actualCategory.getName(), Matchers.is("CategoryName"));
        Mockito.verify(categoryRepository, Mockito.times(1))
                .save(Mockito.any(Category.class));
    }

    @Test
    void patch() {
    }

    @Test
    void delete() {
    }

    @Test
    void getFiltered() {
    }

    @Test
    void getById() {
    }

    private CategoryDto getCategoryDtoNullId() {
        return CategoryDto.builder()
                .id(null)
                .name("CategoryName")
                .build();
    }

    private Category getCategory() {
        return Category.builder()
                .id(1L)
                .name("CategoryName")
                .build();
    }
}