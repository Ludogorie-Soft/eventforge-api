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

    @GetMapping("/{orgId}/{orgName}/get-events")
    public ResponseEntity<List<CommonEventResponse>> showOrgEvents(@PathVariable("orgId")Long orgId,@PathVariable("orgName")String organisationName ){
        List<CommonEventResponse> events = eventService.getAllEventsOfOrganisationByOrganisationNameAndId(orgId , organisationName);
        return new ResponseEntity<>(events , HttpStatus.OK);
    }
}
