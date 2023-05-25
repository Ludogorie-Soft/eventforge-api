package com.eventforge.security.jwt;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class JWTAuthenticationRequest {

    private String userName;

    private String password;
}
