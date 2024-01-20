package ru.practicum.service;

import ru.practicum.dto.request.EventRequestStatusUpdateRequest;
import ru.practicum.dto.request.EventRequestStatusUpdateResult;
import ru.practicum.dto.request.ParticipationRequestDto;

import java.util.List;

public interface RequestService {
    ParticipationRequestDto createParticipationRequest(Long userId, Long eventId);

    List<ParticipationRequestDto> getRequestsByEventIdAndUserId(Long userId, Long eventId);

    EventRequestStatusUpdateResult replaceStateRequests(Long userId, Long eventId,
                                                        EventRequestStatusUpdateRequest request);

    ParticipationRequestDto cancelRequest(Long userId, Long requestId);

    List<ParticipationRequestDto> getRequestsByRequesterId(Long userId);
}
