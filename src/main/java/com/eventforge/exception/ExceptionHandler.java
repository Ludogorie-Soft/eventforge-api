package com.eventforge.exception;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.IOException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

@RestControllerAdvice
public class ExceptionHandler {

    @org.springframework.web.bind.annotation.ExceptionHandler(GlobalException.class)
    public void handleRuntimeException(GlobalException ex, HttpServletResponse response) throws IOException {
        response.setStatus(ex.getHttpStatus());
        response.setCharacterEncoding("UTF-8");
        response.setContentType(MediaType.TEXT_PLAIN_VALUE);
        response.getWriter().write(ex.getMessage());
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(MethodArgumentNotValidException.class)
    public void handleValidationException(MethodArgumentNotValidException ex, HttpServletResponse response) throws IOException {
        BindingResult bindingResult = ex.getBindingResult();
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();
        Queue<String> errorMessages = new LinkedList<>();
        for (FieldError fieldError : fieldErrors) {
            String fieldName = fieldError.getField();
            String errorMessage = fieldError.getDefaultMessage();
            errorMessages.add(fieldName + ": " + errorMessage);
        }
        response.setStatus(HttpServletResponse.SC_EXPECTATION_FAILED);
        response.setCharacterEncoding("UTF-8");
        response.setContentType(MediaType.TEXT_PLAIN_VALUE);
        response.getWriter().write(errorMessages.toString());
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(value = EventRequestException.class)
    private ResponseEntity<Object> handleEventRequestException(EventRequestException exception) {
        HttpStatus badRequest = HttpStatus.BAD_REQUEST;
        EventErrorMessage eventErrorMessage = new EventErrorMessage(
                exception.getMessage(),
                exception,
                badRequest,
                ZonedDateTime.now(ZoneId.of("Z"))
        );
        return new ResponseEntity<>(eventErrorMessage, badRequest);
    }
}
