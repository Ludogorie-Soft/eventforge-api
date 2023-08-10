package com.eventforge.annotation;

import com.eventforge.dto.request.EventRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDateTime;

public class EndDateValidator implements ConstraintValidator<EndDate , EventRequest> {
    @Override
    public boolean isValid(EventRequest eventRequest, ConstraintValidatorContext constraintValidatorContext) {
        LocalDateTime now = LocalDateTime.now();
       if(eventRequest.getStartsAt().isAfter(eventRequest.getEndsAt()) || eventRequest.getEndsAt().isBefore(now)){
           constraintValidatorContext.buildConstraintViolationWithTemplate("Невалидна дата. Не е възможно създаване на събитие с минала дата спрямо текущата или с минала дата спрямо започване на събитието!")
                   .addPropertyNode("endsAt")
                   .addConstraintViolation();
           return false;
       }
       return true;
    }
}
