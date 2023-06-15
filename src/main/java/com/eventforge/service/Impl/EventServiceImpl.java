package com.eventforge.service.Impl;

import com.eventforge.dto.request.CriteriaFilterRequest;
import com.eventforge.dto.request.EventRequest;
import com.eventforge.dto.response.OneTimeEventResponse;
import com.eventforge.dto.response.RecurrenceEventResponse;
import com.eventforge.exception.DateTimeException;
import com.eventforge.exception.EventRequestException;
import com.eventforge.factory.ResponseFactory;
import com.eventforge.model.Event;
import com.eventforge.model.Organisation;
import com.eventforge.model.User;
import com.eventforge.repository.EventRepository;
import com.eventforge.service.EventService;
import com.eventforge.service.UserService;
import com.eventforge.service.Utils;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
@Slf4j
public class EventServiceImpl implements EventService {
    private final EventRepository eventRepository;
    private final UserService userService;
    private final ModelMapper mapper;
    private final EntityManager entityManager;
    private final ResponseFactory responseFactory;

    private final Utils utils;

    public Optional<Event> findEventById(Long id){
        return eventRepository.findById(id);
    }


    @Override
    public List<OneTimeEventResponse> getAllActiveOneTimeEvents(String order) {
        String orderBy = utils.returnOrderByAscendingByDefaultIfParamNotProvided(order);
        LocalDateTime dateTime = LocalDate.now().atStartOfDay();
        return eventRepository.findAllActiveOneTimeEvents(dateTime, orderBy)
                .stream()
                .map(responseFactory::buildOneTimeEventResponse)
                .toList();
    }

    public List<RecurrenceEventResponse> getAllActiveRecurrenceEvents(String order) {
        String orderBy = utils.returnOrderByAscendingByDefaultIfParamNotProvided(order);
        LocalDateTime dateTime = LocalDate.now().atStartOfDay();
        return eventRepository.findAllActiveRecurrenceEvents(dateTime, orderBy)
                .stream()
                .map(responseFactory::buildRecurrenceEventResponse)
                .toList();
    }

    @Override
    public List<OneTimeEventResponse> getAllExpiredOneTimeEvents(String order) {
        String orderBy = utils.returnOrderByAscendingByDefaultIfParamNotProvided(order);
        LocalDateTime dateTime = LocalDateTime.now();
        return eventRepository.findAllExpiredOneTimeEvents(dateTime, orderBy)
                .stream()
                .map(responseFactory::buildOneTimeEventResponse)
                .toList();
    }

    public List<RecurrenceEventResponse> getAllExpiredRecurrenceEvents(String order) {
        String orderBy = utils.returnOrderByAscendingByDefaultIfParamNotProvided(order);
        LocalDateTime dateTime = LocalDateTime.now();
        return eventRepository.findAllExpiredRecurrenceEvents(dateTime, orderBy)
                .stream()
                .map(responseFactory::buildRecurrenceEventResponse)
                .toList();
    }

    public List<OneTimeEventResponse> getAllOneTimeEventsByUserId(String token) {
        User user = userService.getLoggedUserByToken(token);
        return eventRepository.findAllOneTimeEventsByUserId(user.getId())
                .stream()
                .map(responseFactory::buildOneTimeEventResponse)
                .toList();
    }

    public List<RecurrenceEventResponse> getAllRecurrenceEventsByUserId(String token) {
        User user = userService.getLoggedUserByToken(token);
        return eventRepository.findAllRecurrenceEventsByUserId(user.getId())
                .stream()
                .map(responseFactory::buildRecurrenceEventResponse)
                .toList();
    }

    @Override
    public void saveEvent(Event event) {
        eventRepository.save(event);
    }

    @Override
    public OneTimeEventResponse getEventById(Long eventId) {
        return eventRepository.findById(eventId).map(event -> mapper.map(event, OneTimeEventResponse.class)).orElseThrow(() -> new EventRequestException("Събитие с номер " + eventId + " не е намерено."));
    }

    @Override
    public List<OneTimeEventResponse> getOneTimeEventsByNameByUserId(String token, String name) {
        User user = userService.getLoggedUserByToken(token);

        return eventRepository.findOneTimeEventsByNameByUserId(user.getId(), name)
                .stream()
                .map(responseFactory::buildOneTimeEventResponse)
                .toList();
    }

    public List<RecurrenceEventResponse> getRecurrenceEventByNameByUserId(String token, String name) {
        User user = userService.getLoggedUserByToken(token);
        return eventRepository.findRecurrenceEventsByNameByUserId(user.getId(), name)
                .stream()
                .map(responseFactory::buildRecurrenceEventResponse)
                .toList();
    }

    public void deleteEventById(Long eventId) {
        eventRepository.deleteById(eventId);
        log.info("User deleted event with id :" + eventId);

    }


    @Override
    public void updateEvent(Long eventId, EventRequest eventRequest) {
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new EventRequestException("Събитие с номер " + eventId + " не е намерено!"));
//        List<String> eventCategories = utils.splitStringByComma(eventRequest.getEventCategories());
        event.setName(eventRequest.getName());
        event.setDescription(eventRequest.getDescription());
        event.setAddress(eventRequest.getAddress());
        event.setEventCategories(eventRequest.getEventCategories());
        event.setMinAge(eventRequest.getMinAge());
        event.setMaxAge(eventRequest.getMaxAge());
        event.setIsOnline(eventRequest.getIsOnline());
        event.setIsOneTime(eventRequest.getIsOneTime());
        event.setStartsAt(eventRequest.getStartsAt());
        event.setEndsAt(eventRequest.getEndsAt());
        event.setRecurrenceDetails(eventRequest.getRecurrenceDetails());
        eventRepository.save(event);
    }

    @Override
    public void deleteEvent(Long eventId) {
        eventRepository.deleteById(eventId);
    }

    public List<?> filterEventsByCriteria(CriteriaFilterRequest request) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Event> query = cb.createQuery(Event.class);
        Root<Event> root = query.from(Event.class);
        List<Predicate> predicates = new ArrayList<>();

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

        List<?> resultList = entityManager.createQuery(query)
                .getResultList();
        if (request.getIsOneTime()) {
            return resultList.stream()
                    .map(event -> responseFactory.buildOneTimeEventResponse((Event) event))
                    .toList();
        } else {
            return resultList.stream()
                    .map(event -> responseFactory.buildRecurrenceEventResponse((Event) event))
                    .toList();
        }
    }

    private void addNamePredicate(CriteriaFilterRequest request, CriteriaBuilder cb, Root<Event> root, List<Predicate> predicates) {
        if (request.getName() != null) {
            predicates.add(cb.like(root.get("name"), "%" + request.getName() + "%"));
        }
    }

    private void addDescriptionPredicate(CriteriaFilterRequest request, CriteriaBuilder cb, Root<Event> root, List<Predicate> predicates) {
        if (request.getDescription() != null) {
            predicates.add(cb.like(root.get("description"), "%" + request.getDescription() + "%"));
        }
    }

    private void addAddressPredicate(CriteriaFilterRequest request, CriteriaBuilder cb, Root<Event> root, List<Predicate> predicates) {
        if (request.getAddress() != null) {
            predicates.add(cb.like(root.get("address"), "%" + request.getAddress() + "%"));
        }
    }

    private void addOnlinePredicate(CriteriaFilterRequest request, CriteriaBuilder cb, Root<Event> root, List<Predicate> predicates) {
        if (request.getIsOnline() != null) {
            predicates.add(cb.equal(root.get("isOnline"), request.getIsOnline()));
        }
    }

    private void addOrganisationNamePredicate(CriteriaFilterRequest request, CriteriaBuilder cb, Root<Event> root, List<Predicate> predicates) {
        if (request.getOrganisationName() != null) {
            predicates.add(cb.like(root.get("organisation").get("name"), "%" + request.getOrganisationName() + "%"));
        }
    }

    private void addAgePredicate(CriteriaFilterRequest request, CriteriaBuilder cb, Root<Event> root, List<Predicate> predicates) {
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

    private void addDateTimePredicates(CriteriaFilterRequest request, CriteriaBuilder cb, Root<Event> root, List<Predicate> predicates) {
        if (request.getStartsAt() != null) {
            predicates.add(cb.lessThanOrEqualTo(root.get("startsAt").as(LocalDateTime.class), request.getStartsAt()));
        }
        if (request.getEndsAt() != null) {
            predicates.add(cb.greaterThanOrEqualTo(root.get("endsAt").as(LocalDateTime.class), request.getEndsAt()));
        }
        if(request.getStartsAt()!= null && request.getEndsAt()!=null && request.getStartsAt().isAfter(request.getEndsAt())){
            throw new DateTimeException();
        }
    }

    private void addOneTimePredicate(CriteriaFilterRequest request, CriteriaBuilder cb, Root<Event> root, List<Predicate> predicates) {
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

    private void addExpiredPredicate(CriteriaFilterRequest request, CriteriaBuilder cb, Root<Event> root, List<Predicate> predicates) {
        if (request.isSortByExpired()) {
            predicates.add(cb.lessThan(root.get("endsAt"), LocalDateTime.now()));

        } else {
            predicates.add(cb.greaterThan(root.get("endsAt"), LocalDateTime.now()));
        }
    }

}
