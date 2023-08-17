package com.eventforge.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class EmailConfirmationNotSentException extends RuntimeException{

    private final int httpStatusCode = HttpStatus.EXPECTATION_FAILED.value();

    public EmailConfirmationNotSentException(String email){
        super("Възникна проблем при изпращането на потвърждение към адрес с електронна поща: "+ email);
    }
}
