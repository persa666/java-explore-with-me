package ru.practicum.exp;

public class NonExistentCategoryException extends RuntimeException {
    public NonExistentCategoryException(String message) {
        super(message);
    }

    public NonExistentCategoryException(Long catId) {
        super(String.format("Категория с id = %d не найдена.", catId));
    }
}
