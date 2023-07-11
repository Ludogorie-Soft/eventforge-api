package com.eventforge.exception;

import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;

@Getter
public class EventRequestException extends RuntimeException {
    private final int HTTP_STATUS_CODE = HttpServletResponse.SC_NO_CONTENT;
    public EventRequestException(String message) {
        super(message);
    }
}
