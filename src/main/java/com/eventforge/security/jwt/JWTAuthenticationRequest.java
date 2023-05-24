package com.eventforge.security.jwt;

import lombok.Data;

@Data
public class JWTAuthenticationRequest {

    private String username;

    private String password;
}
