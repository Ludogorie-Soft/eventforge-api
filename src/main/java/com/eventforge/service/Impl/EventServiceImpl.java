package com.eventforge.service.Impl;

import com.eventforge.dto.request.EventRequest;
import com.eventforge.dto.response.EventResponse;
import com.eventforge.exception.EventRequestException;
import com.eventforge.factory.EntityFactory;
import com.eventforge.factory.ResponseFactory;
import com.eventforge.model.Event;
import com.eventforge.model.Organisation;
import com.eventforge.model.User;
import com.eventforge.repository.EventRepository;
import com.eventforge.repository.OrganisationRepository;
import com.eventforge.service.EventService;
import com.eventforge.service.Utils;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.*;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class EventServiceImpl implements EventService {
    private final EventRepository eventRepository;
    private final OrganisationRepository organisationRepository;
    private final ModelMapper mapper;
    private final EntityManager entityManager;
    private final ResponseFactory responseFactory;
    private final EntityFactory entityFactory;

    private final Utils utils;


    @Override
    public List<EventResponse> getAllEvents() {
        LocalDateTime dateTime = LocalDate.now().atStartOfDay();
        return eventRepository.findAllValidEvents(dateTime)
                .stream()
                .filter(event -> event.getEndsAt().isAfter(dateTime) || event.getEndsAt().isEqual(dateTime))
                .map(event -> responseFactory.buildEventResponse(event, event.getOrganisation().getName()))
                .toList();
    }
    @Override
    public List<EventResponse> getAllPassedEvents() {
        LocalDateTime dateTime = LocalDateTime.now();
        return eventRepository.findAllValidPassedEvents(dateTime)
                .stream()
                .map(event -> responseFactory.buildEventResponse(event, event.getOrganisation().getName()))
                .toList();
    }

    @Override
    public void saveEvent(EventRequest eventRequest, String authHeader) {
        Event event = entityFactory.createEvent(eventRequest, authHeader);
        eventRepository.save(event);
    }

    @Override
    public EventResponse getEventById(Long eventId) {
        return eventRepository.findById(eventId).map(event -> mapper.map(event, EventResponse.class)).orElseThrow(() -> new EventRequestException("Събитие с номер " + eventId + " не е намерено."));
    }

    @Override
    public EventResponse getEventByName(String name) {
        Optional<Event> event = Optional.ofNullable(eventRepository.findByName(name).orElseThrow(() -> new EventRequestException("Събитие с име " + name + " не е намерено!")));

        return mapper.map(event, EventResponse.class);
    }


    @Override
    public void updateEvent(Long eventId, EventRequest eventRequest) {
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new EventRequestException("Събитие с номер " + eventId + " не е намерено!"));
        List<String> eventCategories = utils.splitStringByComma(eventRequest.getEventCategories());
        event.setName(eventRequest.getName());
        event.setDescription(eventRequest.getDescription());
        event.setAddress(eventRequest.getAddress());

        if (!eventRequest.getEventCategories().isEmpty()) {
            event.setEventCategories(eventCategories);
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

    //method for filtering
    @Override
    public List<EventResponse> filterOneTimeEventsByCriteria(String name, String description, String address, String organisationName, String date) {
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
        predicates.add(cb.isTrue(userJoin.get("isApprovedByAdmin")));
        predicates.add(cb.isTrue(userJoin.get("isOneTime")));
        predicates.add(cb.lessThan(root.get("endDate"), LocalDateTime.now()));

        query.where(predicates.toArray(new Predicate[0]));

        return entityManager.createQuery(query).getResultList().stream().map(event -> mapper.map(event, EventResponse.class)).toList();
    }

//    public Event mapEventRequestToEvent(EventRequest eventRequest) {
//        Event event = Event.builder().name(eventRequest.getName()).description(eventRequest.getDescription()).address(eventRequest.getAddress()).eventCategories(eventRequest.getEventCategories()).isOnline(eventRequest.getIsOnline()).startsAt(eventRequest.getStartsAt()).endsAt(eventRequest.getEndsAt()).build();
//
//        organisationRepository.findOrganisationByName(eventRequest.getOrganisationName()).ifPresent(event::setOrganisation);
//        return event;
//    }
}
