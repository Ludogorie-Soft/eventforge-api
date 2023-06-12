package com.eventforge.controller;

import com.eventforge.dto.response.RecurrenceEventResponse;
import com.eventforge.service.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/recurrence-events")
public class RecurrenceEventController{

    private final EventService eventService;

    @GetMapping("/active")
    public ResponseEntity<List<RecurrenceEventResponse>> showAllActiveRecurrenceEvents(@RequestParam(value = "order" , required = false) String order){
        return new ResponseEntity<>(eventService.getAllActiveRecurrenceEvents(order) , HttpStatus.OK);
    }

    @GetMapping("/expired")
    public ResponseEntity<List<RecurrenceEventResponse>> showAllExpiredRecurrenceEvents(@RequestParam(value = "order" , required = false) String order){
        return new ResponseEntity<>(eventService.getAllExpiredRecurrenceEvents(order), HttpStatus.OK);
    }
}
