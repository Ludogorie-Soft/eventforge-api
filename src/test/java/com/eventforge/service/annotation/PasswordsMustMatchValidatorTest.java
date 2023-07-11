package com.eventforge.service.annotation;

import com.eventforge.annotation.PasswordsMustMatchValidator;
import com.eventforge.dto.request.RegistrationRequest;
import jakarta.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

public class PasswordsMustMatchValidatorTest {
    private final PasswordsMustMatchValidator validator = new PasswordsMustMatchValidator();

    @Test
    void isValid_WhenPasswordsMatch_ShouldReturnTrue() {
        // Arrange
        RegistrationRequest registrationRequest = new RegistrationRequest();
        registrationRequest.setPassword("password123");
        registrationRequest.setConfirmPassword("password123");

        // Act
        boolean isValid = validator.isValid(registrationRequest, mock(ConstraintValidatorContext.class));

        // Assert
        assertTrue(isValid);
    }

    @Test
    void isValid_WhenPasswordsDoNotMatch_ShouldReturnFalse() {
        // Arrange
        RegistrationRequest registrationRequest = new RegistrationRequest();
        registrationRequest.setPassword("password123");
        registrationRequest.setConfirmPassword("mismatchedPassword");

        // Act
        boolean isValid = validator.isValid(registrationRequest, mock(ConstraintValidatorContext.class));

        // Assert
        assertFalse(isValid);
    }
}
