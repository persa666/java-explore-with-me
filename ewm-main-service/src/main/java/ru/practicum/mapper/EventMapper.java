package ru.practicum.mapper;

import ru.practicum.dto.event.EventFullDto;
import ru.practicum.dto.event.EventShortDto;
import ru.practicum.dto.event.NewEventDto;
import ru.practicum.model.Event;
import ru.practicum.model.State;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class EventMapper {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static Event toEvent(NewEventDto newEventDto) {
        return new Event(
                newEventDto.getAnnotation(),
                null,
                LocalDateTime.now(),
                newEventDto.getDescription(),
                newEventDto.getEventDate(),
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                State.PENDING,
                newEventDto.getTitle()
        );
    }

    public static EventFullDto toEventFullDto(Event event) {
        return new EventFullDto(
                event.getAnnotation(),
                CategoryMapper.toCategoryDto(event.getCategory()),
                0,
                event.getCreatedOn().format(formatter),
                event.getDescription(),
                event.getEventDate().format(formatter),
                event.getId(),
                UserMapper.toUserShortDto(event.getInitiator()),
                LocationMapper.toLocation(event.getPosition()),
                event.getPaid(),
                event.getParticipantLimit(),
                null,
                event.getRequestModeration(),
                event.getState(),
                event.getTitle(),
                0L
        );
    }

    public static EventShortDto toEventShortDto(Event event) {
        return new EventShortDto(
                event.getAnnotation(),
                CategoryMapper.toCategoryDto(event.getCategory()),
                0,
                event.getEventDate().format(formatter),
                event.getId(),
                UserMapper.toUserShortDto(event.getInitiator()),
                event.getPaid(),
                event.getTitle(),
                0L
        );
    }

    public static EventShortDto fromFullToShortDto(EventFullDto eventFullDto) {
        return new EventShortDto(
                eventFullDto.getAnnotation(),
                eventFullDto.getCategory(),
                eventFullDto.getConfirmedRequests(),
                eventFullDto.getEventDate(),
                eventFullDto.getId(),
                eventFullDto.getInitiator(),
                eventFullDto.isPaid(),
                eventFullDto.getTitle(),
                eventFullDto.getViews()
        );
    }
}
