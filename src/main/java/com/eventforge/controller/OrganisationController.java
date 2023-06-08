package com.eventforge.controller;

import com.eventforge.dto.request.OrganisationRequest;
import com.eventforge.dto.request.UpdateAccountRequest;
import com.eventforge.dto.response.OrganisationResponse;
import com.eventforge.security.jwt.JWTService;
import com.eventforge.service.OrganisationService;
import com.eventforge.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/organisation")
public class OrganisationController {

    private static final String AUTHORIZATION = "Authorization";
    private final UserService userService;
    private final JWTService jwtService;
    private final OrganisationService organisationService;

    @PutMapping("/update-account")
    public ResponseEntity<String> updateAccount(@RequestHeader(AUTHORIZATION) String authHeader, @Valid @RequestBody UpdateAccountRequest request) {
        organisationService.updateOrganisation(request, authHeader);
        return new ResponseEntity<>("Успешно обновихте аканта си.", HttpStatus.OK);
    }
//    @PutMapping("/update-account")
//    public ResponseEntity<String> updateOrganisation(@Valid @RequestBody OrganisationRequest organisationRequest, @RequestHeader(AUTHORIZATION) String authHeader) {
//        organisationService.updateOrganisation(organisationRequest, jwtService.extractTokenValueFromHeader(authHeader));
//        return new ResponseEntity<>("Успешно обновихте акаунта си.", HttpStatus.OK);
//    }

    @GetMapping("/{organisationId}")
    public ResponseEntity<OrganisationResponse> getOrganisation(@PathVariable("organisationId") Long uuid) {
        return ResponseEntity.ok(organisationService.getOrganisationById(uuid));
    }

    @GetMapping("/getOrgByName/{name}")
    public ResponseEntity<OrganisationResponse> getOrganisationByName(@PathVariable("name") String name) {
        return ResponseEntity.ok(organisationService.getOrgByName(name));
    }
}
