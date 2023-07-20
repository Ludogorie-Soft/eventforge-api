package com.eventforge.annotation;

import com.eventforge.dto.request.ChangePasswordRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
public class UpdatePasswordsMustMatchValidator implements ConstraintValidator<UpdatePasswordsMustMatch , ChangePasswordRequest> {
    @Override
    public boolean isValid(ChangePasswordRequest request, ConstraintValidatorContext constraintValidatorContext) {
        constraintValidatorContext.disableDefaultConstraintViolation();
        if (!request.getNewPassword().equals(request.getConfirmNewPassword())) {
            constraintValidatorContext.buildConstraintViolationWithTemplate("Новите пароли не съвпадат. Новата парола трябва да съответства на потвърдената парола.")
                    .addPropertyNode("confirmNewPassword")
                    .addConstraintViolation();
            return false;
        }
        return true;
    }

}
