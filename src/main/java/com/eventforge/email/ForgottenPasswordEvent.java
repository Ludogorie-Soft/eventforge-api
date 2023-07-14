package com.eventforge.email;

import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;
@Getter
@Setter
public class ForgottenPasswordEvent extends ApplicationEvent {

    private String email;
    private String applicationUrl;
    private String generatedPassword;
    public ForgottenPasswordEvent(String email , String applicationUrl , String generatedPassword) {
        super(email);
        this.email = email;
        this.applicationUrl = applicationUrl;
        this.generatedPassword = generatedPassword;
    }
}
