package com.eventforge.dto;

import com.eventforge.model.User;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Data;

import java.util.List;
@Data
@Builder
public class OrganisationRequest {
    @NotNull
    private String name;
    private String bullstat;
    @NotNull
    private User user;
    @Pattern(regexp = "^[0-9]{10}$", message = "Телефонният номер трябва да вклъчва 10 цифри!")
    private String phone;
    private String address;
    private String charityOption;
    private String purposeOfOrganisation;
    private List<String> categories;
}