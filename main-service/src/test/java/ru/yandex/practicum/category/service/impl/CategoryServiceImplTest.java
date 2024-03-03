package ru.yandex.practicum.category.service.impl;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.yandex.practicum.category.Category;
import ru.yandex.practicum.category.CategoryDto;
import ru.yandex.practicum.category.CategoryRepository;
import ru.yandex.practicum.exception.NotFoundException;

import java.util.Optional;

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
        CategoryDto categoryDtoReceived = CategoryDto.builder()
                .id(null)
                .name("UpdatedName")
                .build();
        Category categoryInDB = getCategory();
        Category categoryUpdated = Category.builder()
                .id(1L)
                .name("UpdatedName")
                .build();
        Mockito.when(categoryRepository.findById(1L))
                .thenReturn(Optional.of(categoryInDB));
        Mockito.when(categoryRepository.save(Mockito.any(Category.class)))
                .thenReturn(categoryUpdated);

        CategoryDto actualUpdatedCategoryDto = categoryService.patch(categoryDtoReceived, 1L);

        assertThat(actualUpdatedCategoryDto.getId(), Matchers.is(1L));
        assertThat(actualUpdatedCategoryDto.getName(), Matchers.is("UpdatedName"));
        Mockito.verify(categoryRepository, Mockito.times(1))
                .findById(1L);
        Mockito.verify(categoryRepository, Mockito.times(1))
                .save(Mockito.any(Category.class));
    }

    @Test
    void patch_whenFindByIdReturnOptionalEmpty_thenThrowsException() {
        CategoryDto categoryDtoReceived = CategoryDto.builder()
                .id(null)
                .name("UpdatedName")
                .build();
        Mockito.when(categoryRepository.findById(1L))
                .thenReturn(Optional.empty());

        Exception notFound = Assertions.assertThrows(NotFoundException.class, () -> categoryService.patch(categoryDtoReceived, 1L));
        assertThat(notFound.getMessage(), Matchers.is("Category with id=1 was not found"));
        Mockito.verify(categoryRepository, Mockito.never())
                .save(Mockito.any(Category.class));
    }

    @Test
    void delete() {
        Mockito.when(categoryRepository.findById(1L))
                .thenReturn(Optional.of(getCategory()));

        categoryService.delete(1L);

        Mockito.verify(categoryRepository, Mockito.times(1))
                .findById(1L);
        Mockito.verify(categoryRepository, Mockito.times(1))
                .deleteById(1L);
    }

    @Test
    void delete_whenFindByIdReturnOptionalEmpty_thenThrowsException() {
        Mockito.when(categoryRepository.findById(1L))
                .thenReturn(Optional.empty());

        Exception notFound = Assertions.assertThrows(NotFoundException.class, () -> categoryService.delete(1L));

        assertThat(notFound.getMessage(), Matchers.is("Category with id=1 was not found"));
        Mockito.verify(categoryRepository, Mockito.times(1))
                .findById(1L);
        Mockito.verify(categoryRepository, Mockito.never())
                .deleteById(Mockito.any(long.class));
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

    private CategoryDto getCategoryDto() {
        return CategoryDto.builder()
                .id(1L)
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