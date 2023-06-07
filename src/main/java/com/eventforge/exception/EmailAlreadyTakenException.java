package com.eventforge.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class EmailAlreadyTakenException extends RuntimeException{

    private final int httpStatus = HttpStatus.FOUND.value();

    public EmailAlreadyTakenException(){
        super("Въведената електронна поща е вече заета.Моля опитайте с друга");
    }
}
