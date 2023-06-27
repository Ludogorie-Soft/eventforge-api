package com.eventforge.service;

import com.eventforge.dto.request.CriteriaFilterRequest;
import com.eventforge.dto.request.EventRequest;
import com.eventforge.dto.response.OneTimeEventResponse;
import com.eventforge.dto.response.RecurrenceEventResponse;
import com.eventforge.model.Event;

import java.util.List;

public interface EventService {
    List<OneTimeEventResponse> getAllActiveOneTimeEvents(String order);
    List<OneTimeEventResponse> getAllExpiredOneTimeEvents(String order);

    List<RecurrenceEventResponse> getAllActiveRecurrenceEvents(String order);

    List<RecurrenceEventResponse> getAllExpiredRecurrenceEvents(String order);

    void saveEvent(Event event);

    OneTimeEventResponse getEventById(Long eventId);
    List<OneTimeEventResponse> getOneTimeEventsByNameByUserId(String token, String name);

    void updateEvent(Long eventId, EventRequest eventRequest);


    List<?> filterEventsByCriteria(CriteriaFilterRequest filterRequest);
}
