package com.eventforge.controller;

import com.eventforge.model.User;
import com.eventforge.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ProbController {

    private final UserService userService;


    private static final String AUTHORIZATION = "Authorization";
    @GetMapping("/proba")
    public ResponseEntity<String> proba(@RequestHeader(AUTHORIZATION) String authorization) {

        User user = userService.getLoggedUserByToken(authorization);

        return ResponseEntity.ok().body(user.getUsername());
    }

}
