package com.eventforge.service.annotation;

import com.eventforge.annotation.RegistrationOrganisationPriorityNotNullValidator;
import com.eventforge.dto.request.RegistrationRequest;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Path;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.Collections;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class RegistrationOrganisationPriorityNotNullValidatorTest {
    private RegistrationOrganisationPriorityNotNullValidator validator;
    @Mock
    private ConstraintValidatorContext constraintValidatorContext;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        validator = new RegistrationOrganisationPriorityNotNullValidator();

    }

    @Test
    void isValid_WhenOrganisationPrioritiesNotEmpty_ShouldReturnTrue() {
        // Arrange
        RegistrationRequest registrationRequest = new RegistrationRequest();
        registrationRequest.setOrganisationPriorities(Set.of("priority", "priority2"));

        // Act
        boolean isValid = validator.isValid(registrationRequest, constraintValidatorContext);

        // Assert
        assertTrue(isValid);
    }

    @Test
    void isValid_WhenOrganisationPrioritiesEmptyAndOptionalCategoryNotEmpty_ShouldReturnTrue() {
        // Arrange
        RegistrationRequest registrationRequest = new RegistrationRequest();
        registrationRequest.setOrganisationPriorities(Collections.emptySet());
        registrationRequest.setOptionalCategory("optional");

        // Act
        boolean isValid = validator.isValid(registrationRequest, constraintValidatorContext);

        // Assert
        assertTrue(isValid);
    }

}
