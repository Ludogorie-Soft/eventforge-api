package com.eventforge.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Data
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
