package com.eventforge.annotation;

import com.eventforge.dto.request.EventRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class AgeBoundaryValidator implements ConstraintValidator<AgeBoundary, EventRequest> {
    @Override
    public boolean isValid(EventRequest eventRequest, ConstraintValidatorContext constraintValidatorContext) {
        int minAge = eventRequest.getMinAge();
        int maxAge = eventRequest.getMaxAge();
        if (minAge > 0 && maxAge == 0) {
            return true;
        }
        if (maxAge > 0 && maxAge < minAge) {
            return false;
        }
        return true;
    }
}
