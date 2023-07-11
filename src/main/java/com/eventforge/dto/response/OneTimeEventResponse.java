package com.eventforge.dto.response;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OneTimeEventResponse {
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
    @DateTimeFormat(pattern = "dd-MM-yyyy HH:mm")
    private LocalDateTime startsAt;
    @DateTimeFormat(pattern = "dd-MM-yyyy HH:mm")
    private LocalDateTime endsAt;
}
