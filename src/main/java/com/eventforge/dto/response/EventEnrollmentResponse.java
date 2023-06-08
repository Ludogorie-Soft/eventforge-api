package com.eventforge.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EventEnrollmentResponse {
    private Long id;
    private String eventName;
    private String phone;
    private String externalLink;
    private String email;
}
//админите ще могат да виждат всички записани хора към събитията , а организациите ще могат да виждат само записаните хора към техните събития