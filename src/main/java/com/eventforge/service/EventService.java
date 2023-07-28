package com.eventforge.service;

import com.eventforge.dto.request.CriteriaFilterRequest;
import com.eventforge.dto.request.EventRequest;
import com.eventforge.dto.request.PageRequestDto;
import com.eventforge.dto.response.CommonEventResponse;
import com.eventforge.exception.DateTimeException;
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

    public List<CommonEventResponse> getThreeUpcomingEvents(){
        LocalDateTime now = LocalDateTime.now();
        return eventRepository.findThreeUpcomingEvents(now).stream().map(responseFactory::buildCommonEventResponse).toList();

    }

    public List<CommonEventResponse> getAllOneTimeEventsByOrganisationId(Long id) {
        return eventRepository.findAllOneTimeEventsByOrganisationId(id).stream().map(responseFactory::buildCommonEventResponse).toList();
    }

    public List<CommonEventResponse> getAllRecurrenceEventsByOrganisationId(Long id) {
        return eventRepository.findAllRecurrenceEventsByOrganisationId(id).stream().map(responseFactory::buildCommonEventResponse).toList();
    }


    public Page<Event> getAllActiveOneTimeEvents(PageRequestDto pageRequest) {
        Pageable pageable= new PageRequestDto().getPageable(pageRequest);
        LocalDateTime dateTime = LocalDate.now().atStartOfDay();
        return eventRepository.findAllActiveOneTimeEvents(dateTime, pageable);
    }

    public Page<Event> getAllActiveRecurrenceEvents(PageRequestDto pageRequest) {
        Pageable pageable = new PageRequestDto().getPageable(pageRequest);
        LocalDateTime dateTime = LocalDate.now().atStartOfDay();
        return eventRepository.findAllActiveRecurrenceEvents(dateTime, pageable);
    }


    public Page<Event> getAllExpiredOneTimeEvents(PageRequestDto pageRequest) {
        Pageable pageable= new PageRequestDto().getPageable(pageRequest);
        LocalDateTime dateTime = LocalDateTime.now();
        return eventRepository.findAllExpiredOneTimeEvents(dateTime , pageable);
    }

    public Page<Event> getAllExpiredRecurrenceEvents(PageRequestDto pageRequest) {
        Pageable pageable= new PageRequestDto().getPageable(pageRequest);

        LocalDateTime dateTime = LocalDateTime.now();
        return eventRepository.findAllExpiredRecurrenceEvents(dateTime, pageable);
    }

    public List<CommonEventResponse> getAllEventsByUserIdForOrganisation(String token) {
        User user = userService.getLoggedUserByToken(token);
        return eventRepository.findAllEventsForOrganisationByUserId(user.getId())
                .stream()
                .map(responseFactory::buildCommonEventResponse)
                .toList();
    }

    public void saveEvent(Event event) {
        eventRepository.save(event);
    }


    public CommonEventResponse getEventDetailWithConditionsById(Long eventId ) {
        Event event = eventRepository.findEventByIdWithCondition(eventId);
        if (event != null) {
            return responseFactory.buildCommonEventResponse(event);
        } else {
            throw new EventRequestException("Търсеното от вас събитие не е намерено.");
        }
    }

    public CommonEventResponse getEventDetailsWithoutConditionsById(Long eventId){
        Optional<Event> event = eventRepository.findById(eventId);
        if(event.isEmpty()){
            throw new EventRequestException("Търсеното от вас събитие не е наремено");
        }
        return responseFactory.buildCommonEventResponse(event.get());
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

    public void deleteEventByIdForAdmin(Long eventId){
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
        event.setEventCategories(eventRequest.getEventCategories());
        event.setMinAge(eventRequest.getMinAge());
        event.setMaxAge(eventRequest.getMaxAge());
        event.setPrice(eventRequest.getPrice());
        event.setIsOnline(eventRequest.getIsOnline());
        event.setIsOneTime(eventRequest.getIsOneTime());
        event.setStartsAt(eventRequest.getStartsAt());
        event.setEndsAt(eventRequest.getEndsAt());
        event.setRecurrenceDetails(eventRequest.getRecurrenceDetails());

        //invoking method to save the event in the database with the new changes
        saveEvent(event);
        log.info("Successful  update for event with id :{}. Invoked by user with email:{}", eventId, user.getUsername());

    }

    public Page<Event> filterEventsByCriteria(CriteriaFilterRequest request , PageRequestDto pageRequest) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Event> query = cb.createQuery(Event.class);
        Root<Event> root = query.from(Event.class);
        List<Predicate> predicates = new ArrayList<>();
        addCategoryPredicate(request, cb, root, predicates);
        addNamePredicate(request, cb, root, predicates);
        addDescriptionPredicate(request, cb, root, predicates);
        addAddressPredicate(request, cb, root, predicates);
        addOnlinePredicate(request, cb, root, predicates);
        addOrganisationNamePredicate(request, cb, root, predicates);
        addAgePredicate(request, cb, root, predicates);
        addDateTimePredicates(request, cb, root, predicates);
        addOneTimePredicate(request, cb, root, predicates);
        addUserPredicates(cb, root, predicates);
        addExpiredPredicate(request, cb, root, predicates);

        query.where(predicates.toArray(new Predicate[0]));
        if(pageRequest.getSort().isAscending()){
            query.orderBy(cb.asc(root.get(pageRequest.getSortByColumn())));
        } else {
            query.orderBy(cb.desc(root.get(pageRequest.getSortByColumn())));
        }

        Pageable pageable = new PageRequestDto().getPageable(pageRequest);

        TypedQuery<Event> typedQuery = entityManager.createQuery(query);

        typedQuery.setFirstResult(pageable.getPageNumber() * pageable.getPageSize());
        typedQuery.setMaxResults(pageable.getPageSize());


        List<Event> resultList = typedQuery.getResultList();

        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
        countQuery.select(cb.count(countQuery.from(Event.class)));
        countQuery.where(predicates.toArray(new Predicate[0]));
        Long totalCount = entityManager.createQuery(countQuery).getSingleResult();

        return new PageImpl<>(resultList,pageable, totalCount);
    }

    public void addCategoryPredicate(CriteriaFilterRequest request, CriteriaBuilder cb, Root<Event> root, List<Predicate> predicates) {
        if (request.getEventCategories() != null) {
            String[] categories = request.getEventCategories().split(",");
            for (String category : categories) {
                predicates.add(cb.like(root.get("eventCategories"), "%" + category.trim() + "%"));
            }
        }
    }

    public void addNamePredicate(CriteriaFilterRequest request, CriteriaBuilder cb, Root<Event> root, List<Predicate> predicates) {
        if (request.getName() != null) {
            predicates.add(cb.like(root.get("name"), "%" + request.getName() + "%"));
        }
    }

    public void addDescriptionPredicate(CriteriaFilterRequest request, CriteriaBuilder cb, Root<Event> root, List<Predicate> predicates) {
        if (request.getDescription() != null) {
            predicates.add(cb.like(root.get("description"), "%" + request.getDescription() + "%"));
        }
    }

    public void addAddressPredicate(CriteriaFilterRequest request, CriteriaBuilder cb, Root<Event> root, List<Predicate> predicates) {
        if (request.getAddress() != null) {
            predicates.add(cb.like(root.get("address"), "%" + request.getAddress() + "%"));
        }
    }

    public void addOnlinePredicate(CriteriaFilterRequest request, CriteriaBuilder cb, Root<Event> root, List<Predicate> predicates) {
        if (request.getIsOnline() != null) {
            predicates.add(cb.equal(root.get("isOnline"), request.getIsOnline()));
        }
    }

    public void addOrganisationNamePredicate(CriteriaFilterRequest request, CriteriaBuilder cb, Root<Event> root, List<Predicate> predicates) {
        if (request.getOrganisationName() != null) {
            predicates.add(cb.like(root.get("organisation").get("name"), "%" + request.getOrganisationName() + "%"));
        }
    }

    public void addAgePredicate(CriteriaFilterRequest request, CriteriaBuilder cb, Root<Event> root, List<Predicate> predicates) {
        if (request.getMinAge() != null && request.getMaxAge() != null) {
            if (request.getMinAge() == 0 && request.getMaxAge() == 0) {
                predicates.add(cb.equal(root.get("minAge"), 0));
                predicates.add(cb.equal(root.get("maxAge"), 0));
            } else if (request.getMinAge() == 0 && request.getMaxAge() > 0) {
                predicates.add(cb.equal(root.get("minAge"), 0));
                predicates.add(cb.lessThanOrEqualTo(root.get("maxAge"), request.getMaxAge()));
            } else if (request.getMaxAge() == 0 && request.getMinAge() > 0) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("minAge"), request.getMinAge()));
            } else {
                if (request.getMinAge() <= request.getMaxAge()) {
                    predicates.add(cb.and(
                            cb.greaterThanOrEqualTo(root.get("minAge"), request.getMinAge()),
                            cb.lessThanOrEqualTo(root.get("maxAge"), request.getMaxAge())
                    ));
                }

            }
        }
    }

    public void addDateTimePredicates(CriteriaFilterRequest request, CriteriaBuilder cb, Root<Event> root, List<Predicate> predicates) {
        if (request.getStartsAt() != null) {
            predicates.add(cb.lessThanOrEqualTo(root.get("startsAt").as(LocalDateTime.class), request.getStartsAt()));
        }
        if (request.getEndsAt() != null) {
            predicates.add(cb.greaterThanOrEqualTo(root.get("endsAt").as(LocalDateTime.class), request.getEndsAt()));
        }
        if (request.getStartsAt() != null && request.getEndsAt() != null && request.getStartsAt().isAfter(request.getEndsAt())) {
            throw new DateTimeException();
        }
    }

    public void addOneTimePredicate(CriteriaFilterRequest request, CriteriaBuilder cb, Root<Event> root, List<Predicate> predicates) {
        if (request.getIsOneTime()) {
            predicates.add(cb.isTrue(root.get("isOneTime")));
        } else {
            predicates.add(cb.isFalse(root.get("isOneTime")));
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
            predicates.add(cb.lessThan(root.get("endsAt"), LocalDateTime.now()));

        } else {
            predicates.add(cb.greaterThan(root.get("endsAt"), LocalDateTime.now()));
        }
    }

}
