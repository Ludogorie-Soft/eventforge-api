package com.eventforge.annotation;

import com.eventforge.dto.request.UpdateAccountRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class UpdateOrganisationPriorityNotNullValidator implements ConstraintValidator<UpdateOrganisationPriorityNotNull , UpdateAccountRequest> {
    @Override
    public boolean isValid(UpdateAccountRequest updateAccountRequest, ConstraintValidatorContext constraintValidatorContext) {
        if (!((updateAccountRequest.getChosenPriorities() != null && updateAccountRequest.getChosenPriorities().size() > 0) ||
                (updateAccountRequest.getOptionalCategory() != null && updateAccountRequest.getOptionalCategory().length() > 0))) {
            constraintValidatorContext.buildConstraintViolationWithTemplate("Моля изберете поне една категория която отговаря на приоритетите на организацията. Ако изброените отгоре не отговарят , моля посочете в полето отдолу")
                    .addPropertyNode("chosenPriorities")
                    .addConstraintViolation();
            return false;
        }
        return true;
    }
}
