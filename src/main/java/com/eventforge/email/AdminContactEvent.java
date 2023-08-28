package com.eventforge.email;


import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

@Getter
@Setter
public class AdminContactEvent extends ApplicationEvent {
    private Long contactId;
    private String adminAnswer;

    public AdminContactEvent(Long contactId , String adminAnswer) {
        super(contactId);
        this.contactId = contactId;
        this.adminAnswer = adminAnswer;
    }
}
