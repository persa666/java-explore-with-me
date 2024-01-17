package ru.practicum.dto.event;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.constraints.FutureTimeTwoHours;

import javax.validation.constraints.*;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewEventDto {
    @NotEmpty
    @NotBlank
    @Size(min = 20, max = 2000)
    private String annotation;

    @PositiveOrZero
    @NotNull
    private Long category;

    @NotEmpty
    @NotBlank
    @Size(min = 20, max = 7000)
    private String description;

    @FutureTimeTwoHours
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @NotNull
    private LocalDateTime eventDate;

    private Location location;

    private Boolean paid = false;

    private Integer participantLimit = 0;

    private Boolean requestModeration = true;

    @NotEmpty
    @NotBlank
    @Size(min = 3, max = 120)
    private String title;
}
