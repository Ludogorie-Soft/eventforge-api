package com.eventforge.exception;

import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;

@Getter
public class DateTimeException extends RuntimeException{
    private final int HTTP_STATUS_CODE = HttpServletResponse.SC_SEE_OTHER;

    public DateTimeException(){
        super("Не може датата на започване да е по-голяма от датата на приключване");
    }
}
