package com.eventforge.controller;

import com.eventforge.dto.EventRequest;
import com.eventforge.dto.EventResponse;
import com.eventforge.service.EventService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

import static org.springframework.http.HttpStatus.ACCEPTED;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/events")
public class EventController {
    private final EventService eventService;

    @GetMapping
    public ResponseEntity<List<EventResponse>> getAllEvents() {
        return ResponseEntity.ok(eventService.getAllEvents());
    }

    @GetMapping("/{eventId}")
    public ResponseEntity<EventResponse> getEvent(@PathVariable("eventId") UUID uuid) {
        return ResponseEntity.ok(eventService.getEventById(uuid));
    }
    @GetMapping("/{name}")
    public ResponseEntity<EventResponse> getEvent(@PathVariable("name") String name) {
        return ResponseEntity.ok(eventService.getEventByName(name));
    }

    @PostMapping
    public ResponseEntity<EventResponse> createEvent(@Valid @RequestBody EventRequest eventRequest) {
        return new ResponseEntity<>(eventService.saveEvent(eventRequest), HttpStatus.CREATED);
    }

    @PutMapping("/{eventId}")
    public ResponseEntity<String> updateEvent(@PathVariable("eventId") UUID id,
                                              @Valid @RequestBody EventRequest eventRequest) {
        eventService.updateEvent(id, eventRequest);
        return new ResponseEntity<>("Всички промени са направени", HttpStatus.OK);
    }

    @DeleteMapping("/{eventId}")
    public ResponseEntity<String> deleteEvent(@PathVariable("eventId") UUID id) {
        eventService.deleteEvent(id);
        return new ResponseEntity<>("Събитието е изтрито успешно!", HttpStatus.OK);
    }
    @GetMapping("/filter")
    public ResponseEntity<List<EventResponse>> filterEvents(@RequestParam(required = false) String name,
                                                            @RequestParam(required = false) String description,
                                                            @RequestParam(required = false) String address,
                                                            @RequestParam(required = false) String organisationName,
                                                            @RequestParam(required = false) String date) {
        return new ResponseEntity<>(eventService.filterEventsByCriteria(name, description, address, organisationName, date), ACCEPTED);
    }
}
