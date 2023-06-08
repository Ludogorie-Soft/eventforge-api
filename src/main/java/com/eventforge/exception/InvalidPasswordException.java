package com.eventforge.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
@Getter
public class InvalidPasswordException extends RuntimeException{
    private final int HTTP_STATUS_CODE = HttpStatus.CONFLICT.value();

    public InvalidPasswordException(String message){
        super(message);
    }
}
