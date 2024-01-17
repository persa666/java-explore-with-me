package ru.practicum.exp;

public class NonExistentEventException extends RuntimeException {
    public NonExistentEventException(String message) {
        super(message);
    }

    public NonExistentEventException(Long eventId) {
        super(String.format("Событие с id = %d не найдено.", eventId));
    }
}
