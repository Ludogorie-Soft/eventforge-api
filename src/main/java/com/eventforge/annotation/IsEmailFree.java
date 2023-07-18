package com.eventforge.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.PARAMETER;

@Target({FIELD , PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = IsEmailFreeValidator.class)
public @interface IsEmailFree {

    String message() default "Въведената електронна поща е вече заета.Моля опитайте с друга";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
