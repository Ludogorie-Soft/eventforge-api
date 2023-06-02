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
    @GetMapping(path = "{organisationId}")
    public ResponseEntity<OrganisationResponse> getOrganisation(@PathVariable("organisationId") UUID uuid) {
        return ResponseEntity.ok(organisationService.getOrganisationById(uuid));
    }
}
