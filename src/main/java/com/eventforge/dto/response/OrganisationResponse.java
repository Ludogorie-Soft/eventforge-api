package com.eventforge.dto.response;

import lombok.*;

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
    private String email;
    private String address;
    private String website;
    private String facebookLink;
    private String charityOption;
    private String organisationPurpose;
    private Set<String> organisationPriorities;
    private List<EventResponse> expiredEvents;
    private List<EventResponse> activeEvents;
    private List<EventResponse> upcomingEvents;

}
