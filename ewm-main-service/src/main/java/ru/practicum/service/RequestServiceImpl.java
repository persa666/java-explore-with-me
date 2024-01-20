package ru.practicum.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.dto.request.EventRequestStatusUpdateRequest;
import ru.practicum.dto.request.EventRequestStatusUpdateResult;
import ru.practicum.dto.request.ParticipationRequestDto;
import ru.practicum.exp.*;
import ru.practicum.mapper.ParticipationRequestMapper;
import ru.practicum.model.*;
import ru.practicum.repository.EventRepository;
import ru.practicum.repository.RequestRepository;
import ru.practicum.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RequestServiceImpl implements RequestService {
    private final RequestRepository requestRepository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;

    @Override
    public ParticipationRequestDto createParticipationRequest(Long userId, Long eventId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NonExistentUserException(userId));
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new NonExistentEventException(eventId));
        Integer confirmedRequests = requestRepository.findCountByEventId(eventId);
        if (user.getId().equals(event.getInitiator().getId()) || event.getState().equals(State.PENDING)
                || (event.getParticipantLimit() <= confirmedRequests) && (event.getParticipantLimit() != 0)) {
            throw new ParticipationRequestException("Инициатор события не может добавить запрос на участие в своём " +
                    "событии/нельзя участвовать в неопубликованном событии/достигнут лимит запросов на участие");
        }
        ParticipationRequest request = new ParticipationRequest(
                LocalDateTime.now(),
                event,
                user,
                State.PENDING.toString()
        );
        if (!event.getRequestModeration() || event.getParticipantLimit().equals(0)) {
            request.setStatus(StateParticipation.CONFIRMED.toString());
        }
        return ParticipationRequestMapper.toParticipationRequestDto(requestRepository.save(request));
    }

    @Override
    public List<ParticipationRequestDto> getRequestsByEventIdAndUserId(Long userId, Long eventId) {
        return requestRepository.findByEventId(eventId)
                .stream()
                .map(ParticipationRequestMapper::toParticipationRequestDto)
                .collect(Collectors.toList());
    }

    @Override
    public EventRequestStatusUpdateResult replaceStateRequests(Long userId, Long eventId,
                                                               EventRequestStatusUpdateRequest request) {
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new NonExistentEventException(eventId));
        Integer count = requestRepository.findCountByEventId(eventId);
        if (!event.getParticipantLimit().equals(0)) {
            if (event.getParticipantLimit() <= count) {
                throw new EventException("Превышено количество запросов на участие.");
            }
        }
        List<ParticipationRequest> requests = requestRepository.findAllById(Arrays.asList(request.getRequestIds()));
        List<ParticipationRequest> newRequests = new ArrayList<>();
        List<ParticipationRequestDto> confirmedRequests = new ArrayList<>();
        List<ParticipationRequestDto> rejectedRequests = new ArrayList<>();
        if (event.getParticipantLimit() != 0 || !event.getRequestModeration()) {
            ParticipationRequest participationRequest;
            ParticipationRequestDto participationRequestDto;
            String state = request.getStatus().toString();
            for (ParticipationRequest value : requests) {
                participationRequest = value;
                if (!participationRequest.getStatus().equals(State.PENDING.toString())) {
                    throw new ParticipationRequestException("Заявка не в статусе ожидания.");
                }
                if (state.equals(StateParticipation.CONFIRMED.toString()) && count < event.getParticipantLimit()) {
                    count++;
                } else {
                    state = StateParticipation.REJECTED.toString();
                }
                participationRequest.setStatus(state);
                participationRequestDto =
                        ParticipationRequestMapper.toParticipationRequestDto(participationRequest);
                if (participationRequest.getStatus().equals(StateParticipation.CONFIRMED.toString())) {
                    confirmedRequests.add(participationRequestDto);
                } else {
                    rejectedRequests.add(participationRequestDto);
                }
                newRequests.add(participationRequest);
            }
            requestRepository.saveAll(newRequests);
        }
        return new EventRequestStatusUpdateResult(confirmedRequests, rejectedRequests);
    }

    @Override
    public ParticipationRequestDto cancelRequest(Long userId, Long requestId) {
        ParticipationRequest request = requestRepository.findById(requestId)
                .orElseThrow(() -> new NonExistentParticipationRequestException(requestId));
        request.setStatus(State.CANCELED.toString());
        return ParticipationRequestMapper.toParticipationRequestDto(requestRepository.save(request));
    }

    @Override
    public List<ParticipationRequestDto> getRequestsByRequesterId(Long userId) {
        userRepository.findById(userId).orElseThrow(() -> new NonExistentUserException(userId));
        return requestRepository.findByRequesterId(userId)
                .stream()
                .map(ParticipationRequestMapper::toParticipationRequestDto)
                .collect(Collectors.toList());
    }
}
