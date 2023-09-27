package com.eventforge.service;

import com.eventforge.dto.request.CriteriaFilterRequest;
import com.eventforge.dto.request.EventRequest;
import com.eventforge.dto.request.PageRequestDto;
import com.eventforge.dto.response.EventResponse;
import com.eventforge.exception.EventRequestException;
import com.eventforge.factory.ResponseFactory;
import com.eventforge.model.Event;
import com.eventforge.model.Organisation;
import com.eventforge.model.User;
import com.eventforge.repository.EventRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
@Slf4j
public class EventService {
    private final EventRepository eventRepository;
    private final UserService userService;

    private final EntityManager entityManager;
    private final ResponseFactory responseFactory;
    private final ImageService imageService;

    private static final String [] START_END_DATE = {"startsAt" , "endsAt"};

    public List<EventResponse> getThreeUpcomingEvents() {
        LocalDateTime now = LocalDateTime.now();
        return eventRepository.findThreeUpcomingEvents(now).stream().map(responseFactory::buildEventResponse).toList();

    }


    public Page<Event> getAllActiveEvents(PageRequestDto pageRequest) {
        Pageable pageable = new PageRequestDto().getPageable(pageRequest);
        LocalDateTime dateTime = LocalDateTime.now();
        return eventRepository.findAllActiveEvents(dateTime, pageable);
    }

    public Page<Event> getAllActiveAds(PageRequestDto pageRequest) {
        Pageable pageable = new PageRequestDto().getPageable(pageRequest);
        LocalDateTime dateTime = LocalDateTime.now();
        return eventRepository.findAllActiveAds(dateTime, pageable);
    }


    public Page<Event> getAllExpiredEvents(PageRequestDto pageRequest) {
        Pageable pageable = new PageRequestDto().getPageable(pageRequest);
        LocalDateTime dateTime = LocalDateTime.now();
        return eventRepository.findAllExpiredEvents(dateTime, pageable);
    }

    public Page<Event> getAllExpiredAds(PageRequestDto pageRequest) {
        Pageable pageable = new PageRequestDto().getPageable(pageRequest);

        LocalDateTime dateTime = LocalDateTime.now();
        return eventRepository.findAllExpiredAds(dateTime, pageable);
    }

    public List<EventResponse> getAllEventsAndAdsByUserIdForOrganisation(String token) {
        User user = userService.getLoggedUserByToken(token);
        return eventRepository.findAllEventsAndAdsForOrganisationByUserId(user.getId())
                .stream()
                .map(responseFactory::buildEventResponse)
                .toList();
    }

    public void saveEvent(Event event) {
        eventRepository.save(event);
    }


    public EventResponse getEventDetailWithConditionsById(Long eventId) {
        Event event = eventRepository.findEventByIdWithCondition(eventId);
        if (event != null) {
            return responseFactory.buildEventResponse(event);
        } else {
            throw new EventRequestException("Търсеното от вас събитие не е намерено.");
        }
    }

    public EventResponse getEventDetailsWithoutConditionsById(Long eventId) {
        Optional<Event> event = eventRepository.findById(eventId);
        if (event.isEmpty()) {
            throw new EventRequestException("Търсеното от вас събитие не е наремено");
        }
        return responseFactory.buildEventResponse(event.get());
    }

    public void deleteEventByIdAndUserIdForOrganisation(Long eventId, String token) {
        User user = userService.getLoggedUserByToken(token);
        Event eventToDelete = eventRepository.findEventByIdAndUserId(user.getId(), eventId);
        if (eventToDelete != null) {
            eventRepository.delete(eventToDelete);
            log.info("User deleted event with id :" + eventId);
        } else {
            log.info("Unsuccessful attempt for user - {} , to delete event with id :" + eventId, user.getUsername());
            throw new EventRequestException("Търсеното от вас събитие с идентификационен номер:" + eventId + " ,не съществува или не принаджели на вашият акаунт!");
        }

    }

    public void deleteEventByIdForAdmin(Long eventId) {
        eventRepository.deleteById(eventId);
    }


    public void updateEvent(Long eventId, EventRequest eventRequest, String token) {
        User user = userService.getLoggedUserByToken(token);
        Event event = eventRepository.findEventByIdAndUserId(user.getId(), eventId);

        if (event == null) {
            log.info("Unsuccessful attempt for user - {} , to update event with id :" + eventId, user.getUsername());
            throw new EventRequestException("Търсеното от вас събитие с идентификационен номер:" + eventId + " ,не съществува или не принаджели на вашият акаунт!");
        }

        if (eventRequest.getImageUrl() != null) {
            imageService.saveImageToDb(null, null, eventRequest.getImageUrl(), null, event);
        }

        event.setName(eventRequest.getName());
        event.setDescription(eventRequest.getDescription());
        event.setAddress(eventRequest.getAddress());
        event.setFacebookLink(eventRequest.getFacebookLink());
        event.setEventCategories(eventRequest.getEventCategories());
        event.setMinAge(eventRequest.getMinAge());
        event.setMaxAge(eventRequest.getMaxAge());
        event.setPrice(eventRequest.getPrice());
        event.setIsOnline(eventRequest.getIsOnline());
        event.setIsEvent(eventRequest.getIsEvent());
        event.setStartsAt(eventRequest.getStartsAt());
        event.setEndsAt(eventRequest.getEndsAt());
        event.setRecurrenceDetails(eventRequest.getRecurrenceDetails());

        //invoking method to save the event in the database with the new changes
        saveEvent(event);
        log.info("Successful  update for event with id :{}. Invoked by user with email:{}", eventId, user.getUsername());

    }

    public Page<Event> filterEventsByCriteria(CriteriaFilterRequest request, PageRequestDto pageRequest) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Event> query = cb.createQuery(Event.class);
        Root<Event> root = query.from(Event.class);
        List<Predicate> predicates = new ArrayList<>();

        addSearchPredicate(request ,cb , root , predicates);
        addDateTimePredicates(request, cb, root, predicates);
        addIsEventPredicate(request, cb, root, predicates);
        addUserPredicates(cb, root, predicates);
        addExpiredPredicate(request, cb, root, predicates);

        query.where(predicates.toArray(new Predicate[0]));
        if (pageRequest.getSort().isAscending()) {
            query.orderBy(cb.asc(root.get(pageRequest.getSortByColumn())));
        } else {
            query.orderBy(cb.desc(root.get(pageRequest.getSortByColumn())));
        }

        Pageable pageable = new PageRequestDto().getPageable(pageRequest);

        CriteriaQuery<Long> countQuery = entityManager.getCriteriaBuilder().createQuery(Long.class);
        countQuery.select(entityManager.getCriteriaBuilder().count(countQuery.from(Event.class)));

        TypedQuery<Event> typedQuery = entityManager.createQuery(query);
        int totalElements = typedQuery.getResultList().size();
        typedQuery.setFirstResult(pageable.getPageNumber() * pageable.getPageSize());
        typedQuery.setMaxResults(pageable.getPageSize());
        List<Event> resultList = typedQuery.getResultList();


        return new PageImpl<>(resultList, pageable, totalElements);
    }

    public void addSearchPredicate(CriteriaFilterRequest request, CriteriaBuilder cb, Root<Event> root, List<Predicate> predicates) {
        if (request.getValue() != null) {
            String[] keywords = request.getValue().split("[,\\s]+"); // Split the input into keywords
            List<Predicate> keywordPredicates = new ArrayList<>();

            for (String keyword : keywords) {
                String trimmedKeyword = "%" + keyword.trim() + "%";
                Predicate categoryPredicate = cb.like(root.get("eventCategories"), trimmedKeyword);
                Predicate namePredicate = cb.like(root.get("name"), trimmedKeyword);
                Predicate organisationNamePredicate = cb.like(root.get("organisation").get("name"), trimmedKeyword);
                Predicate addressPredicate = cb.like(root.get("address"), trimmedKeyword);

                // Combine the predicates for this keyword with OR
                Predicate keywordPredicate = cb.or(categoryPredicate, namePredicate, organisationNamePredicate, addressPredicate);

                keywordPredicates.add(keywordPredicate);
            }

            // Combine the keyword predicates with an overall OR
            Predicate finalPredicate = cb.or(keywordPredicates.toArray(new Predicate[0]));

            predicates.add(finalPredicate);
        }
    }


    public void addDateTimePredicates(CriteriaFilterRequest request, CriteriaBuilder cb, Root<Event> root, List<Predicate> predicates) {
        if (request.getStartsAt() != null && request.getEndsAt() == null) {
            LocalDate startsAt = request.getStartsAt();
            LocalDateTime startOfDay = startsAt.atTime(LocalTime.MIN);
            LocalDateTime endOfDay = startsAt.atTime(LocalTime.MAX);
            predicates.add(cb.between(root.get(START_END_DATE[0]).as(LocalDateTime.class), startOfDay, endOfDay));
        } else if (request.getStartsAt() == null && request.getEndsAt() != null) {
            LocalDate endsAt = request.getEndsAt();
            LocalDateTime startOfDay = endsAt.atTime(LocalTime.MIN);
            LocalDateTime endOfDay = endsAt.atTime(LocalTime.MAX);
            predicates.add(cb.between(root.get(START_END_DATE[1]).as(LocalDateTime.class), startOfDay, endOfDay));
        } else if (request.getStartsAt() != null && request.getEndsAt() != null) {
            LocalDate startsAt = request.getStartsAt();
            LocalDate endsAt = request.getEndsAt();
            LocalDateTime startOfDay = startsAt.atTime(LocalTime.MIN);
            LocalDateTime endOfDay = endsAt.atTime(LocalTime.MAX);
            predicates.add(cb.between(root.get(START_END_DATE[0]).as(LocalDateTime.class), startOfDay, endOfDay));
            predicates.add(cb.between(root.get(START_END_DATE[1]).as(LocalDateTime.class), startOfDay, endOfDay));

        }
    }

    public void addIsEventPredicate(CriteriaFilterRequest request, CriteriaBuilder cb, Root<Event> root, List<Predicate> predicates) {
        boolean isEvent = request.getIsEvent();
        if (isEvent) {
            predicates.add(cb.isTrue(root.get("isEvent")));
        } else {
            predicates.add(cb.isFalse(root.get("isEvent")));
        }
    }

    private void addUserPredicates(CriteriaBuilder cb, Root<Event> root, List<Predicate> predicates) {
        Join<Event, Organisation> orgJoin = root.join("organisation");
        Join<Organisation, User> userJoin = orgJoin.join("user");
        predicates.add(cb.isTrue(userJoin.get("isNonLocked")));
        predicates.add(cb.isTrue(userJoin.get("isApprovedByAdmin")));
    }

    public void addExpiredPredicate(CriteriaFilterRequest request, CriteriaBuilder cb, Root<Event> root, List<Predicate> predicates) {
        if (request.isSortByExpired()) {
            predicates.add(cb.lessThan(root.get(START_END_DATE[1]), LocalDateTime.now()));

        } else {
            predicates.add(cb.greaterThan(root.get(START_END_DATE[1]), LocalDateTime.now()));
        }
    }

}
