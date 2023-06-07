package com.eventforge.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.StringJoiner;

@RestControllerAdvice
public class ExceptionHandler {

    @org.springframework.web.bind.annotation.ExceptionHandler(EmailAlreadyTakenException.class)
    public ResponseEntity<String> handleEmailAlreadyTakenException(EmailAlreadyTakenException ex)  {
        return ResponseEntity.status(ex.getHttpStatus())
                .contentType(MediaType.TEXT_PLAIN)
                .body(ex.getMessage());
    }
    @org.springframework.web.bind.annotation.ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> handleValidationException(MethodArgumentNotValidException ex) {
        List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();
        List<ObjectError> globalErrors = ex.getBindingResult().getGlobalErrors();

        StringJoiner joiner = new StringJoiner("/ ");
        if(!globalErrors.isEmpty()){
            for (ObjectError error : globalErrors) {
                joiner.add(error.getObjectName() + ": " + error.getDefaultMessage());
            }
        }

        if(!fieldErrors.isEmpty()){
            for (FieldError error : fieldErrors) {
                joiner.add(error.getField() + ": " + error.getDefaultMessage());
            }
        }


        String errorString = joiner.toString();

        return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED)
                .contentType(MediaType.TEXT_PLAIN)
                .body(errorString);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(EmailConfirmationNotSentException.class)
    public ResponseEntity<String> handleEmailConfirmationNotSentException(EmailConfirmationNotSentException ex ) {
        return ResponseEntity.status(ex.getHTTP_STATUS_CODE())
                .contentType(MediaType.TEXT_PLAIN).body(ex.getMessage());
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<String> handleInvalidCredentialsException(InvalidCredentialsException ex ) {
        return ResponseEntity.status(ex.getHTTP_STATUS_CODE())
                .contentType(MediaType.TEXT_PLAIN).body(ex.getMessage());
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(InvalidEmailConfirmationLinkException.class)
    public ResponseEntity<String> handleInvalidEmailConfirmationLinkException(InvalidEmailConfirmationLinkException ex) {
        return ResponseEntity.status(ex.getHTTP_STATUS_CODE())
                .contentType(MediaType.TEXT_PLAIN).body(ex.getMessage());
    }
    @org.springframework.web.bind.annotation.ExceptionHandler(UserDisabledException.class)
    public ResponseEntity<String> handleUserDisabledException(UserDisabledException ex) {
        return ResponseEntity.status(ex.getHTTP_STATUS_CODE())
                .contentType(MediaType.TEXT_PLAIN).body(ex.getMessage());
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(UserLockedException.class)
    public ResponseEntity<String> handleUserLockedException(UserLockedException ex){
        return ResponseEntity.status(ex.getHTTP_STATUS_CODE())
                .contentType(MediaType.TEXT_PLAIN).body(ex.getMessage());
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
