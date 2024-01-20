package ru.practicum.dto.category;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryDto {

    @PositiveOrZero
    private Long id;

    @NotEmpty
    @NotBlank
    @Size(min = 1, max = 50, message = "Длина строки не должна превышать 50 символов")
    private String name;

    public CategoryDto(String name) {
        this.name = name;
    }
}
