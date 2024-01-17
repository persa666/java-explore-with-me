package ru.practicum.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.category.CategoryDto;
import ru.practicum.dto.category.NewCategoryDto;
import ru.practicum.service.CategoryService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Validated
public class CategoryController {
    private final CategoryService categoryService;

    @PostMapping("/admin/categories")
    @ResponseStatus(value = HttpStatus.CREATED)
    public CategoryDto createCategory(@RequestBody @Valid NewCategoryDto newCategoryDto) {
        return categoryService.createCategory(newCategoryDto);
    }

    @DeleteMapping("/admin/categories/{catId}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteCategory(@PathVariable(name = "catId") @PositiveOrZero Long catId) {
        categoryService.deleteCategory(catId);
    }

    @PatchMapping("/admin/categories/{catId}")
    public CategoryDto replaceCategory(@RequestBody @Valid NewCategoryDto newCategoryDto,
                                       @PathVariable(name = "catId") @PositiveOrZero Long catId) {
        return categoryService.replaceCategory(newCategoryDto, catId);
    }

    @GetMapping("/categories")
    public List<CategoryDto> getAllCategories(@RequestParam(required = false, name = "from", defaultValue = "0")
                                              @PositiveOrZero int from,
                                              @RequestParam(required = false, name = "size", defaultValue = "10")
                                              @Positive int size) {
        return categoryService.getAllCategories(PageRequest.of(from / size, size));
    }

    @GetMapping("/categories/{catId}")
    public CategoryDto getCategoryById(@PathVariable(name = "catId") @PositiveOrZero Long catId) {
        return categoryService.getCategoryById(catId);
    }
}
