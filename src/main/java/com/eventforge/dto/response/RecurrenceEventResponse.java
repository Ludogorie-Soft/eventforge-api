package com.eventforge.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class RecurrenceEventResponse {
    private Long id;
    private Long imageId;
    private String imageUrl;
    private String name;
    private String description;
    private String address;
    private String eventCategories;
    private String organisationName;
    private String price;
    private String ageBoundary;
    private LocalDateTime startsAt;
    private LocalDateTime endsAt;
    private String recurrenceDetails;

}
