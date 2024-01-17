package ru.practicum.dto.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.dto.category.CategoryDto;
import ru.practicum.dto.user.UserShortDto;
import ru.practicum.model.State;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventFullDto {

    @NotEmpty
    @NotBlank
    private String annotation;

    @NotNull
    private CategoryDto category;

    private Integer confirmedRequests;

    private String createdOn;

    private String description;

    @NotEmpty
    private String eventDate;

    private Long id;

    @NotNull
    private UserShortDto initiator;

    @NotNull
    private Location location;

    @NotEmpty
    private boolean paid;

    private int participantLimit;

    private String publishedOn;

    private boolean requestModeration;

    private State state;

    @NotEmpty
    @NotBlank
    private String title;

    private Long views;
}
