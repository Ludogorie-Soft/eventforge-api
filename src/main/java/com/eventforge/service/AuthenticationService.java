package com.eventforge.service;

import com.eventforge.exception.GlobalException;
import com.eventforge.security.jwt.JWTAuthenticationRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final AuthenticationManager authenticationManager;


    public Authentication authenticate(JWTAuthenticationRequest authRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.getUserName(), authRequest.getPassword())
            );
            return authentication;
        } catch (BadCredentialsException ex) {
            throw new GlobalException("Грешно потребителско име или парола");

        }
    }

}
