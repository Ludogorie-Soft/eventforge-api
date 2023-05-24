package com.eventforge.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RegistrationRequest {

    private String email;

    private String name;

    private String bullstat;

    private String purposeOfOrganisation;
    private String password;
    private String confirmPassword;

}
