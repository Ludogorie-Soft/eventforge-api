package com.eventforge.controller;

import com.eventforge.dto.request.CriteriaFilterRequest;
import com.eventforge.service.Impl.EventServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class EventController {
    private final EventServiceImpl eventService;


    @PostMapping("/filter-by-criteria")
    public ResponseEntity<List<?>>getEventsByCriteria(@RequestBody CriteriaFilterRequest filterRequest){
        return new ResponseEntity<>(eventService.filterEventsByCriteria(filterRequest), HttpStatus.OK);
    }

    @DeleteMapping("/delete-event/{id}")
    public ResponseEntity<String> deleteEventById(@RequestHeader("Authorization") String authHeader , @PathVariable("id")Long id){
        eventService.deleteEventById(id);
        return new ResponseEntity<>("Успешно изтрихте събитието" , HttpStatus.OK);
    }

}
