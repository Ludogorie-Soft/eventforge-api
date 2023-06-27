package com.eventforge.annotation;

import com.eventforge.dto.request.RegistrationRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordsMustMatchValidator implements ConstraintValidator<PasswordsMustMatch , RegistrationRequest> {
    @Override
    public boolean isValid(RegistrationRequest registrationRequest, ConstraintValidatorContext constraintValidatorContext) {
        return registrationRequest.getPassword().equals(registrationRequest.getConfirmPassword());
    }
}
