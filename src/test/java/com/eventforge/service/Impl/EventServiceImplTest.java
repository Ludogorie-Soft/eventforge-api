package com.eventforge.service.Impl;

import com.eventforge.dto.request.CriteriaFilterRequest;
import com.eventforge.dto.request.EventRequest;
import com.eventforge.dto.response.OneTimeEventResponse;
import com.eventforge.dto.response.RecurrenceEventResponse;
import com.eventforge.exception.EventRequestException;
import com.eventforge.factory.ResponseFactory;
import com.eventforge.model.Event;
import com.eventforge.model.User;
import com.eventforge.repository.EventRepository;
import com.eventforge.service.UserService;
import com.eventforge.service.Utils;
import jakarta.persistence.criteria.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EventServiceImplTest {
    @Mock
    private EventRepository eventRepository;
    @Mock
    private UserService userService;
    @Mock
    private ModelMapper mapper;
    @Mock
    private ResponseFactory responseFactory;
    @InjectMocks
    private CriteriaFilterRequest request;
    @Mock
    private CriteriaBuilder cb;
    @Mock
    private Root<Event> root;
    @Mock
    private Utils utils;

    @InjectMocks
    private EventServiceImpl eventServiceImpl;

    @Test
    void getAllActiveOneTimeEvents_shouldReturnListOfOneTimeEventResponses() {
        String order = "asc";
        LocalDateTime now = LocalDate.now().atStartOfDay();
        List<Event> oneTimeEvents = List.of(Event.builder().name("event1").build(),
                Event.builder().name("event2").build());

        when(utils.returnOrderByAscendingByDefaultIfParamNotProvided(order)).thenReturn(order);
        when(eventRepository.findAllActiveOneTimeEvents(now, order)).thenReturn(oneTimeEvents);
        var result = eventServiceImpl.getAllActiveOneTimeEvents(order);

        assertThat(result)
                .isNotNull()
                .hasSize(2);

        verify(utils).returnOrderByAscendingByDefaultIfParamNotProvided(order);
        verify(eventRepository).findAllActiveOneTimeEvents(now, order);
        verify(responseFactory, times(2)).buildOneTimeEventResponse(any(Event.class));
    }

    @Test
    void getAllActiveRecurrenceEvents_shouldReturnListOfRecurrenceEventResponses() {
        String order = "asc";
        LocalDateTime now = LocalDate.now().atStartOfDay();
        List<Event> recurrenceEvents = List.of(Event.builder().name("event1").build(),
                Event.builder().name("event2").build());

        when(utils.returnOrderByAscendingByDefaultIfParamNotProvided(order)).thenReturn(order);
        when(eventRepository.findAllActiveRecurrenceEvents(now, order)).thenReturn(recurrenceEvents);

        var result = eventServiceImpl.getAllActiveRecurrenceEvents(order);


        assertThat(result)
                .isNotNull()
                .hasSize(2);
        verify(utils).returnOrderByAscendingByDefaultIfParamNotProvided(order);
        verify(eventRepository).findAllActiveRecurrenceEvents(now, order);
        verify(responseFactory, times(2)).buildRecurrenceEventResponse(any(Event.class));
    }

    @Test
    void getAllExpiredOneTimeEvents_shouldReturnListOfOneTimeEventResponses() {
        String order = "asc";
        LocalDateTime expectedDateTime = LocalDateTime.parse("2023-06-13T11:38:32.947843100");
        List<Event> oneTimeEvents = List.of(
                Event.builder().name("event1").build(),
                Event.builder().name("event2").build()
        );

        when(utils.returnOrderByAscendingByDefaultIfParamNotProvided(anyString())).thenReturn("asc");
        when(eventRepository.findAllExpiredOneTimeEvents(any(LocalDateTime.class), eq(order))).thenReturn(oneTimeEvents);

        List<OneTimeEventResponse> result = eventServiceImpl.getAllExpiredOneTimeEvents(expectedDateTime.toString());

        assertThat(result)
                .isNotNull()
                .hasSize(2);
        verify(responseFactory, times(2)).buildOneTimeEventResponse(any(Event.class));
    }

    @Test
    void getAllExpiredRecurrenceEvents_shouldReturnListOfRecurrenceEventResponses() {
        String order = "asc";
        when(utils.returnOrderByAscendingByDefaultIfParamNotProvided((order))).thenReturn("asc");

        List<RecurrenceEventResponse> actualResponses = eventServiceImpl.getAllExpiredRecurrenceEvents(order);
        assertThat(actualResponses).isEqualTo(new ArrayList<>());
    }

    @Test
    void getAllOneTimeEventsByUserId_shouldReturnListOfOneTimeEventResponses() {
        String token = "valid_token";
        User user = User.builder().id(1L).build();

        when(userService.getLoggedUserByToken((token))).thenReturn(user);
        when(eventRepository.findAllOneTimeEventsByUserId((user.getId()))).thenReturn(new ArrayList<>());

        List<OneTimeEventResponse> actualResponses = eventServiceImpl.getAllOneTimeEventsByUserId(token);
        assertThat(actualResponses).isEqualTo(new ArrayList<>());
    }

    @Test
    void getAllRecurrenceEventsByUserId_shouldReturnListOfRecurrenceEventResponses() {
        String token = "exampleToken";
        User user = User.builder().id(1L).build();

        when(userService.getLoggedUserByToken((token))).thenReturn(user);
        when(eventRepository.findAllRecurrenceEventsByUserId((user.getId()))).thenReturn(new ArrayList<>());

        List<RecurrenceEventResponse> actualResponses = eventServiceImpl.getAllRecurrenceEventsByUserId(token);
        assertThat(actualResponses).isEqualTo(new ArrayList<>());
    }

    @Test
    void saveEvent_shouldSaveEvent() {
        Event event = new Event();
        eventServiceImpl.saveEvent(event);
        verify(eventRepository).save((event));
    }

    @Test
    void getEventById_shouldReturnOneTimeEventResponse() {
        Long eventId = 1L;
        Event event = new Event();
        OneTimeEventResponse expectedResponse = OneTimeEventResponse.builder().build();

        when(eventRepository.findById((eventId))).thenReturn(Optional.of(event));
        when(mapper.map((event), (OneTimeEventResponse.class))).thenReturn(expectedResponse);

        OneTimeEventResponse actualResponse = eventServiceImpl.getEventById(eventId);
        assertThat(actualResponse).isEqualTo(expectedResponse);
    }

    @Test
    void getEventById_eventNotFound_shouldThrowEventRequestException() {
        Long eventId = 123L;
        when(eventRepository.findById(eventId)).thenReturn(Optional.empty());
        assertThrows(EventRequestException.class, () -> eventServiceImpl.getEventById(eventId));
    }

    @Test
    void getOneTimeEventsByNameByUserId_shouldReturnListOfOneTimeEventResponses() {
        String token = "your_token";
        String name = "your_event_name";
        User user = new User();

        when(userService.getLoggedUserByToken((token))).thenReturn(user);
        when(eventRepository.findOneTimeEventsByNameByUserId((user.getId()), (name))).thenReturn(new ArrayList<>());

        List<OneTimeEventResponse> actualResponses = eventServiceImpl.getOneTimeEventsByNameByUserId(token, name);
        assertThat(actualResponses).isEqualTo(new ArrayList<>());
    }

    @Test
    void getRecurrenceEventByNameByUserId_shouldReturnListOfRecurrenceEventResponses() {
        String token = "your_token";
        String name = "your_event_name";
        User user = new User();

        when(userService.getLoggedUserByToken(token)).thenReturn(user);
        when(eventRepository.findRecurrenceEventsByNameByUserId(user.getId(), name)).thenReturn(new ArrayList<>());

        List<RecurrenceEventResponse> actualResponses = eventServiceImpl.getRecurrenceEventByNameByUserId(token, name);
        assertThat(actualResponses).isEqualTo(new ArrayList<>());
    }

    @Test
    void deleteEventById_shouldDeleteEventAndLogInfo() {
        Long eventId = 1L;

        eventServiceImpl.deleteEventById(eventId);
        verify(eventRepository).deleteById(eventId);

        ArgumentCaptor<Long> argumentCaptor = ArgumentCaptor.forClass(Long.class);
        verify(eventRepository).deleteById(argumentCaptor.capture());

        assertEquals(eventId, argumentCaptor.getValue());
    }

    @Test
    void addNamePredicate_shouldAddPredicateWhenNameNotNull() {
        String name = "event";

        request.setName(name);
        List<Predicate> predicates = new ArrayList<>();
        eventServiceImpl.addNamePredicate(request, cb, root, predicates);

        verify(cb).like((root.get("name")), ("%" + name + "%"));
        assertEquals(1, predicates.size());
    }

    @Test
    void addNamePredicate_shouldNotAddPredicateWhenNameNull() {
        List<Predicate> predicates = new ArrayList<>();

        eventServiceImpl.addNamePredicate(request, cb, root, predicates);

        assertEquals(0, predicates.size());
        verifyNoInteractions(cb, root);
    }

    @Test
    void addDescriptionPredicate_shouldAddPredicateWhenDescriptionNotNull() {
        String description = "example";
        request.setDescription(description);
        List<Predicate> predicates = new ArrayList<>();

        eventServiceImpl.addDescriptionPredicate(request, cb, root, predicates);

        verify(cb).like((root.get("description")), ("%" + description + "%"));
        assertEquals(1, predicates.size());
    }

    @Test
    void addDescriptionPredicate_shouldNotAddPredicateWhenDescriptionNull() {
        List<Predicate> predicates = new ArrayList<>();

        eventServiceImpl.addDescriptionPredicate(request, cb, root, predicates);

        assertEquals(0, predicates.size());
        verifyNoInteractions(cb, root);
    }

    @Test
    void addAddressPredicate_shouldAddPredicateWhenAddressNotNull() {
        String address = "123 Main St";
        request.setAddress(address);
        List<Predicate> predicates = new ArrayList<>();

        eventServiceImpl.addAddressPredicate(request, cb, root, predicates);

        assertEquals(1, predicates.size());
        verify(cb).like((root.get("address")), ("%" + address + "%"));
    }

    @Test
    void addAddressPredicate_shouldNotAddPredicateWhenAddressNull() {
        List<Predicate> predicates = new ArrayList<>();

        eventServiceImpl.addAddressPredicate(request, cb, root, predicates);

        assertEquals(0, predicates.size());
        verifyNoInteractions(cb, root);
    }

    @Test
    void addOnlinePredicate_shouldNotAddPredicateWhenIsOnlineNull() {
        List<Predicate> predicates = new ArrayList<>();

        eventServiceImpl.addOnlinePredicate(request, cb, root, predicates);

        assertTrue(predicates.isEmpty());
        verify(cb, never()).equal(any(), any());
    }

    @Test
    void addOnlinePredicate_shouldAddPredicateWhenIsOnlineNotNull() {
        Boolean isOnline = true;
        request.setIsOnline(isOnline);
        List<Predicate> predicates = new ArrayList<>();
        eventServiceImpl.addOnlinePredicate(request, cb, root, predicates);

        assertEquals(1, predicates.size());
        verify(cb).equal((root.get("isOnline")), (isOnline));
    }

    @Test
    void addOrganisationNamePredicate_shouldNotAddPredicateWhenOrganisationNameNull() {
        request.setOrganisationName(null);
        List<Predicate> predicates = new ArrayList<>();

        eventServiceImpl.addOrganisationNamePredicate(request, cb, root, predicates);

        assertTrue(predicates.isEmpty());
        verify(cb, never()).like(any(), (Expression<String>) any());
    }

    @Test
    void addOrganisationNamePredicate_shouldAddPredicateWhenOrganisationNameNotNull() {
        String organisationName = "ABC Organization";
        request.setOrganisationName(organisationName);
        Join organisationJoin = mock(Join.class);
        List<Predicate> predicates = new ArrayList<>();

        when(root.get("organisation")).thenReturn(organisationJoin);
        eventServiceImpl.addOrganisationNamePredicate(request, cb, root, predicates);

        assertEquals(1, predicates.size());
        verify(cb).like((organisationJoin.get("name")), ("%" + organisationName + "%"));
    }

    @Test
    void updateEvent_shouldUpdateEventInRepository() {
        // Arrange
        Long eventId = 1L;
        EventRequest eventRequest = EventRequest.builder().name("Updated Event").description("Updated description").build();

        Event existingEvent = new Event();

        when(eventRepository.findById(eventId)).thenReturn(Optional.of(existingEvent));

        eventServiceImpl.updateEvent(eventId, eventRequest);

        verify(eventRepository).save(existingEvent);
        assertEquals("Updated Event", existingEvent.getName());
        assertEquals("Updated description", existingEvent.getDescription());
    }

    @Test
    void testAddDateTimePredicates_BothStartsAtAndEndsAtNull() {
        request.setStartsAt(null);
        request.setEndsAt(null);

        List<Predicate> predicates = new ArrayList<>();
        eventServiceImpl.addDateTimePredicates(request, cb, root, predicates);

        Assertions.assertTrue(predicates.isEmpty());
    }

    @Test
    void testAddOneTimePredicate_IsOneTimeTrue() {
        request.setIsOneTime(true);
        List<Predicate> predicates = new ArrayList<>();

        eventServiceImpl.addOneTimePredicate(request, cb, root, predicates);
        Assertions.assertEquals(1, predicates.size());
    }

    @Test
    void testAddOneTimePredicate_IsOneTimeFalse() {
        request.setIsOneTime(false);
        List<Predicate> predicates = new ArrayList<>();
        eventServiceImpl.addOneTimePredicate(request, cb, root, predicates);
        Assertions.assertEquals(1, predicates.size());
    }

    @Test
    void testAddExpiredPredicate_SortByExpiredTrue() {
        List<Predicate> predicates = new ArrayList<>();
        request.setSortByExpired(true);

        eventServiceImpl.addExpiredPredicate(request, cb, root, predicates);
        Assertions.assertEquals(1, predicates.size());
    }

    @Test
    void testAddExpiredPredicate_SortByExpiredFalse() {
        List<Predicate> predicates = new ArrayList<>();
        request.setSortByExpired(false);

        eventServiceImpl.addExpiredPredicate(request, cb, root, predicates);
        Assertions.assertEquals(1, predicates.size());
    }

    @Test
    void testAddAgePredicate_MinAgeAndMaxAgeNotNull() {
        List<Predicate> predicates = new ArrayList<>();
        request.setMinAge(18);
        request.setMaxAge(30);

        Predicate minAgePredicate = Mockito.mock(Predicate.class);
        Predicate maxAgePredicate = Mockito.mock(Predicate.class);

        when(cb.greaterThanOrEqualTo(root.get("minAge"), request.getMinAge())).thenReturn(minAgePredicate);
        when(cb.lessThanOrEqualTo(root.get("maxAge"), request.getMaxAge())).thenReturn(maxAgePredicate);

        eventServiceImpl.addAgePredicate(request, cb, root, predicates);
        Assertions.assertEquals(1, predicates.size());
    }

    @Test
    void testAddAgePredicate_MinAgeAndMaxAgeZero() {
        List<Predicate> predicates = new ArrayList<>();
        request.setMinAge(0);
        request.setMaxAge(0);

        Predicate minAgePredicate = Mockito.mock(Predicate.class);
        Predicate maxAgePredicate = Mockito.mock(Predicate.class);

        when(cb.equal(root.get("minAge"), 0)).thenReturn(minAgePredicate);
        when(cb.equal(root.get("maxAge"), 0)).thenReturn(maxAgePredicate);

        eventServiceImpl.addAgePredicate(request, cb, root, predicates);
        Assertions.assertEquals(2, predicates.size());
    }

    @Test
    void testAddAgePredicate_MinAgeZeroAndMaxAgeGreaterThanZero() {
        List<Predicate> predicates = new ArrayList<>();
        request.setMinAge(0);
        request.setMaxAge(30);

        Predicate minAgePredicate = Mockito.mock(Predicate.class);
        Predicate maxAgePredicate = Mockito.mock(Predicate.class);

        when(cb.equal(root.get("minAge"), 0)).thenReturn(minAgePredicate);
        when(cb.lessThanOrEqualTo(root.get("maxAge"), request.getMaxAge())).thenReturn(maxAgePredicate);

        eventServiceImpl.addAgePredicate(request, cb, root, predicates);

        Assertions.assertEquals(2, predicates.size());
    }

    @Test
    void testAddAgePredicate_MinAgeGreaterThanZeroAndMaxAgeZero() {
        List<Predicate> predicates = new ArrayList<>();
        request.setMinAge(18);
        request.setMaxAge(0);

        Predicate minAgePredicate = Mockito.mock(Predicate.class);
        when(cb.greaterThanOrEqualTo(root.get("minAge"), request.getMinAge())).thenReturn(minAgePredicate);

        eventServiceImpl.addAgePredicate(request, cb, root, predicates);
        Assertions.assertEquals(1, predicates.size());
    }
}