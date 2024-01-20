package ru.practicum.dto.compilation;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewCompilationDto {
    private Long[] events;

    private Boolean pinned = false;

    @NotEmpty
    @NotBlank
    @Size(min = 1, max = 50)

    private String title;
}
