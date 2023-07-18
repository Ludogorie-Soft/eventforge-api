package com.eventforge.annotation;

import com.eventforge.dto.request.RegistrationRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class RegistrationOrganisationPriorityNotNullValidator implements ConstraintValidator<RegistrationOrganisationPriorityNotNull, RegistrationRequest> {
    @Override
    public boolean isValid(RegistrationRequest registrationRequest, ConstraintValidatorContext constraintValidatorContext) {
        if(!(registrationRequest.getOrganisationPriorities().size() > 0 || (registrationRequest.getOptionalCategory()!=null && registrationRequest.getOptionalCategory().length() > 0))){
            constraintValidatorContext.buildConstraintViolationWithTemplate("Моля изберете поне една категория която отговаря на приоритетите на организацията. Ако изброените отгоре не отговарят , моля посочете в полето отдолу")
                    .addPropertyNode("organisationPriorities")
                    .addConstraintViolation();
            return false;
        }
       return true;
    }
}