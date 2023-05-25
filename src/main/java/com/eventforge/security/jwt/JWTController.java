package com.eventforge.security.jwt;

;
import com.eventforge.service.AuthenticationService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;


@RequiredArgsConstructor
@RestController
public class JWTController {

    private final JWTService jwtService;
    private final AuthenticationService authenticationService;


    @PostMapping("/authenticate")
    public String getTokenForAuthenticatedUser(@RequestBody JWTAuthenticationRequest authRequest){
        Authentication authentication = authenticationService.authenticate(authRequest);
        if(authentication.isAuthenticated()){
            return jwtService.getGeneratedToken(authRequest.getUserName());
        } else {
            throw new UsernameNotFoundException("Грешно въведен имейл или парола.");
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout() {
        // You can perform additional logout-related logic here if needed
        return ResponseEntity.ok("Logged out successfully.");
    }


}

