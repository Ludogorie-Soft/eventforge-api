package com.eventforge.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EventResponse {
    private Long id;
    private String image;
    private String name;
    private String description;
    private String address;
    private String eventCategories;
    private String organisationName;
    private boolean isOnline;
    private LocalDateTime startsAt;
    private LocalDateTime endsAt;
}
