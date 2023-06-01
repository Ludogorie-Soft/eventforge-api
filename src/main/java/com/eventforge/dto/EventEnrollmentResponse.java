package com.eventforge.dto;

import com.eventforge.model.Event;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.UUID;
@Data
@Builder
public class EventEnrollmentResponse {
    private UUID id;
    private List<Event> eventId;
    private String phone;
    private String externalLink;
    private String email;
}
