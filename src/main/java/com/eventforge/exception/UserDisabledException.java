package com.eventforge.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
@Getter
public class UserDisabledException extends RuntimeException{
    private final int HTTP_STATUS_CODE = HttpStatus.SERVICE_UNAVAILABLE.value();

    public UserDisabledException(){
        super("Моля потвърдете първо електронната си поща.");
    }
}
