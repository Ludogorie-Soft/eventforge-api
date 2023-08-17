package com.eventforge.exception;

import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
@Getter
public class InvalidEmailConfirmationLinkException extends RuntimeException{
    private final int httpStatusCode = HttpServletResponse.SC_BAD_REQUEST;

    public InvalidEmailConfirmationLinkException(String message){
        super(message);
    }
}
