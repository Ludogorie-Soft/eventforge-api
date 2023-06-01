package com.eventforge.service.Impl;

import com.eventforge.dto.EventRequest;
import com.eventforge.dto.EventResponse;
import com.eventforge.exception.EventRequestException;
import com.eventforge.model.Event;
import com.eventforge.repository.EventRepository;
import com.eventforge.service.EventService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class EventServiceImpl implements EventService {
    private final EventRepository eventRepository;
    private final ModelMapper mapper;

    @Override
    public List<EventResponse> getAllEvents() {
        return eventRepository.findAll().stream().map(event -> mapper.map(event, EventResponse.class)).toList();
    }

    @Override
    public EventResponse getEventById(UUID eventId) {
        return eventRepository.findById(eventId).map(event ->
                mapper.map(event, EventResponse.class)).orElseThrow(() -> new EventRequestException("Събитие с номер " + eventId + " не е намерено."));
    }

    @Override
    public EventResponse getEventByName(String name) {
        Optional<Event> event = Optional.ofNullable(eventRepository.findByName(name)
                .orElseThrow(() -> new EventRequestException("Събитие с име " + name + " не е намерено!")));

        return mapper.map(event, EventResponse.class);
    }

    @Override
    public EventResponse saveEvent(EventRequest eventRequest) {
        if (eventRepository.findById(eventRequest.getId()).isPresent()) {
            throw new EventRequestException("Има създадено събитие с номер " + eventRequest.getId() + "!");
        }
        Event event = mapper.map(eventRequest, Event.class);
        return mapper.map(eventRepository.save(event), EventResponse.class);
    }

    @Override
    public void updateEvent(UUID eventId, EventRequest eventRequest) {
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new EventRequestException("Събитие с номер " + eventId + " не е намерено!"));

        event.setName(eventRequest.getName());
        event.setDescription(eventRequest.getDescription());
        event.setAddress(eventRequest.getAddress());

        if (!eventRequest.getEventCategories().isEmpty()) {
            event.setEventCategories(eventRequest.getEventCategories());
        }
        event.setOnline(eventRequest.isOnline());
        event.setStartsAt(eventRequest.getStartsAt());
        event.setEndsAt(eventRequest.getEndsAt());
        eventRepository.save(event);
    }

    @Override
    public void deleteEvent(UUID eventId) {
        eventRepository.deleteById(eventId);
    }
}
