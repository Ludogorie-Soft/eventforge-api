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

    private final UserService userService;

    private final JWTService jwtService;


    @GetMapping("/proba")
    public ResponseEntity<String> proba(@RequestHeader("Authorization") String authorization){

        User user = userService.getLoggedUserByToken(jwtService.extractTokenValueFromHeader(authorization));

        return ResponseEntity.ok().body(user.getUsername());
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
