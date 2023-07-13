package com.eventforge.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
    @JsonIgnore
    private Long orgId;
    private String logo;
    private String background;
    private String name;
    private String bullstat;
    private String address;
    private String charityOption;
    private String organisationPurpose;
    private Set<String> organisationPriorities;
    private List<CommonEventResponse> organisationEvents;
    private LocalDateTime registeredAt;
    private LocalDateTime updatedAt;
}
