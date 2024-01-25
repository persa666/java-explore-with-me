package ru.practicum.service;

import org.springframework.data.domain.PageRequest;
import ru.practicum.dto.event.EventFullDto;
import ru.practicum.dto.event.EventShortDto;
import ru.practicum.dto.event.NewEventDto;
import ru.practicum.dto.request.UpdateEventAdminRequest;
import ru.practicum.dto.request.UpdateEventUserRequest;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface EventService {
    EventFullDto createEvent(Long userId, NewEventDto newEventDto);

    List<EventShortDto> getEventsByUserId(Long userId, PageRequest pageRequest);

    EventFullDto getEventFullDtoByUserIdAndEventId(Long userId, Long eventId);

    EventFullDto replaceEventByUserIdAndEventId(Long userId, Long eventId, UpdateEventUserRequest request);

    EventFullDto replaceEventByEventIdAnAdmin(Long eventId, UpdateEventAdminRequest request);

    EventFullDto getEventById(Long eventId, HttpServletRequest request);

    List<EventFullDto> getEvents(Long[] users, String[] states, Long[] categories, String rangeStart,
                                 String rangeEnd, PageRequest pageRequest);

    List<EventShortDto> getEventsByFilter(String text, Long[] categories, Boolean paid, String rangeStart,
                                          String rangeEnd, Boolean onlyAvailable, String sort, PageRequest pageRequest,
                                          HttpServletRequest request);

    List<EventShortDto> getActualEventsBySubscriberId(Long userId, PageRequest pageRequest);
}
