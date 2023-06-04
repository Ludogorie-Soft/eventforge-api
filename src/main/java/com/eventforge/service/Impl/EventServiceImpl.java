package com.eventforge.service.Impl;

import com.eventforge.dto.EventRequest;
import com.eventforge.dto.EventResponse;
import com.eventforge.exception.EventRequestException;
import com.eventforge.factory.EntityFactory;
import com.eventforge.factory.ResponseFactory;
import com.eventforge.model.Event;
import com.eventforge.model.User;
import com.eventforge.repository.EventRepository;
import com.eventforge.service.EventService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class EventServiceImpl implements EventService {
    private final EventRepository eventRepository;
    private final ModelMapper mapper;

    private final ResponseFactory responseFactory;

    private final EntityFactory entityFactory;
    @Override
    public List<EventResponse> getAllEvents1(){
        return eventRepository.findAll().stream().map(event -> responseFactory.buildEventResponse(event , event.getOrganisation().getName())).collect(Collectors.toList());
    }

    @Override
    public void createEvent(EventRequest eventRequest) {
        Event event = entityFactory.createEvent(eventRequest);
        eventRepository.save(event);
    }

    @Override
    public List<EventResponse> getAllEvents() {
        return eventRepository.findAll().stream().map(event -> mapper.map(event, EventResponse.class)).toList();
    }

    @Override
    public EventResponse getEventById(Long eventId) {
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
    public void updateEvent(Long eventId, EventRequest eventRequest) {
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new EventRequestException("Събитие с номер " + eventId + " не е намерено!"));

        event.setName(eventRequest.getName());
        event.setDescription(eventRequest.getDescription());
        event.setAddress(eventRequest.getAddress());

        if (!eventRequest.getEventCategories().isEmpty()) {
            event.setEventCategories(eventRequest.getEventCategories());
        }
        event.setIsOnline(eventRequest.getIsOnline());
        event.setStartsAt(eventRequest.getStartsAt());
        event.setEndsAt(eventRequest.getEndsAt());
        eventRepository.save(event);
    }

    @Override
    public void deleteEvent(Long eventId) {
        eventRepository.deleteById(eventId);
    }
}
