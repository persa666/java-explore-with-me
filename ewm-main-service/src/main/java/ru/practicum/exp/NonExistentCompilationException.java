package ru.practicum.exp;

public class NonExistentCompilationException extends RuntimeException {
    public NonExistentCompilationException(String message) {
        super(message);
    }

    public NonExistentCompilationException(Long compId) {
        super(String.format("Подборка с id = %d не найдено.", compId));
    }
}
