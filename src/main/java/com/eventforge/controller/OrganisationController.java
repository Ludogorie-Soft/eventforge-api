package com.eventforge.controller;

import com.eventforge.service.OrganisationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class OrganisationController {

    private final OrganisationService organisationService;


    @GetMapping("/proba")
    public String proba(@RequestHeader("Authorization") String authorization){
        return "proba";
    }
}
