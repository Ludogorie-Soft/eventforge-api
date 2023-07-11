package com.eventforge.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class JWTAuthenticationRequest {

    private String userName;

    private String password;
}
