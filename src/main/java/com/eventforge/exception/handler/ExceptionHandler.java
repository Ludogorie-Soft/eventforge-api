package com.eventforge.exception.handler;

import com.eventforge.slack.SlackNotifier;
import com.eventforge.exception.*;
import com.eventforge.service.Utils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RestControllerAdvice
@RequiredArgsConstructor
public class ExceptionHandler {

    private final Utils utils;

    private final SlackNotifier slackNotifier;
    @org.springframework.web.bind.annotation.ExceptionHandler(Throwable.class)
    public void sendNotificationToSlackWhenInternalServerErrorOccurs(Throwable throwable) {
        // Get the current timestamp
        LocalDateTime timestamp = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

        // Create a detailed message including timestamp, exception message, and cause
        String message = String.format(
                "Exception occurred at %s%nBACKEND APPLICATION%nMessage: %s%nCause: %s",
                formatter.format(timestamp),
                throwable.getMessage(),
                throwable.getCause()
        );
        // Log the exception (optional)
        throwable.printStackTrace();

        // Send the exception details to Slack
        slackNotifier.sendNotification(message);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<String> handleUserNotFoundException(UsernameNotFoundException ex){
        return ResponseEntity.status(321).contentType(MediaType.TEXT_PLAIN).body(ex.getMessage());
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


    @org.springframework.web.bind.annotation.ExceptionHandler(ImageException.class)
    public ResponseEntity<String> handleImageException(ImageException ex) {
        return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE).contentType(MediaType.TEXT_PLAIN).body(ex.getMessage());
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(InvalidPasswordException.class)
    public ResponseEntity<String> handleInvalidPasswordException(InvalidPasswordException ex){
        return ResponseEntity.status(ex.getHTTP_STATUS_CODE()).contentType(MediaType.TEXT_PLAIN).body(ex.getMessage());
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(EventRequestException.class)
    public ResponseEntity<String> handleEventRequestException(EventRequestException exception) {
        return ResponseEntity.status(exception.getHTTP_STATUS_CODE()).contentType(MediaType.TEXT_PLAIN).body(exception.getMessage());
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(OrganisationRequestException.class)
    public ResponseEntity<String> handleOrganisationRequestException(OrganisationRequestException ex){
        return ResponseEntity.status(ex.getHTTP_STATUS_CODE()).contentType(MediaType.TEXT_PLAIN).body(ex.getMessage());
    }
}
