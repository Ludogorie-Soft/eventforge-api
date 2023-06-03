package com.eventforge.exception;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.IOException;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@RestControllerAdvice
public class ExceptionHandler {

    @org.springframework.web.bind.annotation.ExceptionHandler(GlobalException.class)
    public void handleRuntimeException(GlobalException ex, HttpServletResponse response) throws  IOException {
        response.setStatus(ex.getHttpStatus());
        response.setCharacterEncoding("UTF-8");
        response.setContentType(MediaType.TEXT_PLAIN_VALUE);
        response.getWriter().write(ex.getMessage());
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
