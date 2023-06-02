package com.eventforge.service;

import com.eventforge.dto.EventRequest;
import com.eventforge.dto.EventResponse;

import java.util.List;
import java.util.UUID;

public interface EventService {
    List<EventResponse> getAllEvents();

    EventResponse getEventById(UUID eventId);

    EventResponse getEventByName(String name);

    EventResponse saveEvent(EventRequest eventRequest);

    void updateEvent(UUID eventId, EventRequest eventRequest);

    void deleteEvent(UUID eventId);

    List<EventResponse> filterEventsByCriteria(String name, String description, String address, String organisationName, String date);
}
