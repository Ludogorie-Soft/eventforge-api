package com.eventforge.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = UpdateOrganisationPriorityNotNullValidator.class)
public @interface UpdateOrganisationPriorityNotNull {
    String message() default "Моля изберете поне една категория която отговаря на приоритетите на организацията. Ако изброените отгоре не отговарят , моля посочете в полето отдолу";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
