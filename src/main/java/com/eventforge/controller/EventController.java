package com.eventforge.controller;

import com.eventforge.dto.request.CriteriaFilterRequest;
import com.eventforge.service.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class EventController {
    private final EventService eventService;


    @PostMapping("/filter-by-criteria")
    public ResponseEntity<List<?>>getEventsByCriteria(@RequestBody CriteriaFilterRequest filterRequest){
        return new ResponseEntity<>(eventService.filterEventsByCriteria(filterRequest), HttpStatus.OK);
    }

    @DeleteMapping("/delete-event/{id}")
    public ResponseEntity<String> deleteEventById(@RequestHeader("Authorization") String authHeader , @PathVariable("id")Long id){
        eventService.deleteEventByIdAndUserIdForOrganisation(id , authHeader);
        return new ResponseEntity<>("Успешно изтрихте събитието" , HttpStatus.OK);
    }

}
