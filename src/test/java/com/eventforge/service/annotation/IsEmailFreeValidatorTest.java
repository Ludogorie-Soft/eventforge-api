package com.eventforge.service.annotation;

import com.eventforge.annotation.IsEmailFreeValidator;
import com.eventforge.model.User;
import com.eventforge.service.UserService;
import jakarta.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
@ExtendWith(MockitoExtension.class)
class IsEmailFreeValidatorTest {

    private IsEmailFreeValidator validator;

    private UserService userService;

    private ConstraintValidatorContext constraintValidatorContext;

    @BeforeEach
    void setUp() {
        userService = mock(UserService.class);
        validator = new IsEmailFreeValidator(userService);
        constraintValidatorContext = mock(ConstraintValidatorContext.class);
    }

    @Test
    void isValid_WhenEmailIsFree_ShouldReturnTrue() {
        // Arrange

        String email = "free@example.com";
        when(userService.getUserByEmail(email)).thenReturn(null);

        // Act
        boolean isValid = validator.isValid(email, constraintValidatorContext);

        // Assert
        assertTrue(isValid);
    }

    @Test
    void isValid_WhenEmailIsTaken_ShouldReturnFalse() {
        // Arrange
        String email = "taken@example.com";
        when(userService.getUserByEmail(email)).thenReturn(new User());

        // Act
        boolean isValid = validator.isValid(email, constraintValidatorContext);

        // Assert
        assertFalse(isValid);
    }
}

