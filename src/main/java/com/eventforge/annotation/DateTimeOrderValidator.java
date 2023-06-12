package com.eventforge.annotation;

import com.eventforge.dto.request.EventRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class DateTimeOrderValidator implements ConstraintValidator<DateTimeOrder , EventRequest> {
    @Override
    public boolean isValid(EventRequest eventRequest, ConstraintValidatorContext constraintValidatorContext) {
        return eventRequest.getStartsAt().isBefore(eventRequest.getEndsAt()) ||
                eventRequest.getStartsAt().isEqual(eventRequest.getEndsAt());
    }
}
