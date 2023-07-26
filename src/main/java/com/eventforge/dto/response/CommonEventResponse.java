package com.eventforge.dto.response;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CommonEventResponse {
    private Long id;
    private Long orgId;
    private Long imageId;
    private String imageUrl;
    private String name;
    private String organisationName;
    private boolean isOnline;
    private String address;
    private String eventCategories;
    private String price;
    private String ageBoundary;
    @DateTimeFormat(pattern = "dd-MM-yyyy HH:mm")
    private LocalDateTime startsAt;
    @DateTimeFormat(pattern = "dd-MM-yyyy HH:mm")
    private LocalDateTime endsAt;
    private String description;
    private String isOneTime;
    private String recurrenceDetails;
}
