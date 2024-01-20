package ru.practicum.service;

import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.StatsClient;
import ru.practicum.dto.event.EventFullDto;
import ru.practicum.dto.event.EventShortDto;
import ru.practicum.dto.event.NewEventDto;
import ru.practicum.dto.request.UpdateEventAdminRequest;
import ru.practicum.dto.request.UpdateEventUserRequest;
import ru.practicum.exp.*;
import ru.practicum.hit.HitDto;
import ru.practicum.hit.HitShort;
import ru.practicum.mapper.EventMapper;
import ru.practicum.mapper.LocationMapper;
import ru.practicum.model.*;
import ru.practicum.repository.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {
    private final EventRepository eventRepository;
    private final PositionRepository positionRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final RequestRepository requestRepository;
    private final SubscriptionRepository subscriptionRepository;

    private final StatsClient statsClient;
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public EventFullDto createEvent(Long userId, NewEventDto newEventDto) {
        Event event = EventMapper.toEvent(newEventDto);
        event.setCategory(categoryRepository.findById(newEventDto
                .getCategory()).orElseThrow(() -> new NonExistentCategoryException(newEventDto.getCategory())));
        event.setInitiator(userRepository.findById(userId).orElseThrow(() -> new NonExistentUserException(userId)));
        if (newEventDto.getPaid() != null) {
            event.setPaid(newEventDto.getPaid());
        }
        if (newEventDto.getParticipantLimit() != null) {
            event.setParticipantLimit(newEventDto.getParticipantLimit());
        }
        if (newEventDto.getRequestModeration() != null) {
            event.setRequestModeration(newEventDto.getRequestModeration());
        }
        Position position = positionRepository.findByLatAndLon(newEventDto.getLocation().getLat(),
                newEventDto.getLocation().getLon());
        event.setPosition(Objects
                .requireNonNullElseGet(position,
                        () -> positionRepository.save(LocationMapper.toPosition(newEventDto.getLocation()))));
        if (event.getRequestModeration()) {
            event.setPublishedOn(LocalDateTime.now());
        }
        event = eventRepository.save(event);
        EventFullDto eventFullDto = EventMapper.toEventFullDto(event);
        if (event.getPublishedOn() != null) {
            eventFullDto.setPublishedOn(event.getPublishedOn().format(formatter));
        }
        return EventMapper.toEventFullDto(eventRepository.save(event));
    }

    @Override
    public List<EventShortDto> getEventsByUserId(Long userId, PageRequest pageRequest) {
        List<EventShortDto> list = eventRepository.findByInitiatorId(userId, pageRequest)
                .stream()
                .map(EventMapper::toEventShortDto)
                .collect(Collectors.toList());
        return getEventsWithConfirmedRequests(list);
    }

    private List<EventShortDto> getEventsWithConfirmedRequests(List<EventShortDto> list) {
        List<Long> eventIds = list.stream()
                .map(EventShortDto::getId)
                .collect(Collectors.toList());
        List<Integer> confirmedRequests = requestRepository.findCountByEventIdIn(eventIds);
        if (eventIds.isEmpty()) {
            return list;
        }
        if (confirmedRequests.size() > 0) {
            IntStream.range(0, list.size())
                    .forEachOrdered(i -> list.get(i).setConfirmedRequests(confirmedRequests.get(i)));
        }
        return getEventsWithViews(list, eventIds);
    }

    private List<EventShortDto> getEventsWithViews(List<EventShortDto> list, List<Long> eventIds) {
        Map<String, Object> parameters = Map.of("start", LocalDateTime.now().minusDays(5).format(formatter),
                "end", LocalDateTime.now().format(formatter), "uris", eventIds.stream()
                        .map(number -> "/events/" + number).toArray(), "unique", true);
        List<HitShort> views = (List<HitShort>) statsClient.getState(parameters).getBody();
        if (views != null) {
            if (views.size() != 0) {
                for (int i = 0; i < views.size(); i++) {
                    LinkedHashMap<String, String> map = (LinkedHashMap<String, String>) views.get(i);
                    Long eventId = Long.valueOf(map.get("uri").substring(8));
                    LinkedHashMap<String, Integer> counts = (LinkedHashMap<String, Integer>) views.get(i);
                    Long count = Long.valueOf(counts.get("hits"));
                    int index = -1;
                    for (int j = 0; j < list.size(); j++) {
                        if (list.get(j).getId().equals(eventId)) {
                            index = j;
                            break;
                        }
                    }
                    EventShortDto event = list.get(index);
                    event.setViews(count);
                    list.set(index, event);
                }
            }
        }
        return list;
    }

    @Override
    public EventFullDto getEventFullDtoByUserIdAndEventId(Long userId, Long eventId) {
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new NonExistentEventException(eventId));
        if (!event.getInitiator().getId().equals(userId)) {
            throw new EventException(eventId);
        }
        if (event.getPublishedOn() == null) {
            return EventMapper.toEventFullDto(event);
        }
        EventFullDto eventFullDto = EventMapper.toEventFullDto(event);
        eventFullDto.setPublishedOn(event.getPublishedOn().format(formatter));
        return getEventWithConfirmedRequests(eventFullDto);
    }

    private EventFullDto getEventWithConfirmedRequests(EventFullDto eventFullDto) {
        eventFullDto.setConfirmedRequests(requestRepository.findCountByEventId(eventFullDto.getId()));
        return getEventWithViews(eventFullDto);
    }

    private EventFullDto getEventWithViews(EventFullDto eventFullDto) {
        Map<String, Object> parameters = Map.of("start", LocalDateTime.now().minusDays(5).format(formatter),
                "end", LocalDateTime.now().format(formatter), "uris", "/events/" + eventFullDto.getId(),
                "unique", true);
        List<HitShort> views = (List<HitShort>) statsClient.getState(parameters).getBody();
        if (views != null) {
            if (views.size() != 0) {
                LinkedHashMap<String, Integer> map = (LinkedHashMap<String, Integer>) views.get(0);
                eventFullDto.setViews(Long.valueOf(map.get("hits")));
            }
        }
        return eventFullDto;
    }

    @Override
    public EventFullDto replaceEventByUserIdAndEventId(Long userId, Long eventId, UpdateEventUserRequest request) {
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new NonExistentEventException(eventId));
        if (!event.getInitiator().getId().equals(userId)) {
            throw new NonExistentEventException("Событие недоступно");
        }
        if (event.getState().equals(State.PUBLISHED)) {
            throw new EventException("Событие уже опубликовано.");
        }
        if (request.getAnnotation() != null) {
            event.setAnnotation(request.getAnnotation());
        }
        if (request.getCategory() != null) {
            Category category = categoryRepository.findById(request.getCategory())
                    .orElseThrow(() -> new NonExistentCategoryException(request.getCategory()));
            event.setCategory(category);
        }
        if (request.getDescription() != null) {
            event.setDescription(request.getDescription());
        }
        if (request.getEventDate() != null) {
            event.setEventDate(request.getEventDate());
        }
        if (request.getLocation() != null) {
            if (!request.getLocation().equals(LocationMapper.toLocation(event.getPosition()))) {
                event.setPosition(positionRepository.save(LocationMapper.toPosition(request.getLocation())));
            }
        }
        if (request.getPaid() != null) {
            event.setPaid(request.getPaid());
        }
        if (request.getParticipantLimit() != null) {
            event.setParticipantLimit(request.getParticipantLimit());
        }
        if (request.getRequestModeration() != null) {
            event.setRequestModeration(request.getRequestModeration());
        }
        if (request.getStateAction() != null) {
            if (request.getStateAction().equals(StateAction.CANCEL_REVIEW)) {
                event.setState(State.CANCELED);
            }
            if (request.getStateAction().equals(StateAction.SEND_TO_REVIEW)) {
                event.setState(State.PENDING);
            }
        }
        if (request.getTitle() != null) {
            event.setTitle(request.getTitle());
        }
        event = eventRepository.save(event);
        if (event.getPublishedOn() == null) {
            return EventMapper.toEventFullDto(event);
        }
        EventFullDto eventFullDto = EventMapper.toEventFullDto(event);
        eventFullDto.setPublishedOn(event.getPublishedOn().format(formatter));
        return getEventWithConfirmedRequests(eventFullDto);
    }

    @Override
    public EventFullDto replaceEventByEventIdAnAdmin(Long eventId, UpdateEventAdminRequest request) {
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new NonExistentEventException(eventId));
        if (!event.getState().equals(State.PENDING)) {
            throw new EventException("Событие не в ожидании публикации.");
        }
        if (event.getEventDate().isAfter(LocalDateTime.now().plusHours(1))) {
            event.setPublishedOn(LocalDateTime.now());
        } else {
            throw new EventBadRequest("Дата и время на которые намечено событие не может быть раньше, " +
                    "чем через один час от времени публикации.");
        }
        if (request.getAnnotation() != null) {
            event.setAnnotation(request.getAnnotation());
        }
        if (request.getCategory() != null) {
            Category category = categoryRepository.findById(request.getCategory())
                    .orElseThrow(() -> new NonExistentCategoryException(request.getCategory()));
            event.setCategory(category);
        }
        if (request.getDescription() != null) {
            event.setDescription(request.getDescription());
        }
        if (request.getEventDate() != null) {
            if (request.getEventDate().isAfter(LocalDateTime.now().plusHours(2))) {
                event.setEventDate(request.getEventDate());
            } else {
                throw new EventBadRequest("Дата и время на которые намечено событие не может быть раньше, " +
                        "чем через 2 часа от текущего времени.");
            }
        }
        if (request.getLocation() != null) {
            if (!request.getLocation().equals(LocationMapper.toLocation(event.getPosition()))) {
                event.setPosition(positionRepository.save(LocationMapper.toPosition(request.getLocation())));
            }
        }
        if (request.getPaid() != null) {
            event.setPaid(request.getPaid());
        }
        if (request.getParticipantLimit() != null) {
            event.setParticipantLimit(request.getParticipantLimit());
        }
        if (request.getRequestModeration() != null) {
            event.setRequestModeration(request.getRequestModeration());
        }
        if (request.getStateAction() != null) {
            if (request.getStateAction().equals(StateActionAdmin.PUBLISH_EVENT)) {
                event.setState(State.PUBLISHED);
            }
            if (request.getStateAction().equals(StateActionAdmin.REJECT_EVENT)) {
                event.setState(State.CANCELED);
            }
        }
        if (request.getTitle() != null) {
            event.setTitle(request.getTitle());
        }
        event = eventRepository.save(event);
        EventFullDto eventFullDto = EventMapper.toEventFullDto(event);
        eventFullDto.setPublishedOn(event.getPublishedOn().format(formatter));
        return getEventWithConfirmedRequests(eventFullDto);
    }

    @Override
    public EventFullDto getEventById(Long eventId, HttpServletRequest request) {
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new NonExistentEventException(eventId));
        if (!event.getState().equals(State.PUBLISHED)) {
            throw new NonExistentEventException("Событие не опубликовано.");
        }
        HitDto hitDto = new HitDto(
                "ewm-main-service",
                request.getRequestURI(),
                request.getRemoteAddr(),
                LocalDateTime.now().format(formatter)
        );
        statsClient.createHit(hitDto);
        return getEventWithConfirmedRequests(EventMapper.toEventFullDto(event));
    }

    @Override
    public List<EventFullDto> getEvents(Long[] users, String[] states, Long[] categories, String rangeStart,
                                        String rangeEnd, PageRequest pageRequest) {
        List<BooleanExpression> conditions = new ArrayList<>();
        boolean bool = false;
        List<State> status = new ArrayList<>();
        for (String state : states) {
            bool = true;
            status.add(State.valueOf(state));
        }
        if (users.length > 0) {
            bool = true;
            conditions.add(QEvent.event.initiator.id.in(users));
        }
        if (status.size() > 0) {
            bool = true;
            conditions.add(QEvent.event.state.in(status));
        }
        if (categories.length > 0) {
            bool = true;
            conditions.add(QEvent.event.category.id.in(categories));
        }
        if (rangeStart != null) {
            bool = true;
            conditions.add(QEvent.event.eventDate.after(LocalDateTime.parse(rangeStart, formatter)));
        }
        if (rangeEnd != null) {
            bool = true;
            conditions.add(QEvent.event.eventDate.before(LocalDateTime.parse(rangeEnd, formatter)));
        }
        if (bool) {
            BooleanExpression filter = conditions
                    .stream()
                    .reduce(BooleanExpression::and)
                    .orElseThrow();
            return getEventsWithConfirmedRequestsFull(eventRepository.findAll(filter, pageRequest)
                    .stream()
                    .map(EventMapper::toEventFullDto)
                    .collect(Collectors.toList()));
        } else {
            return getEventsWithConfirmedRequestsFull(eventRepository.findAll(pageRequest)
                    .stream()
                    .map(EventMapper::toEventFullDto)
                    .collect(Collectors.toList()));
        }
    }

    private List<EventFullDto> getEventsWithConfirmedRequestsFull(List<EventFullDto> list) {
        List<Long> eventIds = list.stream()
                .map(EventFullDto::getId)
                .collect(Collectors.toList());
        if (eventIds.isEmpty()) {
            return list;
        }
        List<Integer> confirmedRequests = requestRepository.findCountByEventIdIn(eventIds);
        if (confirmedRequests.size() > 0) {
            IntStream.range(0, Math.min(list.size(), confirmedRequests.size()))
                    .forEachOrdered(i -> list.get(i).setConfirmedRequests(confirmedRequests.get(i)));
        }
        return getEventsWithViewsFull(list, eventIds);
    }

    private List<EventFullDto> getEventsWithViewsFull(List<EventFullDto> list, List<Long> eventIds) {
        Map<String, Object> parameters = Map.of("start", LocalDateTime.now().minusDays(5).format(formatter),
                "end", LocalDateTime.now().format(formatter), "uris", eventIds.stream()
                        .map(number -> "/events/" + number).toArray(), "unique", true);
        List<HitShort> views = (List<HitShort>) statsClient.getState(parameters).getBody();
        if (views != null) {
            if (views.size() != 0) {
                for (int i = 0; i < views.size(); i++) {
                    LinkedHashMap<String, String> map = (LinkedHashMap<String, String>) views.get(i);
                    if (map.get("uri").length() < 9) {
                        continue;
                    }
                    Long eventId = Long.valueOf(map.get("uri").substring(8));
                    LinkedHashMap<String, Integer> counts = (LinkedHashMap<String, Integer>) views.get(i);
                    Long count = Long.valueOf(counts.get("hits"));
                    int index = -1;
                    for (int j = 0; j < list.size(); j++) {
                        if (list.get(j).getId().equals(eventId)) {
                            index = j;
                            break;
                        }
                    }
                    if (index != -1) {
                        EventFullDto event = list.get(index);
                        event.setViews(count);
                        list.set(index, event);
                    }
                }
            }
        }
        return list;
    }

    @Override
    public List<EventShortDto> getEventsByFilter(String text, Long[] categories, Boolean paid, String rangeStart,
                                                 String rangeEnd, Boolean onlyAvailable, String sort,
                                                 PageRequest pageRequest, HttpServletRequest request) {
        List<BooleanExpression> conditions = new ArrayList<>();
        State state = State.PUBLISHED;
        conditions.add(QEvent.event.state.eq(state));
        BooleanExpression annotationCondition = QEvent.event.annotation.lower().like(text.toLowerCase());
        BooleanExpression descriptionCondition = QEvent.event.description.lower().like(text.toLowerCase());
        BooleanExpression filteredConditions = annotationCondition.or(descriptionCondition);
        if (categories.length > 0) {
            conditions.add(QEvent.event.category.id.in(categories));
        }
        if (paid != null) {
            conditions.add(QEvent.event.paid.eq(paid));
        }
        if (rangeStart != null) {
            conditions.add(QEvent.event.eventDate.after(LocalDateTime.parse(rangeStart, formatter)));
        }
        if (rangeEnd != null) {
            conditions.add(QEvent.event.eventDate.before(LocalDateTime.parse(rangeEnd, formatter)));
        }
        if (rangeEnd == null && rangeStart == null) {
            conditions.add(QEvent.event.eventDate.after(LocalDateTime.now()));
        }
        if (rangeEnd != null && rangeStart != null) {
            if (LocalDateTime.parse(rangeStart, formatter).isAfter(LocalDateTime.parse(rangeEnd, formatter))) {
                throw new EventBadRequest("Неккоректные дата и время для поиска.");
            }
        }
        BooleanExpression filter = conditions
                .stream()
                .reduce(BooleanExpression::and)
                .orElseThrow();
        filter = filteredConditions.or(filter);
        List<EventFullDto> list = eventRepository.findAll(filter, pageRequest)
                .stream()
                .map(EventMapper::toEventFullDto)
                .collect(Collectors.toList());
        HitDto hitDto = new HitDto(
                "ewm-main-service",
                request.getRequestURI(),
                request.getRemoteAddr(),
                LocalDateTime.now().format(formatter)
        );
        statsClient.createHit(hitDto);
        return getEventsWithFilterAndCounts(list, onlyAvailable, sort);
    }

    private List<EventShortDto> getEventsWithFilterAndCounts(List<EventFullDto> list, Boolean onlyAvailable,
                                                             String sort) {
        list = getEventsWithConfirmedRequestsFull(list);
        List<EventShortDto> availableList;
        if (onlyAvailable) {
            availableList = list.stream()
                    .filter(x -> x.getParticipantLimit() == 0 || x.getConfirmedRequests() < x.getParticipantLimit())
                    .map(EventMapper::fromFullToShortDto)
                    .collect(Collectors.toList());
        } else {
            availableList = list.stream()
                    .map(EventMapper::fromFullToShortDto)
                    .collect(Collectors.toList());
        }
        if (sort != null) {
            if (sort.equals("EVENT_DATE")) {
                availableList = availableList.stream()
                        .sorted(Comparator.comparing(EventShortDto::getEventDate))
                        .collect(Collectors.toList());
            }
            if (sort.equals("VIEWS")) {
                availableList = availableList.stream()
                        .sorted(Comparator.comparing(EventShortDto::getViews))
                        .collect(Collectors.toList());
            }
        }
        return availableList;
    }

    @Override
    public List<EventShortDto> getActualEventsBySubscriberId(Long userId, PageRequest pageRequest) {
        List<Long> initiatorIds = subscriptionRepository.findAuthorOfContentIdBySubscriberId(userId);
        return getEventsWithConfirmedRequests(eventRepository.findByInitiatorIdInAndEventDateAfterAndState(initiatorIds,
                        LocalDateTime.now(), State.PUBLISHED, pageRequest)
                .stream()
                .map(EventMapper::toEventShortDto)
                .collect(Collectors.toList()));
    }
}
