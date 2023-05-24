package com.eventforge.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
@Data
@Builder
public class EventResponse {
    private String name;
    private String description;
    private String address;
    private List<String> eventCategories;
    private UUID organisationId;
    private boolean isOnline;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime startsAt;
    private LocalDateTime endsAt;
}
