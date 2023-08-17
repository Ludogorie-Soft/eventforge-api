package com.eventforge.exception;

import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;

@Getter
public class InvalidCredentialsException extends RuntimeException{

    private final int httpStatusCode = HttpServletResponse.SC_NOT_FOUND;

    public InvalidCredentialsException(){
        super("Невалидна потребителска поща или парола");
    }
}
