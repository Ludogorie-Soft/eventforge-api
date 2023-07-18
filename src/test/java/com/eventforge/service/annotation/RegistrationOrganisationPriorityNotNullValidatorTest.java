package com.eventforge.service.annotation;

import com.eventforge.annotation.RegistrationOrganisationPriorityNotNullValidator;
import com.eventforge.dto.request.RegistrationRequest;
import jakarta.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertTrue;

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
