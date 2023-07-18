package com.eventforge.annotation;

import com.eventforge.dto.request.ChangePasswordRequest;
import com.eventforge.service.Utils;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
public class UpdatePasswordsMustMatchValidator implements ConstraintValidator<UpdatePasswordsMustMatch , ChangePasswordRequest> {
    private final Utils utils;
    @Override
    public boolean isValid(ChangePasswordRequest request, ConstraintValidatorContext constraintValidatorContext) {
        boolean isValid = true;
        constraintValidatorContext.disableDefaultConstraintViolation();
        if (!utils.isPasswordValid(request.getOldPassword(), request.getCurrentPassword())) {
            isValid = false;
            constraintValidatorContext.buildConstraintViolationWithTemplate("Паролата не съвпада с тази от базата ни данни.")
                    .addPropertyNode("oldPassword")
                    .addConstraintViolation();
        }
        if (!request.getNewPassword().equals(request.getConfirmNewPassword())) {
            isValid = false;
            constraintValidatorContext.buildConstraintViolationWithTemplate("Новите пароли не съвпадат. Новата парола трябва да съответства на потвърдената парола.")
                    .addPropertyNode("confirmNewPassword")
                    .addConstraintViolation();
        }
        return isValid;
    }

}
