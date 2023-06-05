package com.eventforge.controller;

import com.eventforge.dto.EventRequest;
import com.eventforge.dto.EventResponse;
import com.eventforge.service.EventService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/events")
public class EventController {
    private final EventService eventService;

    private static final String AUTHORIZATION = "Authorization";

    @GetMapping
    public ResponseEntity<List<EventResponse>> showAllEvents(@Param("orderBy")String orderBy){
        return new ResponseEntity<>(eventService.getAllEvents(orderBy) ,HttpStatus.OK);
    }

    @GetMapping(path = "{eventId}")
    public ResponseEntity<EventResponse> getEvent(@PathVariable("eventId") Long uuid) {
        return ResponseEntity.ok(eventService.getEventById(uuid));
    }
    @GetMapping(path = "{name}")
    public ResponseEntity<EventResponse> getEvent(@PathVariable("name") String name) {
        return ResponseEntity.ok(eventService.getEventByName(name));
    }


    @PutMapping(path = "{eventId}")
    public ResponseEntity<String> updateEvent(@PathVariable("eventId") Long id,
                                              @Valid @RequestBody EventRequest eventRequest) {
        eventService.updateEvent(id, eventRequest);
        return new ResponseEntity<>("All changes are done", HttpStatus.OK);
    }

    @DeleteMapping(path = "{eventId}")
    public ResponseEntity<String> deleteEvent(@PathVariable("eventId") Long id) {
        eventService.deleteEvent(id);
        return new ResponseEntity<>("Event has been deleted successfully!!", HttpStatus.OK);
    }


    @PostMapping("/create-event")
    public ResponseEntity<String> createEvent(@RequestBody EventRequest eventRequest , @RequestHeader(AUTHORIZATION) String authHeader){
        eventService.saveEvent(eventRequest , authHeader);
        return new ResponseEntity<>("Успешно създано събитие" , HttpStatus.CREATED);
    }
}
