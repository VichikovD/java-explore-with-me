package ru.yandex.practicum.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = InTwoHoursValidator.class)
public @interface InTwoHours {
    String message() default "{Date time should be 2 hours after now}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
