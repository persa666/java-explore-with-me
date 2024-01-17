package ru.practicum.service;

import org.springframework.data.domain.PageRequest;
import ru.practicum.dto.category.CategoryDto;
import ru.practicum.dto.category.NewCategoryDto;

import java.util.List;

public interface CategoryService {

    CategoryDto createCategory(NewCategoryDto newCategoryDto);

    void deleteCategory(Long catId);

    CategoryDto replaceCategory(NewCategoryDto newCategoryDto, Long catId);

    List<CategoryDto> getAllCategories(PageRequest pageRequest);

    CategoryDto getCategoryById(Long catId);
}
