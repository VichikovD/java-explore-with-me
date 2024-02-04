package ru.yandex.practicum.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = EmailCustomValidator.class)
public @interface EmailCustom {
    String message() default "{Email validation failed. Check on following: " +
            "localPart@domainPart  common length > 5, localPart length < 65, domainPart < 64}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
