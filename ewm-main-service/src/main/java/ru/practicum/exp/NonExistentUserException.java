package ru.practicum.exp;

public class NonExistentUserException extends RuntimeException {
    public NonExistentUserException(String message) {
        super(message);
    }

    public NonExistentUserException(Long userId) {
        super(String.format("Пользователь с id = %d не найден.", userId));
    }
}
