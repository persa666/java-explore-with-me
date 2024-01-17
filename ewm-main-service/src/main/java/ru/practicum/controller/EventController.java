package ru.practicum.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.event.EventFullDto;
import ru.practicum.dto.event.EventShortDto;
import ru.practicum.dto.event.NewEventDto;
import ru.practicum.dto.request.UpdateEventAdminRequest;
import ru.practicum.dto.request.UpdateEventUserRequest;
import ru.practicum.service.EventService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@Validated
@RequiredArgsConstructor
public class EventController {
    private final EventService eventService;

    @PostMapping("/users/{userId}/events")
    @ResponseStatus(HttpStatus.CREATED)
    public EventFullDto createEvent(@PathVariable(name = "userId") @PositiveOrZero Long userId,
                                    @RequestBody @Valid NewEventDto newEventDto) {
        return eventService.createEvent(userId, newEventDto);
    }

    @GetMapping("/users/{userId}/events")
    public List<EventShortDto> getEventsByUserId(@PathVariable(name = "userId") @PositiveOrZero Long userId,
                                                 @RequestParam(required = false, name = "from", defaultValue = "0")
                                                 @PositiveOrZero int from,
                                                 @RequestParam(required = false, name = "size", defaultValue = "10")
                                                 @Positive int size) {
        return eventService.getEventsByUserId(userId, PageRequest.of(from / size, size));
    }

    @GetMapping("/users/{userId}/events/{eventId}")
    public EventFullDto getEventFullDtoByUserIdAndEventId(@PathVariable(name = "userId") @PositiveOrZero Long userId,
                                                          @PathVariable(name = "eventId")
                                                          @PositiveOrZero Long eventId) {
        return eventService.getEventFullDtoByUserIdAndEventId(userId, eventId);
    }

    @PatchMapping("/users/{userId}/events/{eventId}")
    public EventFullDto replaceEventByUserIdAndEventId(@PathVariable(name = "userId") @PositiveOrZero Long userId,
                                                       @PathVariable(name = "eventId") @PositiveOrZero Long eventId,
                                                       @RequestBody @Valid UpdateEventUserRequest request) {
        return eventService.replaceEventByUserIdAndEventId(userId, eventId, request);
    }

    @PatchMapping("/admin/events/{eventId}")
    public EventFullDto replaceEventByEventIdAnAdmin(@PathVariable(name = "eventId") @PositiveOrZero Long eventId,
                                                     @RequestBody @Valid UpdateEventAdminRequest request) {
        return eventService.replaceEventByEventIdAnAdmin(eventId, request);
    }

    @GetMapping("/events/{id}")
    public EventFullDto getEventById(@PathVariable(name = "id") @PositiveOrZero Long eventId,
                                     HttpServletRequest request) {
        return eventService.getEventById(eventId, request);
    }

    @GetMapping("/admin/events")
    public List<EventFullDto> getEvents(@RequestParam(required = false, name = "users", defaultValue = "") Long[] users,
                                        @RequestParam(required = false, name = "states",
                                                defaultValue = "") String[] states,
                                        @RequestParam(required = false, name = "categories",
                                                defaultValue = "") Long[] categories,
                                        @RequestParam(required = false, name = "rangeStart") String rangeStart,
                                        @RequestParam(required = false, name = "rangeEnd") String rangeEnd,
                                        @RequestParam(required = false, name = "from", defaultValue = "0")
                                        @PositiveOrZero int from,
                                        @RequestParam(required = false, name = "size", defaultValue = "10")
                                        @Positive int size) {
        return eventService.getEvents(users, states, categories, rangeStart, rangeEnd,
                PageRequest.of(from / size, size));
    }

    @GetMapping("/events")
    public List<EventShortDto> getEventsByFilter(@RequestParam(required = false, name = "text", defaultValue = "")
                                                 String text,
                                                 @RequestParam(required = false, name = "categories",
                                                         defaultValue = "") Long[] categories,
                                                 @RequestParam(required = false, name = "paid") Boolean paid,
                                                 @RequestParam(required = false, name = "rangeStart") String rangeStart,
                                                 @RequestParam(required = false, name = "rangeEnd") String rangeEnd,
                                                 @RequestParam(required = false, name = "onlyAvailable",
                                                         defaultValue = "false") Boolean onlyAvailable,
                                                 @RequestParam(required = false, name = "sort") String sort,
                                                 @RequestParam(required = false, name = "from", defaultValue = "0")
                                                 @PositiveOrZero int from,
                                                 @RequestParam(required = false, name = "size", defaultValue = "10")
                                                 @Positive int size, HttpServletRequest request) {
        return eventService.getEventsByFilter(text, categories, paid, rangeStart, rangeEnd, onlyAvailable, sort,
                PageRequest.of(from / size, size), request);
    }
}
