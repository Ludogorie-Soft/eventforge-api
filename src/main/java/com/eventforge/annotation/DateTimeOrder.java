package com.eventforge.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = DateTimeOrderValidator.class)
public @interface DateTimeOrder {
    String message() default "Невалидни дати. Началната дата трябва да бъде преди или равна на крайната дата.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
