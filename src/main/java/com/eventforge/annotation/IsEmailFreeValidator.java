package com.eventforge.annotation;

import com.eventforge.dto.request.RegistrationRequest;
import com.eventforge.service.UserService;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class IsEmailFreeValidator implements ConstraintValidator<IsEmailFree, String> {

    private final UserService userService;
    @Override
    public boolean isValid(String email, ConstraintValidatorContext constraintValidatorContext) {
        return userService.getUserByEmail(email) == null;
    }
}
