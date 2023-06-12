package com.eventforge.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = AgeBoundaryValidator.class)
public @interface AgeBoundary {
    String message() default "Възрастовите диапазони не съответстват. Максималната възрастова граница трябва да е по-голяма от малката.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
