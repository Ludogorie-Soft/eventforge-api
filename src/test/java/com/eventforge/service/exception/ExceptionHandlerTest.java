package com.eventforge.service.exception;

import com.eventforge.exception.*;
import com.eventforge.exception.handler.ExceptionHandler;
import com.eventforge.service.Utils;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

class ExceptionHandlerTest {

    @Mock
    private Utils mockUtils;

    private ExceptionHandler exceptionHandler;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        exceptionHandler = new ExceptionHandler(mockUtils);
    }

    @Test
    void testHandleUserNotFoundException() {
        // Create an instance of YourController

        // Create a UsernameNotFoundException instance
        UsernameNotFoundException exception = new UsernameNotFoundException("User not found");

        // Call the method under test
        ResponseEntity<String> response = exceptionHandler.handleUserNotFoundException(exception);

        // Assert the response
        assertEquals(321, response.getStatusCode().value(),
                "Expected HTTP status code 321");
        assertEquals(MediaType.TEXT_PLAIN, response.getHeaders().getContentType(), "Expected response content type TEXT_PLAIN");
        assertEquals("User not found", response.getBody(), "Expected response body to be 'User not found'");
    }

    // Add tests for other exception handlers
    @Test
    void testHandleValidationException() {
        // Arrange
        Object targetObject = new Object(); // Replace with your target object
        BindingResult bindingResult = new BeanPropertyBindingResult(targetObject, "targetObject");
        bindingResult.addError(new ObjectError("targetObject", "Error message")); // Add any desired errors

        MethodArgumentNotValidException exception = new MethodArgumentNotValidException((MethodParameter) null, bindingResult);

        // Mock the behavior of the Utils class
        when(mockUtils.generateErrorStringFromMethodArgumentNotValidException(eq(Collections.emptyList()), eq(bindingResult.getFieldErrors()))).thenReturn("Validation error");

        // Act
        ResponseEntity<String> response = exceptionHandler.handleValidationException(exception);

        // Assert
        assertEquals(HttpStatus.PRECONDITION_FAILED, response.getStatusCode());
        assertEquals(MediaType.TEXT_PLAIN, response.getHeaders().getContentType());
        assertNull(response.getBody());
    }

    @Test
    void testUserDisabledException() {
        // Arrange
       UserDisabledException exception = new UserDisabledException();

        // Act
        ResponseEntity<String> response = exceptionHandler.handleUserDisabledException(exception);

        // Assert
        assertEquals(HttpStatus.SERVICE_UNAVAILABLE, response.getStatusCode());
        assertEquals(MediaType.TEXT_PLAIN, response.getHeaders().getContentType());
        assertEquals("Моля потвърдете първо електронната си поща.", response.getBody());
    }

    @Test
    void testHandleEmailConfirmationNotSentException() {
        // Arrange
        EmailConfirmationNotSentException exception = new EmailConfirmationNotSentException("test@test.com");
        int expectedStatus = HttpStatus.EXPECTATION_FAILED.value();

        // Act
        ResponseEntity<String> response = exceptionHandler.handleEmailConfirmationNotSentException(exception);


        // Assert
        assertEquals(expectedStatus, response.getStatusCode().value());
        assertEquals(MediaType.TEXT_PLAIN, response.getHeaders().getContentType());
        assertEquals("Възникна проблем при изпращането на потвърждение към адрес с електронна поща: test@test.com", response.getBody());
    }

    @Test
    void testHandleInvalidCredentialsException() {
        // Arrange
        InvalidCredentialsException exception = new InvalidCredentialsException();
        int expectedStatus = HttpServletResponse.SC_NOT_FOUND;

        // Act
        ResponseEntity<String> response = exceptionHandler.handleInvalidCredentialsException(exception);

        // Assert
        assertEquals(expectedStatus, response.getStatusCode().value());
        assertEquals(MediaType.TEXT_PLAIN, response.getHeaders().getContentType());
        assertEquals("Невалидна потребителска поща или парола", response.getBody());
    }

    @Test
    void testHandleInvalidEmailConfirmationLinkException() {
        // Arrange
        InvalidEmailConfirmationLinkException exception = new InvalidEmailConfirmationLinkException("Invalid email confirmation link");
        int expectedStatus = HttpStatus.BAD_REQUEST.value();

        // Act
        ResponseEntity<String> response = exceptionHandler.handleInvalidEmailConfirmationLinkException(exception);

        // Assert
        assertEquals(expectedStatus, response.getStatusCode().value());
        assertEquals(MediaType.TEXT_PLAIN, response.getHeaders().getContentType());
        assertEquals("Invalid email confirmation link", response.getBody());
    }

    @Test
    void testHandleImageException() {
        // Arrange
        ImageException exception = new ImageException("Unsupported media type");
        int expectedStatus = HttpStatus.UNSUPPORTED_MEDIA_TYPE.value();

        // Act
        ResponseEntity<String> response = exceptionHandler.handleImageException(exception);

        // Assert
        assertEquals(expectedStatus, response.getStatusCode().value());
        assertEquals(MediaType.TEXT_PLAIN, response.getHeaders().getContentType());
        assertEquals("Unsupported media type", response.getBody());
    }


    @Test
    void testHandleEventRequestException() {
        // Arrange
        EventRequestException exception = new EventRequestException("Invalid event request");
        int expectedStatus = exception.getHTTP_STATUS_CODE();

        // Act
        ResponseEntity<String> response = exceptionHandler.handleEventRequestException(exception);

        // Assert
        assertEquals(expectedStatus, response.getStatusCode().value());
        assertEquals(MediaType.TEXT_PLAIN, response.getHeaders().getContentType());
        assertEquals("Invalid event request", response.getBody());
    }

    @Test
    void testHandleUserLockedException() {
        // Arrange
        UserLockedException exception = new UserLockedException();
        int expectedStatus = exception.getHTTP_STATUS_CODE();

        // Act
        ResponseEntity<String> response = exceptionHandler.handleUserLockedException(exception);

        // Assert
        assertEquals(expectedStatus, response.getStatusCode().value());
        assertEquals(MediaType.TEXT_PLAIN, response.getHeaders().getContentType());
        assertEquals("Забранен е всякакъв достъп до този акаунт. За повече информация , свържете се с администратора на сайта.", response.getBody());
    }

    @Test
    void testHandleOrganisationRequestException() {
        // Create an instance of YourController


        // Create an OrganisationRequestException instance with custom status code and message
        OrganisationRequestException exception = new OrganisationRequestException("Request expired");

        // Call the method under test
        ResponseEntity<String> response = exceptionHandler.handleOrganisationRequestException(exception);

        // Assert the response
        assertEquals(HttpStatus.GONE, response.getStatusCode(),
                "Expected HTTP status code GONE (410)");
        assertEquals(MediaType.TEXT_PLAIN, response.getHeaders().getContentType(),
                "Expected response content type TEXT_PLAIN");
        assertEquals("Request expired", response.getBody(),
                "Expected response body to be 'Request expired'");
    }


}
