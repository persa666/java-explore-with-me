package ru.practicum.exp;

public class EventException extends RuntimeException {
    public EventException(String message) {
        super(message);
    }

    public EventException(Long eventId) {
        super(String.format("Исключение с событием id = %d.", eventId));
    }
}
