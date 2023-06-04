package com.eventforge.service;

import com.eventforge.dto.EventRequest;
import com.eventforge.dto.EventResponse;

import java.util.List;
import java.util.UUID;

public interface EventService {
    List<EventResponse> getAllEvents1();

    void createEvent(EventRequest eventRequest);
    List<EventResponse> getAllEvents();

    EventResponse getEventById(Long eventId);
    EventResponse getEventByName(String name);

    void updateEvent(Long eventId, EventRequest eventRequest);

    void deleteEvent(Long eventId);

}
