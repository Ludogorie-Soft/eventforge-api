package com.eventforge.service.annotation;

import com.eventforge.annotation.UpdateOrganisationPriorityNotNullValidator;
import com.eventforge.dto.request.UpdateAccountRequest;
import jakarta.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

public class UpdateOrganisationPriorityNotNullValidatorTest {
    private final UpdateOrganisationPriorityNotNullValidator validator = new UpdateOrganisationPriorityNotNullValidator();

    @Test
    void isValid_WhenChosenPrioritiesNotEmpty_ShouldReturnTrue() {
        // Arrange
        UpdateAccountRequest updateAccountRequest = new UpdateAccountRequest();
        updateAccountRequest.setChosenPriorities(Set.of("priority1","priority2"));

        // Act
        boolean isValid = validator.isValid(updateAccountRequest, mock(ConstraintValidatorContext.class));

        // Assert
        assertTrue(isValid);
    }

    @Test
    void isValid_WhenChosenPrioritiesEmptyAndOptionalCategoryNotEmpty_ShouldReturnTrue() {
        // Arrange
        UpdateAccountRequest updateAccountRequest = new UpdateAccountRequest();
        updateAccountRequest.setChosenPriorities(Set.of("prirority"));
        updateAccountRequest.setOptionalCategory("something");

        // Act
        boolean isValid = validator.isValid(updateAccountRequest, mock(ConstraintValidatorContext.class));

        // Assert
        assertTrue(isValid);
    }

}
