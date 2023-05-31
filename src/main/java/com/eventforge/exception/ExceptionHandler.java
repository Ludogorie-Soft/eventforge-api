package com.eventforge.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.ZoneId;
import java.time.ZonedDateTime;

@RestControllerAdvice
public class ExceptionHandler {

    @org.springframework.web.bind.annotation.ExceptionHandler(GlobalException.class)
    public ResponseEntity<String> handleRuntimeException(Exception ex) {
        // Handle the exception and return an appropriate response
        String errorMessage = ex.getMessage();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
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
