package ru.practicum.exp;

public class NonExistentParticipationRequestException extends RuntimeException {
    public NonExistentParticipationRequestException(String message) {
        super(message);
    }

    public NonExistentParticipationRequestException(Long requestId) {
        super(String.format("Запрос с id = %d не найден.", requestId));
    }
}
