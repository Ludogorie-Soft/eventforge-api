package com.eventforge.service;

import com.eventforge.dto.EventRequest;
import com.eventforge.dto.EventResponse;

import java.util.List;

public interface EventService {
    List<EventResponse> getAllEvents();
    List<EventResponse> getAllPassedEvents();

    void saveEvent(EventRequest eventRequest , String authHeader);

    EventResponse getEventById(Long eventId);
    EventResponse getEventByName(String name);

    void updateEvent(Long eventId, EventRequest eventRequest);

    void deleteEvent(Long eventId);

    List<EventResponse> filterOneTimeEventsByCriteria(String name, String description, String address, String organisationName, String date);
}
