package com.eventforge.dto.request;

import com.eventforge.model.OrganisationPriority;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.Set;

@Data
@Builder
public class OrganisationRequest {
    @NotNull
    private String name;
    private String bullstat;
    @NotNull
    private String username;
    private String fullName;
    @Pattern(regexp = "^[0-9]{10}$", message = "Телефонният номер трябва да вклъчва 10 цифри!")
    private String phone;
    private String address;
    private String charityOption;
    private String organisationPurpose;
    private List<String> categories;
    private Set<OrganisationPriority> organisationPriorities;
}