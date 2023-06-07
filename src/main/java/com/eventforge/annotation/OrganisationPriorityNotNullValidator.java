package com.eventforge.annotation;

import com.eventforge.dto.RegistrationRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class OrganisationPriorityNotNullValidator implements ConstraintValidator<OrganisationPriorityNotNull , RegistrationRequest> {
    @Override
    public boolean isValid(RegistrationRequest registrationRequest, ConstraintValidatorContext constraintValidatorContext) {
        return registrationRequest.getOrganisationPriorities().size() > 0 || registrationRequest.getOptionalCategory().length() > 0;
    }
}