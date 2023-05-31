package com.eventforge.exception;

public class EventRequestException extends RuntimeException {
    public EventRequestException(String message) {
        super(message);
    }

    public EventRequestException(String message, Throwable cause) {
        super(message, cause);
    }
}
