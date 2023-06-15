package com.eventforge.controller;

import com.eventforge.dto.request.CriteriaFilterRequest;
import com.eventforge.dto.response.OneTimeEventResponse;
import com.eventforge.service.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/events")
public class EventController {
    private final EventService eventService;


    @PostMapping("/filter-by-criteria")
    public ResponseEntity<List<?>>getEventsByCriteria(@RequestBody CriteriaFilterRequest filterRequest){
        return new ResponseEntity<>(eventService.filterEventsByCriteria(filterRequest), HttpStatus.OK);
    }


    @GetMapping(path = "{eventId}")
    public ResponseEntity<OneTimeEventResponse> getEvent(@PathVariable("eventId") Long uuid) {
        return ResponseEntity.ok(eventService.getEventById(uuid));
    }

}
