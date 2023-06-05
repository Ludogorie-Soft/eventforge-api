package com.eventforge.controller;
import com.eventforge.dto.EventRequest;
import com.eventforge.dto.EventResponse;
import com.eventforge.dto.OrganisationRequest;
import com.eventforge.dto.OrganisationResponse;
import com.eventforge.model.Organisation;
import com.eventforge.model.User;
import com.eventforge.security.jwt.JWTService;
import com.eventforge.service.OrganisationService;
import com.eventforge.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/organisation")
public class OrganisationController {

    private final UserService userService;

    private final JWTService jwtService;

    private final OrganisationService organisationService;

    private static final String AUTHORIZATION ="Authorization";


    @GetMapping("/proba")
    public ResponseEntity<String> proba(@RequestHeader(AUTHORIZATION) String authorization){

        User user = userService.getLoggedUserByToken(jwtService.extractTokenValueFromHeader(authorization));

        return ResponseEntity.ok().body(user.getUsername());
    }
    @PutMapping("/update-account")
    public ResponseEntity<String> updateOrganisation(@Valid @RequestBody OrganisationRequest organisationRequest ,@RequestHeader(AUTHORIZATION) String authHeader) {
        organisationService.updateOrganisation(organisationRequest, jwtService.extractTokenValueFromHeader(authHeader));
        return new ResponseEntity<>("Успешно обновихте акаунта си.", HttpStatus.OK);
    }
    @GetMapping("/{organisationId}")
    public ResponseEntity<OrganisationResponse> getOrganisation(@PathVariable("organisationId") Long uuid) {
        return ResponseEntity.ok(organisationService.getOrganisationById(uuid));
    }
    @GetMapping("/allOrganisations")
    public ResponseEntity<List<OrganisationResponse>> getAllOrganisations() {
        return ResponseEntity.ok(organisationService.getAllOrganisations());
    }
    @GetMapping("/getOrgByName/{name}")
    public ResponseEntity<OrganisationResponse> getOrganisationByName(@PathVariable("name")String name){
        return ResponseEntity.ok(organisationService.getOrgByName(name));
    }
    @DeleteMapping(path = "{organisationId}")
    public ResponseEntity<String> deleteOrganisation(@PathVariable("organisationId") Long id) {
        organisationService.deleteOrganisation(id);
        return new ResponseEntity<>("Успешно изтрита организация!", HttpStatus.OK);
    }
    @PostMapping("/updateOrganisationIsEnabled")
    public ResponseEntity<String> updateOrganisationIsEnabled(@RequestParam("orgName") String orgName) {
        organisationService.updateUserEnabled(orgName);
        return ResponseEntity.ok("Успешно одобрихте организацията!");
    }
}
