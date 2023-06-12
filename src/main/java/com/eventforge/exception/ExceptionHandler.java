package com.eventforge.exception;

import com.eventforge.service.Utils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.ZoneId;
import java.time.ZonedDateTime;

@RestControllerAdvice
@RequiredArgsConstructor
public class ExceptionHandler {

    private final Utils utils;
    @org.springframework.web.bind.annotation.ExceptionHandler(DateTimeException.class)
    public ResponseEntity<String> handleDateTimeException(DateTimeException e){
        return ResponseEntity.status(e.getHTTP_STATUS_CODE())
                .contentType(MediaType.TEXT_PLAIN)
                .body(e.getMessage());
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(EmailAlreadyTakenException.class)
    public ResponseEntity<String> handleEmailAlreadyTakenException(EmailAlreadyTakenException ex) {
        return ResponseEntity.status(ex.getHttpStatus())
                .contentType(MediaType.TEXT_PLAIN)
                .body(ex.getMessage());
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> handleValidationException(MethodArgumentNotValidException ex) {

        String errorString = utils.generateErrorStringFromMethodArgumentNotValidException(ex.getGlobalErrors(), ex.getFieldErrors());

        return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED)
                .contentType(MediaType.TEXT_PLAIN)
                .body(errorString);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(EmailConfirmationNotSentException.class)
    public ResponseEntity<String> handleEmailConfirmationNotSentException(EmailConfirmationNotSentException ex) {
        return ResponseEntity.status(ex.getHTTP_STATUS_CODE())
                .contentType(MediaType.TEXT_PLAIN).body(ex.getMessage());
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<String> handleInvalidCredentialsException(InvalidCredentialsException ex) {
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
    public ResponseEntity<String> handleUserLockedException(UserLockedException ex) {
        return ResponseEntity.status(ex.getHTTP_STATUS_CODE())
                .contentType(MediaType.TEXT_PLAIN).body(ex.getMessage());
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(InvalidPasswordException.class)
    public ResponseEntity<String> handleInvalidPasswordException(InvalidPasswordException ex) {
        return ResponseEntity.status(ex.getHTTP_STATUS_CODE()).contentType(MediaType.TEXT_PLAIN).body(ex.getMessage());
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
