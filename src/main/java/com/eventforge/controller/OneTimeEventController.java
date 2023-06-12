package com.eventforge.controller;

import com.eventforge.dto.response.OneTimeEventResponse;
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
@RequestMapping("/v1/api/one-time-events")
public class OneTimeEventController {

    private final EventService eventService;

    @GetMapping("/active")
    public ResponseEntity<List<OneTimeEventResponse>> showAllActiveOneTimeEvents(@RequestParam(value = "order" , required = false) String order) {
        return new ResponseEntity<>(eventService.getAllActiveOneTimeEvents(order), HttpStatus.OK);
    }

    @GetMapping("/expired")
    public ResponseEntity<List<OneTimeEventResponse>>showAllExpiredOneTimeEvents(@RequestParam(value = "order" , required = false) String order){
        return new ResponseEntity<>(eventService.getAllExpiredOneTimeEvents(order), HttpStatus.OK);
    }

}
