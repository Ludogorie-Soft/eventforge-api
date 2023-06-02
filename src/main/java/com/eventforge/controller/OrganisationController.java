package com.eventforge.controller;

import com.eventforge.model.User;
import com.eventforge.security.jwt.JWTService;
import com.eventforge.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/organisation")
public class OrganisationController {

    private final UserService userService;

    private final JWTService jwtService;


    @GetMapping("/proba")
    public ResponseEntity<String> proba(@RequestHeader("Authorization") String authorization){

        User user = userService.getLoggedUserByToken(jwtService.extractTokenValueFromHeader(authorization));

        return ResponseEntity.ok().body(user.getUsername());
    }
}
