package com.eventforge.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
@Data
@Builder
public class EventRequest {
    @Size(min=5, max = 30, message = "Името на събитието трява да е между 5 и 30 символа!")
    private String name;
    private String description;
    private String address;
    private List<String> eventCategories;
    @NotBlank
    @NotNull
    private UUID organisationId;
    private boolean isOnline;
    private LocalDateTime startsAt;
    private LocalDateTime endsAt;
}