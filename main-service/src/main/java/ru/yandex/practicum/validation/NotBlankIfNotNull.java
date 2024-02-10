package ru.yandex.practicum.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = NotBlankIfNotNullValidator.class)
public @interface NotBlankIfNotNull {
    String message() default "{Field validation failed. It shouldn't contain only whitespaces or be empty" +
            "if it is not null}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
