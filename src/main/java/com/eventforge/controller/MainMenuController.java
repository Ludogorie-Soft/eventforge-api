package com.eventforge.controller;

import com.eventforge.dto.response.EventResponse;
import com.eventforge.dto.response.OrganisationResponse;
import com.eventforge.service.EventService;
import com.eventforge.service.OrganisationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/menu")
public class MainMenuController {

    private final EventService eventService;

    private final OrganisationService organisationService;
    @GetMapping("/events")
    public ResponseEntity<List<EventResponse>> showThreeUpcomingEvents(){
        return new ResponseEntity<>(eventService.getThreeUpcomingEvents() , HttpStatus.OK);
    }
    @GetMapping("/organisations")
    public ResponseEntity<List<OrganisationResponse>> showThreeRandomOrganisations(){
        return new ResponseEntity<>(organisationService.fetchThreeRandomOrganisations() , HttpStatus.OK);
    }

}
