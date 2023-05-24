package com.eventforge.dto;

import com.eventforge.model.Event;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Data;

import java.util.List;
@Data
@Builder
public class EventEnrollmentRequest {
    @NotNull
    private List<Event> eventId;
    @Pattern(regexp = "^[0-9]{10}$", message = "Телефонният номер трябва да вклъчва 10 цифри!")
    private String number;
    private String phone;
    private String externalLink;
    @Email
    private String email;
}
