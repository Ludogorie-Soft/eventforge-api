package com.eventforge.service.annotation;

import com.eventforge.annotation.EndDateValidator;
import com.eventforge.dto.request.EventRequest;
import jakarta.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
public class EndDateValidatorTest {

    private final EndDateValidator validator =new EndDateValidator();

    @Test
    void isValid_WhenEndDateIsAfterStartDateAndCurrentDate_ShouldReturnTrue() {
        // Arrange

        EventRequest eventRequest = new EventRequest();
        eventRequest.setStartsAt(LocalDateTime.of(2023 ,8 ,12, 12 , 45));
        eventRequest.setEndsAt(LocalDateTime.of(2100 ,8,14 ,15 ,30));

        // Act
        boolean isValid = validator.isValid(eventRequest, mock(ConstraintValidatorContext.class));

        // Assert
        assertTrue(isValid);
    }

}
