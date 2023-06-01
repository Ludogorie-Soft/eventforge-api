package com.eventforge.controller;

import com.eventforge.dto.EventRequest;
import com.eventforge.dto.EventResponse;
import com.eventforge.dto.OrganisationRequest;
import com.eventforge.dto.OrganisationResponse;
import com.eventforge.model.Organisation;
import com.eventforge.service.OrganisationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/organisation")
public class OrganisationController {

    private final OrganisationService organisationService;


    @GetMapping("/proba")
    public String proba(@RequestHeader("Authorization") String authorization){
        return "proba";
    }
    @PutMapping("/update-account")
    public ResponseEntity<String> updateOrganisation(@Valid @RequestBody OrganisationRequest organisationRequest) {
        organisationService.updateOrganisation(organisationRequest);
        return new ResponseEntity<>("Успешно обновихте акаунта си.", HttpStatus.OK);
    }
    @GetMapping(path = "{organisationId}")
    public ResponseEntity<OrganisationResponse> getOrganisation(@PathVariable("organisationId") UUID uuid) {
        return ResponseEntity.ok(organisationService.getOrganisationById(uuid));
    }
}
