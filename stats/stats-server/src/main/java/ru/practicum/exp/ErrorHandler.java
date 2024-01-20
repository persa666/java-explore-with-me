package ru.practicum.exp;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RestControllerAdvice
public class ErrorHandler {
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleTimeBadRequest(final TimeException e) {
        return new ApiError(
                HttpStatus.BAD_REQUEST.name(),
                "Неправильно сделанный запрос.",
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
}
