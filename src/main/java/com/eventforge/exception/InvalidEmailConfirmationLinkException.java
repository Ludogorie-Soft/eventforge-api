package com.eventforge.exception;

import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
@Getter
public class InvalidEmailConfirmationLinkException extends RuntimeException{
    private final int HTTP_STATUS_CODE = HttpServletResponse.SC_BAD_REQUEST;

    public InvalidEmailConfirmationLinkException(){
        super("Линкът за активация е невалиден");
    }
}
