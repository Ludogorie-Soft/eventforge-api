package com.eventforge.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrganisationResponse {
    private UUID id;
    private String name;
    private String bullstat;
    private String email;
    private String phone;
    private String address;
    private String charityOption;
    private String purposeOfOrganisation;
    private List<String> categories;
//    private LocalDateTime registeredAt;
//    private LocalDateTime updatedAt;
}
