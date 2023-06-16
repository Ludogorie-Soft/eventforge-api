package com.eventforge.controller;

import com.eventforge.dto.response.OneTimeEventResponse;
import com.eventforge.dto.response.OrganisationResponse;
import com.eventforge.dto.response.RecurrenceEventResponse;
import com.eventforge.dto.response.container.EventResponseContainer;
import com.eventforge.model.Organisation;
import com.eventforge.service.Impl.EventServiceImpl;
import com.eventforge.service.OrganisationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/unauthorized")
public class UnauthorizedUserController {

    private final EventServiceImpl eventService;

    private final OrganisationService organisationService;
    @GetMapping
    public ResponseEntity<List<OrganisationResponse>> showAllOrganisations(){
        return new ResponseEntity<>(organisationService.getAllOrganisations() , HttpStatus.OK);
    }

    @GetMapping("/{orgName}/get-events/{orgId}")
    public ResponseEntity<EventResponseContainer> showOrgEvents(@PathVariable("orgName")String organisationName , @PathVariable("orgId")Long id){

        List<OneTimeEventResponse> oneTimeEvents = eventService.getAllOneTimeEventsByOrganisationId(id);
        List< RecurrenceEventResponse> recurrenceEvents = eventService.getAllRecurrenceEventsByOrganisationId(id);
        EventResponseContainer eventResponseContainer = new EventResponseContainer(oneTimeEvents , recurrenceEvents);
        return new ResponseEntity<>(eventResponseContainer , HttpStatus.OK);
    }
}
