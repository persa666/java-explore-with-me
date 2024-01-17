package ru.practicum.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.request.EventRequestStatusUpdateRequest;
import ru.practicum.dto.request.EventRequestStatusUpdateResult;
import ru.practicum.dto.request.ParticipationRequestDto;
import ru.practicum.service.RequestService;

import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Validated
public class RequestController {
    private final RequestService requestService;

    @PostMapping("/users/{userId}/requests")
    @ResponseStatus(HttpStatus.CREATED)
    public ParticipationRequestDto createParticipationRequest(@PathVariable(name = "userId")
                                                              @PositiveOrZero Long userId,
                                                              @RequestParam(name = "eventId")
                                                              @PositiveOrZero Long eventId) {
        return requestService.createParticipationRequest(userId, eventId);
    }

    @GetMapping("/users/{userId}/events/{eventId}/requests")
    public List<ParticipationRequestDto> getRequestsByEventIdAndUserId(@PathVariable(name = "userId")
                                                                       @PositiveOrZero Long userId,
                                                                       @PathVariable(name = "eventId")
                                                                       @PositiveOrZero Long eventId) {
        return requestService.getRequestsByEventIdAndUserId(userId, eventId);
    }

    @PatchMapping("/users/{userId}/events/{eventId}/requests")
    public EventRequestStatusUpdateResult replaceStateRequests(@PathVariable(name = "userId")
                                                               @PositiveOrZero Long userId,
                                                               @PathVariable(name = "eventId")
                                                               @PositiveOrZero Long eventId,
                                                               @RequestBody EventRequestStatusUpdateRequest request) {
        return requestService.replaceStateRequests(userId, eventId, request);
    }

    @PatchMapping("/users/{userId}/requests/{requestId}/cancel")
    public ParticipationRequestDto cancelRequest(@PathVariable(name = "userId") @PositiveOrZero Long userId,
                                                 @PathVariable(name = "requestId") @PositiveOrZero Long requestId) {
        return requestService.cancelRequest(userId, requestId);
    }

    @GetMapping("/users/{userId}/requests")
    public List<ParticipationRequestDto> getRequestsByRequesterId(@PathVariable(name = "userId")
                                                                  @PositiveOrZero Long userId) {
        return requestService.getRequestsByRequesterId(userId);
    }
}
