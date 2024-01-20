package ru.practicum.constraints;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = FutureTimeValidatorTwoHours.class)
public @interface FutureTimeTwoHours {

    String message() default "Дата и время на которые намечено событие не может быть раньше, " +
            "чем через два часа от текущего момента";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
