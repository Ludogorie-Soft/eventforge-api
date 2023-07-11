package com.eventforge.controller;

import com.eventforge.dto.response.OneTimeEventResponse;
import com.eventforge.dto.response.OrganisationResponse;
import com.eventforge.dto.response.RecurrenceEventResponse;
import com.eventforge.dto.response.container.EventResponseContainer;
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

    @GetMapping("/{orgName}/get-events")
    public ResponseEntity<EventResponseContainer> showOrgEvents(@PathVariable("orgName")String organisationName){
        Long id = organisationService.getOrgByName(organisationName).getOrgId();
        List<OneTimeEventResponse> oneTimeEvents = eventService.getAllOneTimeEventsByOrganisationId(id);
        List< RecurrenceEventResponse> recurrenceEvents = eventService.getAllRecurrenceEventsByOrganisationId(id);
        EventResponseContainer eventResponseContainer = new EventResponseContainer(oneTimeEvents , recurrenceEvents);
        return new ResponseEntity<>(eventResponseContainer , HttpStatus.OK);
    }
}
