package com.eventforge.dto;

import com.eventforge.model.Event;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class EventEnrollmentResponse {
    private Long id;
    private List<Event> eventId;
    private String phone;
    private String externalLink;
    private String email;
}
