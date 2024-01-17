package ru.practicum.exp;

public class EventBadRequest extends RuntimeException {
    public EventBadRequest(String message) {
        super(message);
    }
}
