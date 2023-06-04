package com.eventforge.service.Impl;

import com.eventforge.dto.EventRequest;
import com.eventforge.dto.EventResponse;
import com.eventforge.exception.EventRequestException;
import com.eventforge.model.Event;
import com.eventforge.model.Organisation;
import com.eventforge.model.User;
import com.eventforge.repository.EventRepository;
import com.eventforge.repository.OrganisationRepository;
import com.eventforge.service.EventService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.*;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class EventServiceImpl implements EventService {
    private final EventRepository eventRepository;
    private final OrganisationRepository organisationRepository;
    private final ModelMapper mapper;
    private final EntityManager entityManager;

    @Override
    public List<EventResponse> getAllEvents() {
        return eventRepository.findAll().stream().map(event -> mapper.map(event, EventResponse.class)).toList();
    }

    @Override
    public EventResponse getEventById(UUID eventId) {
        return eventRepository.findById(eventId).map(event -> mapper.map(event, EventResponse.class)).orElseThrow(() -> new EventRequestException("Събитие с номер " + eventId + " не е намерено."));
    }

    @Override
    public EventResponse getEventByName(String name) {
        Event event = eventRepository.findByName(name).orElseThrow(() -> new EventRequestException("Събитие с име " + name + " не е намерено!"));
        return mapper.map(event, EventResponse.class);
    }

    @Override
    public EventResponse saveEvent(EventRequest eventRequest) {
        Event event = mapEventRequestToEvent(eventRequest);
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

    //method for filtering
    @Override
    public List<EventResponse> filterEventsByCriteria(String name, String description, String address, String organisationName, String date) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Event> query = cb.createQuery(Event.class);
        Root<Event> root = query.from(Event.class);
        List<Predicate> predicates = new ArrayList<>();

        if (name != null) {
            predicates.add(cb.like(root.get("name"), "%" + name + "%"));
        }

        if (description != null) {
            predicates.add(cb.like(root.get("description"), "%" + description + "%"));
        }

        if (address != null) {
            predicates.add(cb.like(root.get("address"), "%" + address + "%"));
        }

        if (organisationName != null) {
            predicates.add(cb.like(root.get("organisation").get("name"), "%" + organisationName + "%"));
        }

        if (date != null) {
            predicates.add(cb.equal(root.get("startsAt"), date));
        }

        Join<Event, Organisation> orgJoin = root.join("organisation");
        Join<Organisation, User> userJoin = orgJoin.join("user");
        predicates.add(cb.isTrue(userJoin.get("isNonLocked")));

        query.where(predicates.toArray(new Predicate[0]));

        return entityManager.createQuery(query).getResultList().stream().map(event -> mapper.map(event, EventResponse.class)).toList();
    }

    public Event mapEventRequestToEvent(EventRequest eventRequest) {
        Event event = Event.builder().id(eventRequest.getId()).name(eventRequest.getName()).description(eventRequest.getDescription()).address(eventRequest.getAddress()).eventCategories(eventRequest.getEventCategories()).isOnline(eventRequest.isOnline()).startsAt(eventRequest.getStartsAt()).endsAt(eventRequest.getEndsAt()).build();

        organisationRepository.findById(eventRequest.getOrganisationId()).ifPresent(event::setOrganisation);
        return event;
    }
}
