package com.eventforge.service.exception;
import com.eventforge.exception.*;
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
import org.springframework.validation.*;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.Collections;
import java.util.List;

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
    void testHandleDateTimeException() {
        // Arrange
        DateTimeException exception = new DateTimeException();

        // Act
        ResponseEntity<String> response = exceptionHandler.handleDateTimeException(exception);

        // Assert
        assertEquals(HttpStatus.SEE_OTHER, response.getStatusCode());
        assertEquals(MediaType.TEXT_PLAIN, response.getHeaders().getContentType());
        assertEquals("Не може датата на започване да е по-голяма от датата на приключване", response.getBody());
    }

    @Test
    void testHandleEmailAlreadyTakenException() {
        // Arrange
        EmailAlreadyTakenException exception = new EmailAlreadyTakenException();

        // Act
        ResponseEntity<String> response = exceptionHandler.handleEmailAlreadyTakenException(exception);

        // Assert
        assertEquals(HttpStatus.FOUND, response.getStatusCode());
        assertEquals(MediaType.TEXT_PLAIN, response.getHeaders().getContentType());
        assertEquals("Въведената електронна поща е вече заета.Моля опитайте с друга", response.getBody());
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
        assertEquals("Моля потвърдете имейла си за да влезете в профилът си", response.getBody());
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
    void testHandleInvalidPasswordException() {
        // Arrange
        InvalidPasswordException exception = new InvalidPasswordException("Invalid password");
        int expectedStatus = HttpStatus.CONFLICT.value();

        // Act
        ResponseEntity<String> response = exceptionHandler.handleInvalidPasswordException(exception);

        // Assert
        assertEquals(expectedStatus, response.getStatusCode().value());
        assertEquals(MediaType.TEXT_PLAIN, response.getHeaders().getContentType());
        assertEquals("Invalid password", response.getBody());
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
        assertEquals("Забранен е достъпът до този акаунт. За повече информация , свържете се с администратора на сайта.", response.getBody());
    }



}
