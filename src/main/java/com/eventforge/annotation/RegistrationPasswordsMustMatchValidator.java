package com.eventforge.annotation;

import com.eventforge.dto.request.RegistrationRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class RegistrationPasswordsMustMatchValidator implements ConstraintValidator<RegistrationPasswordsMustMatch, RegistrationRequest> {


    @Override
    public boolean isValid(RegistrationRequest registrationRequest, ConstraintValidatorContext constraintValidatorContext) {
        if(!registrationRequest.getPassword().equals(registrationRequest.getConfirmPassword())){
            constraintValidatorContext.buildConstraintViolationWithTemplate("Въведените от вас пароли не съвпадат!")
                    .addPropertyNode("confirmPassword")
                    .addConstraintViolation();
            return false;
        }
       return true;
    }
}
