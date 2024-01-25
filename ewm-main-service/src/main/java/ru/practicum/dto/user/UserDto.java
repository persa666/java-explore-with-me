package ru.practicum.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    @NotEmpty
    @NotBlank
    private String name;

    @PositiveOrZero
    private Long id;

    @Email
    @NotEmpty
    private String email;

    @NotNull
    private Boolean permission;
}
