package ru.practicum.mapper;

import ru.practicum.dto.request.ParticipationRequestDto;
import ru.practicum.model.ParticipationRequest;

public class ParticipationRequestMapper {
    public static ParticipationRequestDto toParticipationRequestDto(ParticipationRequest participationRequest) {
        return new ParticipationRequestDto(
                participationRequest.getCreated(),
                participationRequest.getEvent().getId(),
                participationRequest.getId(),
                participationRequest.getRequester().getId(),
                participationRequest.getStatus()
        );
    }
}
