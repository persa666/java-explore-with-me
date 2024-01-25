package ru.practicum.exp;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolationException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RestControllerAdvice
public class ErrorHandler {

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError handleCategoryNotFound(final NonExistentCategoryException e) {
        return new ApiError(
                HttpStatus.NOT_FOUND.name(),
                "Требуемый объект не был найден.",
                e.getMessage(),
                LocalDateTime.now().format(formatter)
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError handleSubscriptionNotFound(final NonExistentSubscriptionException e) {
        return new ApiError(
                HttpStatus.NOT_FOUND.name(),
                "Требуемый объект не был найден.",
                e.getMessage(),
                LocalDateTime.now().format(formatter)
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleParamBadRequest(final MissingServletRequestParameterException e) {
        return new ApiError(
                HttpStatus.BAD_REQUEST.name(),
                "Неправильно сделанный запрос.",
                e.getMessage(),
                LocalDateTime.now().format(formatter)
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError handleUserNotFound(final NonExistentUserException e) {
        return new ApiError(
                HttpStatus.NOT_FOUND.name(),
                "Требуемый объект не был найден.",
                e.getMessage(),
                LocalDateTime.now().format(formatter)
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError handleEventNotFound(final NonExistentEventException e) {
        return new ApiError(
                HttpStatus.NOT_FOUND.name(),
                "Требуемый объект не был найден.",
                e.getMessage(),
                LocalDateTime.now().format(formatter)
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError handleParticipationRequestNotFound(final NonExistentParticipationRequestException e) {
        return new ApiError(
                HttpStatus.NOT_FOUND.name(),
                "Требуемый объект не был найден.",
                e.getMessage(),
                LocalDateTime.now().format(formatter)
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleCategoryBadRequest(final MethodArgumentNotValidException e) {
        return new ApiError(
                HttpStatus.BAD_REQUEST.name(),
                "Неправильно сделанный запрос.",
                e.getMessage(),
                LocalDateTime.now().format(formatter)
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleCategoryConflict(final DataIntegrityViolationException e) {
        return new ApiError(
                HttpStatus.CONFLICT.name(),
                "Ограничение целостности было нарушено.",
                e.getMessage(),
                LocalDateTime.now().format(formatter)
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleConstraintViolation(final ConstraintViolationException e) {
        return new ApiError(
                HttpStatus.BAD_REQUEST.name(),
                "Неправильно сделанный запрос.",
                e.getMessage(),
                LocalDateTime.now().format(formatter)
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleNumberFormat(final NumberFormatException e) {
        return new ApiError(
                HttpStatus.BAD_REQUEST.name(),
                "Неправильно сделанный запрос.",
                e.getMessage(),
                LocalDateTime.now().format(formatter)
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleEventBadRequest(final EventBadRequest e) {
        return new ApiError(
                HttpStatus.BAD_REQUEST.name(),
                "Неправильно сделанный запрос.",
                e.getMessage(),
                LocalDateTime.now().format(formatter)
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError handleModelNotFound(final EmptyResultDataAccessException e) {
        return new ApiError(
                HttpStatus.NOT_FOUND.name(),
                "Требуемый объект не был найден.",
                e.getMessage(),
                LocalDateTime.now().format(formatter)
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError handleModelNotFound(final NonExistentCompilationException e) {
        return new ApiError(
                HttpStatus.NOT_FOUND.name(),
                "Требуемый объект не был найден.",
                e.getMessage(),
                LocalDateTime.now().format(formatter)
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleParticipationRequestConflict(final ParticipationRequestException e) {
        return new ApiError(
                HttpStatus.CONFLICT.name(),
                "Ограничение целостности было нарушено.",
                e.getMessage(),
                LocalDateTime.now().format(formatter)
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleEventConflict(final EventException e) {
        return new ApiError(
                HttpStatus.CONFLICT.name(),
                "Ограничение целостности было нарушено.",
                e.getMessage(),
                LocalDateTime.now().format(formatter)
        );
    }
}
