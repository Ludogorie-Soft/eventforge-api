package com.eventforge.service.annotation;

import com.eventforge.annotation.AgeBoundaryValidator;
import com.eventforge.dto.request.EventRequest;
import jakarta.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class AgeBoundaryValidatorTest {
    private final AgeBoundaryValidator validator = new AgeBoundaryValidator();

    @Test
    void isValid_WhenMinAgeIsZeroAndMaxAgeIsZero_ShouldReturnTrue() {
        // Arrange
        EventRequest eventRequest = new EventRequest();
        eventRequest.setMinAge(0);
        eventRequest.setMaxAge(0);

        // Act
        boolean isValid = validator.isValid(eventRequest, mock(ConstraintValidatorContext.class));

        // Assert
        assertTrue(isValid);
    }

    @Test
    void isValid_WhenMinAgeIsPositiveAndMaxAgeIsZero_ShouldReturnTrue() {
        // Arrange
        EventRequest eventRequest = new EventRequest();
        eventRequest.setMinAge(18);
        eventRequest.setMaxAge(0);

        // Act
        boolean isValid = validator.isValid(eventRequest, mock(ConstraintValidatorContext.class));

        // Assert
        assertTrue(isValid);
    }

    @Test
    void isValid_WhenMaxAgeIsPositiveAndMaxAgeIsLessThanMinAge_ShouldReturnFalse() {
        // Arrange
        EventRequest eventRequest = new EventRequest();
        eventRequest.setMinAge(18);
        eventRequest.setMaxAge(16);

        // Act
        boolean isValid = validator.isValid(eventRequest, mock(ConstraintValidatorContext.class));

        // Assert
        assertFalse(isValid);
    }

    @Test
    void isValid_WhenMinAgeIsPositiveAndMaxAgeIsPositiveAndMaxAgeIsGreaterThanOrEqualToMinAge_ShouldReturnTrue() {
        // Arrange
        EventRequest eventRequest = new EventRequest();
        eventRequest.setMinAge(18);
        eventRequest.setMaxAge(30);

        // Act
        boolean isValid = validator.isValid(eventRequest, mock(ConstraintValidatorContext.class));

        // Assert
        assertTrue(isValid);
    }

}
