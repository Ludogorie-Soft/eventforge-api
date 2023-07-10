package com.eventforge.service.annotation;

import com.eventforge.annotation.RegistrationOrganisationPriorityNotNullValidator;
import com.eventforge.dto.request.RegistrationRequest;
import jakarta.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

public class RegistrationOrganisationPriorityNotNullValidatorTest {
    private final RegistrationOrganisationPriorityNotNullValidator validator = new RegistrationOrganisationPriorityNotNullValidator();

    @Test
    void isValid_WhenOrganisationPrioritiesNotEmpty_ShouldReturnTrue() {
        // Arrange
        RegistrationRequest registrationRequest = new RegistrationRequest();
        registrationRequest.setOrganisationPriorities(Set.of("priority" ,"priority2"));

        // Act
        boolean isValid = validator.isValid(registrationRequest, mock(ConstraintValidatorContext.class));

        // Assert
        assertTrue(isValid);
    }

    @Test
    void isValid_WhenOrganisationPrioritiesEmptyAndOptionalCategoryNotEmpty_ShouldReturnTrue() {
        // Arrange
        RegistrationRequest registrationRequest = new RegistrationRequest();
        registrationRequest.setOrganisationPriorities(Set.of());
        registrationRequest.setOptionalCategory("optional");

        // Act
        boolean isValid = validator.isValid(registrationRequest, mock(ConstraintValidatorContext.class));

        // Assert
        assertTrue(isValid);
    }

    @Test
    void isValid_WhenOrganisationPrioritiesEmptyAndOptionalCategoryEmpty_ShouldReturnFalse() {
        // Arrange
        RegistrationRequest registrationRequest = new RegistrationRequest();
        registrationRequest.setOrganisationPriorities(Set.of());
        registrationRequest.setOptionalCategory("");

        // Act
        boolean isValid = validator.isValid(registrationRequest, mock(ConstraintValidatorContext.class));

        // Assert
        assertFalse(isValid);
    }

}
