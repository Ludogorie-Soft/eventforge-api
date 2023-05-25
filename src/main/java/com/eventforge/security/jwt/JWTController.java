package com.eventforge.security.jwt;

;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import org.springframework.web.bind.annotation.RestController;


@RequiredArgsConstructor
@RestController
public class JWTController {

    private final JWTService jwtService;
    private final AuthenticationManager authenticationManager;


    @PostMapping("/authenticate")
    public String getTokenForAuthenticatedUser(@RequestBody JWTAuthenticationRequest authRequest){
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUserName(), authRequest.getPassword()));
        if(authentication.isAuthenticated()){
            return jwtService.getGeneratedToken(authRequest.getUserName());
        } else {
            throw new UsernameNotFoundException("Грешно въведен имейл или парола.");
        }
    }

}

