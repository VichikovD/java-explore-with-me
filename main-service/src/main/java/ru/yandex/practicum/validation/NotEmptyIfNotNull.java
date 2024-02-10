package ru.yandex.practicum.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = NotEmptyIfNotNullValidator.class)
public @interface NotEmptyIfNotNull {
    String message() default "{Should be not empty if not null}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
