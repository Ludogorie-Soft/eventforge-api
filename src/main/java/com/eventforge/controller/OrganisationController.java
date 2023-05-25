package com.eventforge.controller;

import com.eventforge.dto.RegistrationRequest;
import com.eventforge.model.Organisation;
import com.eventforge.service.OrganisationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class OrganisationController {

    private final OrganisationService organisationService;

    @PostMapping("/registration")
    public ResponseEntity<Organisation> registerOrganisation(@RequestBody RegistrationRequest request){
        return new ResponseEntity<>(organisationService.registerUser(request) , HttpStatus.CREATED);
    }
}
