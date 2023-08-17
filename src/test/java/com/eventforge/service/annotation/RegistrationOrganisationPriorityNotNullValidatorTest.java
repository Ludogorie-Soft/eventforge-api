package com.eventforge.service.annotation;

import com.eventforge.annotation.RegistrationOrganisationPriorityNotNullValidator;
import com.eventforge.dto.request.RegistrationRequest;
import jakarta.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertTrue;
@ExtendWith(MockitoExtension.class)
class RegistrationOrganisationPriorityNotNullValidatorTest {
    @InjectMocks
    private RegistrationOrganisationPriorityNotNullValidator validator;
    @Mock
    private ConstraintValidatorContext constraintValidatorContext;


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
