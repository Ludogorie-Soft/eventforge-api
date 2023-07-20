package com.eventforge.exception;

import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;

@Getter
public class InvalidPasswordException extends RuntimeException{
    private final int HTTP_STATUS_CODE = HttpServletResponse.SC_CONFLICT;
    public InvalidPasswordException(String message){
        super(message);
    }
}
