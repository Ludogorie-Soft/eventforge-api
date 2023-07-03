package com.eventforge.annotation;

import com.eventforge.dto.request.RegistrationRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class RegistrationOrganisationPriorityNotNullValidator implements ConstraintValidator<RegistrationOrganisationPriorityNotNull, RegistrationRequest> {
    @Override
    public boolean isValid(RegistrationRequest registrationRequest, ConstraintValidatorContext constraintValidatorContext) {
        return registrationRequest.getOrganisationPriorities().size() > 0 || registrationRequest.getOptionalCategory().length() > 0;
    }
}