package com.eventforge.email;

import com.eventforge.model.User;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;
@Getter
@Setter
public class RegistrationCompleteEvent extends ApplicationEvent {

    private transient User user;
    private String email;
    private String applicationUrl;

    public RegistrationCompleteEvent(User user,String email ,String applicationUrl) {
        super(applicationUrl);
        this.user = user;
        this.email = email;
        this.applicationUrl = applicationUrl;
    }
}
