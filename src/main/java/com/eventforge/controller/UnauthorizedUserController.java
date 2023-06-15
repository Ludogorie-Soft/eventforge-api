package com.eventforge.controller;

import com.eventforge.dto.response.OrganisationResponse;
import com.eventforge.service.OrganisationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/unauthorized")
public class UnauthorizedUserController {

    private final OrganisationService organisationService;

    public ResponseEntity<List<OrganisationResponse>> showAllOrganisations(){
        return new ResponseEntity<>(organisationService.getAllOrganisations() , HttpStatus.OK);
    }
}
