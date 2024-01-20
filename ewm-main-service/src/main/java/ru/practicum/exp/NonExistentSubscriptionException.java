package ru.practicum.exp;

public class NonExistentSubscriptionException extends RuntimeException {
    public NonExistentSubscriptionException(String message) {
        super(message);
    }

    public NonExistentSubscriptionException(Long subsId) {
        super(String.format("Подписка с id = %d не найдена.", subsId));
    }
}
