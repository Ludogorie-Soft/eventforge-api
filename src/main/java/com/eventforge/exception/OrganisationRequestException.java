package com.eventforge.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
@Getter
public class OrganisationRequestException extends RuntimeException{

    private final int HTTP_STATUS_CODE = HttpStatus.GONE.value();
    public OrganisationRequestException(String message) {
        super(message);
    }
}
