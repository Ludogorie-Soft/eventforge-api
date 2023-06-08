package com.eventforge.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class JWTAuthenticationRequest {

    private String userName;

    private String password;
}
