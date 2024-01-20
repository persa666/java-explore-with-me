package ru.practicum.constraints;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDateTime;

public class FutureTimeValidatorTwoHours implements ConstraintValidator<FutureTimeTwoHours, LocalDateTime> {
    @Override
    public void initialize(FutureTimeTwoHours constraintAnnotation) {
    }

    @Override
    public boolean isValid(LocalDateTime value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime minFutureTime = now.plusHours(2);

        return value.isAfter(minFutureTime);
    }
}
