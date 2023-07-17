package com.eventforge.dto.response;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrganisationResponse {

    private Long orgId;
    private String logo;
    private String background;
    private String name;
    private String bullstat;
    private String address;
    private String charityOption;
    private String organisationPurpose;
    private Set<String> organisationPriorities;
    private List<CommonEventResponse> expiredEvents;
    private List<CommonEventResponse> activeEvents;
    private List<CommonEventResponse> upcomingEvents;
    private LocalDateTime registeredAt;
    private LocalDateTime updatedAt;
}
