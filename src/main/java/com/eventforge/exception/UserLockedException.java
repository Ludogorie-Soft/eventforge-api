package com.eventforge.exception;


import lombok.Getter;
import org.springframework.http.HttpStatus;
@Getter
public class UserLockedException extends RuntimeException{
    private final int HTTP_STATUS_CODE = HttpStatus.LOCKED.value();

    public UserLockedException() {
        super("Забранен е достъпът то този акаунт. За повече информация , свържете се с администратора на сайта.");
    }
}
