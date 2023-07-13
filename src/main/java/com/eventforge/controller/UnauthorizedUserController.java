package com.eventforge.controller;

import com.eventforge.dto.response.CommonEventResponse;
import com.eventforge.dto.response.OrganisationResponse;
import com.eventforge.service.EventService;
import com.eventforge.service.OrganisationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/unauthorized")
public class UnauthorizedUserController {

    private final EventService eventService;

    private final OrganisationService organisationService;
    @GetMapping
    public ResponseEntity<List<OrganisationResponse>> showAllOrganisationsForUnauthorizedUser(@RequestParam(name = "search" ,required = false)String search){
        return new ResponseEntity<>(organisationService.getAllOrganisationsForUnauthorizedUser(search) , HttpStatus.OK);
    }
    @GetMapping("/organisation/details/{organisationId}")
    public ResponseEntity<OrganisationResponse> getOrganisationDetails(@PathVariable("organisationId") Long id) {
        return ResponseEntity.ok(organisationService.getOrganisationDetailsByIdWithCondition(id));
    }
    @GetMapping("/event/details/{id}")
    public ResponseEntity<CommonEventResponse> showEventDetails(@PathVariable("id")Long id){
        return new ResponseEntity<>(eventService.getEventDetailsByIdForAllUsers(id) ,HttpStatus.OK);
    }
}
