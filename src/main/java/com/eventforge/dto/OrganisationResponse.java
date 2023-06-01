package com.eventforge.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
@Data
@Builder
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
    private LocalDateTime registeredAt;
    private LocalDateTime updatedAt;
}
