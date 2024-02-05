package ru.yandex.practicum.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class NotBlankIfNotNullValidator implements ConstraintValidator<NotBlankIfNotNull, String> {
    @Override
    public boolean isValid(String string, ConstraintValidatorContext constraintValidatorContext) {
        if (string == null) {
            return true;
        }

        return !string.isBlank();
    }
}
