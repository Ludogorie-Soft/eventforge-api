package com.eventforge.service.annotation;

import com.eventforge.annotation.RegistrationPasswordsMustMatchValidator;
import com.eventforge.dto.request.RegistrationRequest;
import jakarta.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

class RegistrationRegistrationPasswordsMustMatchValidatorTest {
    private RegistrationPasswordsMustMatchValidator validator;


    @BeforeEach
    void setUp() {
        validator = new RegistrationPasswordsMustMatchValidator();
    }

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

}
