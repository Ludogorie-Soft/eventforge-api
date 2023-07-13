package com.eventforge.service.Impl;

import com.eventforge.dto.request.CriteriaFilterRequest;
import com.eventforge.dto.request.EventRequest;
import com.eventforge.dto.response.OneTimeEventResponse;
import com.eventforge.dto.response.CommonEventResponse;
import com.eventforge.dto.response.RecurrenceEventResponse;
import com.eventforge.exception.EventRequestException;
import com.eventforge.factory.ResponseFactory;
import com.eventforge.model.Event;
import com.eventforge.model.Organisation;
import com.eventforge.model.User;
import com.eventforge.repository.EventRepository;
import com.eventforge.service.EventService;
import com.eventforge.service.ImageService;
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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EventServiceTest {
    @Mock
    private EventRepository eventRepository;
    @Mock
    private UserService userService;
    @Mock
    private ImageService imageService;

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
    private EventService eventService;

    @Test
    void testGetAllOneTimeEventsByOrganisationId() {
        // Arrange
        Long id = 1L;
        List<Event> events = Arrays.asList(new Event(), new Event()); // Create a list of events for testing
        List<OneTimeEventResponse> expectedResponses = Arrays.asList(new OneTimeEventResponse(), new OneTimeEventResponse());

        when(eventRepository.findAllOneTimeEventsByOrganisationId(id)).thenReturn(events);
        when(responseFactory.buildOneTimeEventResponse(any(Event.class))).thenReturn(new OneTimeEventResponse());

        // Act
        List<OneTimeEventResponse> actualResponses = eventService.getAllOneTimeEventsByOrganisationId(id);

        // Assert
        assertEquals(expectedResponses.size(), actualResponses.size());
        // Add additional assertions as needed
        verify(eventRepository).findAllOneTimeEventsByOrganisationId(id);
        verify(responseFactory, times(events.size())).buildOneTimeEventResponse(any(Event.class));
    }

    @Test
    void testGetAllRecurrenceEventsByOrganisationId() {
        // Arrange
        Long id = 1L;
        List<Event> events = Arrays.asList(new Event(), new Event()); // Create a list of events for testing
        List<RecurrenceEventResponse> expectedResponses = Arrays.asList(new RecurrenceEventResponse(), new RecurrenceEventResponse());

        when(eventRepository.findAllRecurrenceEventsByOrganisationId(id)).thenReturn(events);
        when(responseFactory.buildRecurrenceEventResponse(any(Event.class))).thenReturn(new RecurrenceEventResponse());

        // Act
        List<RecurrenceEventResponse> actualResponses = eventService.getAllRecurrenceEventsByOrganisationId(id);

        // Assert
        assertEquals(expectedResponses.size(), actualResponses.size());
        // Add additional assertions as needed
        verify(eventRepository).findAllRecurrenceEventsByOrganisationId(id);
        verify(responseFactory, times(events.size())).buildRecurrenceEventResponse(any(Event.class));
    }

    @Test
    void getAllActiveOneTimeEvents_shouldReturnListOfOneTimeEventResponses() {
        String order = "asc";
        LocalDateTime now = LocalDate.now().atStartOfDay();
        List<Event> oneTimeEvents = List.of(Event.builder().name("event1").build(),
                Event.builder().name("event2").build());

        when(utils.returnOrderByAscendingByDefaultIfParamNotProvided(order)).thenReturn(order);
        when(eventRepository.findAllActiveOneTimeEvents(now, order)).thenReturn(oneTimeEvents);
        var result = eventService.getAllActiveOneTimeEvents(order);

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

        var result = eventService.getAllActiveRecurrenceEvents(order);


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

        List<OneTimeEventResponse> result = eventService.getAllExpiredOneTimeEvents(expectedDateTime.toString());

        assertThat(result)
                .isNotNull()
                .hasSize(2);
        verify(responseFactory, times(2)).buildOneTimeEventResponse(any(Event.class));
    }

    @Test
    void getAllExpiredRecurrenceEvents_shouldReturnListOfRecurrenceEventResponses() {
        String order = "asc";
        when(utils.returnOrderByAscendingByDefaultIfParamNotProvided((order))).thenReturn("asc");

        List<RecurrenceEventResponse> actualResponses = eventService.getAllExpiredRecurrenceEvents(order);
        assertThat(actualResponses).isEqualTo(new ArrayList<>());
    }

    @Test
    void testGetAllEventsByUserIdAndNameForOrganisation_WithName() {
        // Arrange
        String token = "your_token";
        String name = "Event Name";

        User user = new User(); // Create a user object for testing
        user.setId(1L);

        List<Event> events = Arrays.asList(new Event(), new Event()); // Create a list of events for testing
        List<CommonEventResponse> expectedResponses = Arrays.asList(new CommonEventResponse(), new CommonEventResponse());

        when(userService.getLoggedUserByToken(token)).thenReturn(user);
        when(eventRepository.findAllEventsForOrganisationByUserIdAndName(user.getId(), name)).thenReturn(events);
        when(responseFactory.buildCommonEventResponse(any(Event.class))).thenReturn(new CommonEventResponse());

        // Act
        List<CommonEventResponse> actualResponses = eventService.getAllEventsByUserIdAndNameForOrganisation(token, name);

        // Assert
        assertEquals(expectedResponses.size(), actualResponses.size());
        // Add additional assertions as needed
        verify(userService).getLoggedUserByToken(token);
        verify(eventRepository).findAllEventsForOrganisationByUserIdAndName(user.getId(), name);
        verify(responseFactory, times(events.size())).buildCommonEventResponse(any(Event.class));
    }
    @Test
    void testGetAllEventsByUserIdAndNameForOrganisation_NullOrEmptyName() {
        // Arrange
        String token = "your_token";
        String name = null; // or name = ""

        User user = new User(); // Create a user object for testing
        user.setId(1L);

        List<Event> events = Arrays.asList(new Event(), new Event()); // Create a list of events for testing
        List<CommonEventResponse> expectedResponses = Arrays.asList(new CommonEventResponse(), new CommonEventResponse());

        when(userService.getLoggedUserByToken(token)).thenReturn(user);
        when(eventRepository.findAllEventsForOrganisationByUserId(user.getId())).thenReturn(events);
        when(responseFactory.buildCommonEventResponse(any(Event.class))).thenReturn(new CommonEventResponse());

        // Act
        List<CommonEventResponse> actualResponses = eventService.getAllEventsByUserIdAndNameForOrganisation(token, name);

        // Assert
        assertEquals(expectedResponses.size(), actualResponses.size());
        // Add additional assertions as needed
        verify(userService).getLoggedUserByToken(token);
        verify(eventRepository).findAllEventsForOrganisationByUserId(user.getId());
        verify(responseFactory, times(events.size())).buildCommonEventResponse(any(Event.class));
        verifyNoMoreInteractions(eventRepository);
    }


    @Test
    void saveEvent_shouldSaveEvent() {
        Event event = new Event();
        eventService.saveEvent(event);
        verify(eventRepository).save((event));
    }

    @Test
    public void testGetEventDetailWithConditionsById_EventFound() {
        Long eventId = 1L;
        Event event = new Event(); // Create a test Event object

        // Mock the behavior of the eventRepository
        when(eventRepository.findEventByIdWithCondition(eventId)).thenReturn(event);

        // Mock the behavior of the responseFactory
        CommonEventResponse expectedResponse = new CommonEventResponse(); // Create a test response object
        when(responseFactory.buildCommonEventResponse(event)).thenReturn(expectedResponse);

        // Call the method being tested
        CommonEventResponse actualResponse = eventService.getEventDetailWithConditionsById(eventId);

        // Verify the expected interactions and outcomes
        Assertions.assertEquals(expectedResponse, actualResponse);
    }

    @Test
    public void testGetEventDetailWithConditionsById_EventNotFound() {
        Long eventId = 1L;

        // Mock the behavior of the eventRepository to return null
        when(eventRepository.findEventByIdWithCondition(eventId)).thenReturn(null);

        // Call the method being tested and verify the exception is thrown
        Assertions.assertThrows(EventRequestException.class,
                () -> eventService.getEventDetailWithConditionsById(eventId),
                "Търсеното от вас събитие не е намерено.");
    }

    @Test
    void getEventDetailsWithoutConditionsById_whenFound(){
        Long eventId = 10L;
        Event event = new Event();
        when(eventRepository.findById(eventId)).thenReturn(Optional.of(event));

        CommonEventResponse expectedResponse = new CommonEventResponse(); // Create a test response object
        when(responseFactory.buildCommonEventResponse(event)).thenReturn(expectedResponse);

        // Call the method being tested
        CommonEventResponse actualResponse = eventService.getEventDetailsWithoutConditionsById(eventId);

        // Verify the expected interactions and outcomes
        Assertions.assertEquals(expectedResponse, actualResponse);
    }
    @Test
    void getEventDetailsWithoutConditionsById_whenNotFound(){
        Long eventId = 10L;

        Assertions.assertThrows(EventRequestException.class,
                () -> eventService.getEventDetailsWithoutConditionsById(eventId),
                "Търсеното от вас събитие не е намерено.");
    }
    @Test
    void getOneTimeEventsByNameByUserId_shouldReturnListOfOneTimeEventResponses() {
        String token = "your_token";
        String name = "your_event_name";
        User user = new User();

        when(userService.getLoggedUserByToken((token))).thenReturn(user);
        when(eventRepository.findAllEventsForOrganisationByUserIdAndName((user.getId()), (name))).thenReturn(new ArrayList<>());

        List<CommonEventResponse> actualResponses = eventService.getAllEventsByUserIdAndNameForOrganisation(token, name);
        assertThat(actualResponses).isEqualTo(new ArrayList<>());
    }



    @Test
    void deleteEventById_shouldDeleteEventAndLogInfo() {
        Long eventId = 1L;
        Long userId = 1L;
        String token = "token";
        User user = mock(User.class);
        Organisation org = mock(Organisation.class);
        Event event = mock(Event.class);


        when(userService.getLoggedUserByToken(token)).thenReturn(user);
        when(eventRepository.findEventByIdAndUserId(user.getId() , eventId)).thenReturn(event);
        eventService.deleteEventByIdAndUserIdForOrganisation(eventId , token);
        verify(eventRepository).delete(event);

        ArgumentCaptor<Event> argumentCaptor = ArgumentCaptor.forClass(Event.class);
        verify(eventRepository).delete(argumentCaptor.capture());

        assertEquals(event, argumentCaptor.getValue());
    }

    @Test
    public void testDeleteEventByIdAndUserIdForOrganisation_WhenEventIsNull() {
        Long eventId = 123L;
        String token = "your_token_here";
        User user = new User();
        user.setId(1L);
        when(userService.getLoggedUserByToken(token)).thenReturn(user);
        when(eventRepository.findEventByIdAndUserId(user.getId(), eventId)).thenReturn(null);

        assertThrows(EventRequestException.class, () -> eventService.deleteEventByIdAndUserIdForOrganisation(eventId, token));

        // Verify that the eventRepository.delete() method was not called
        verify(eventRepository, never()).delete(any(Event.class));
    }

    @Test
    public void testDeleteEventByIdForAdmin(){
        Long eventId = 15L;
        eventService.deleteEventByIdForAdmin(eventId);

        verify(eventRepository).deleteById(eventId);
    }

    @Test
    void updateEvent_shouldUpdateEventWhenEventFound() {
        User user = mock(User.class);
        Event event = new Event();

        // Arrange
        Long eventId = 1L;
        String token = "token";
        EventRequest eventRequest = EventRequest.builder().name("Updated Event").description("Updated description").imageUrl("image").build();

        when(userService.getLoggedUserByToken(token)).thenReturn(user);
        when(eventRepository.findEventByIdAndUserId(user.getId() , eventId)).thenReturn(event);

        eventService.updateEvent(eventId, eventRequest , token);
        verify(imageService).saveImageToDb(null , null , eventRequest.getImageUrl() , null , event);
        assertEquals("Updated Event", event.getName());
        assertEquals("Updated description", event.getDescription());
    }
    @Test
    void updateEvent_shouldUpdateEventWhenEventNotFound() {
        User user = mock(User.class);

        // Arrange
        Long eventId = 1L;
        String token = "token";
        EventRequest eventRequest = EventRequest.builder().name("Updated Event").description("Updated description").imageUrl("image").build();

        when(userService.getLoggedUserByToken(token)).thenReturn(user);
        when(eventRepository.findEventByIdAndUserId(user.getId() , eventId)).thenReturn(null);

        assertThrows(EventRequestException.class, () -> eventService.updateEvent(eventId,eventRequest ,token));


    }


    @Test
    void addNamePredicate_shouldAddPredicateWhenNameNotNull() {
        String name = "event";

        request.setName(name);
        List<Predicate> predicates = new ArrayList<>();
        eventService.addNamePredicate(request, cb, root, predicates);

        verify(cb).like((root.get("name")), ("%" + name + "%"));
        assertEquals(1, predicates.size());
    }

    @Test
    void addNamePredicate_shouldNotAddPredicateWhenNameNull() {
        List<Predicate> predicates = new ArrayList<>();

        eventService.addNamePredicate(request, cb, root, predicates);

        assertEquals(0, predicates.size());
        verifyNoInteractions(cb, root);
    }

    @Test
    void addDescriptionPredicate_shouldAddPredicateWhenDescriptionNotNull() {
        String description = "example";
        request.setDescription(description);
        List<Predicate> predicates = new ArrayList<>();

        eventService.addDescriptionPredicate(request, cb, root, predicates);

        verify(cb).like((root.get("description")), ("%" + description + "%"));
        assertEquals(1, predicates.size());
    }

    @Test
    void addDescriptionPredicate_shouldNotAddPredicateWhenDescriptionNull() {
        List<Predicate> predicates = new ArrayList<>();

        eventService.addDescriptionPredicate(request, cb, root, predicates);

        assertEquals(0, predicates.size());
        verifyNoInteractions(cb, root);
    }

    @Test
    void addAddressPredicate_shouldAddPredicateWhenAddressNotNull() {
        String address = "123 Main St";
        request.setAddress(address);
        List<Predicate> predicates = new ArrayList<>();

        eventService.addAddressPredicate(request, cb, root, predicates);

        assertEquals(1, predicates.size());
        verify(cb).like((root.get("address")), ("%" + address + "%"));
    }

    @Test
    void addAddressPredicate_shouldNotAddPredicateWhenAddressNull() {
        List<Predicate> predicates = new ArrayList<>();

        eventService.addAddressPredicate(request, cb, root, predicates);

        assertEquals(0, predicates.size());
        verifyNoInteractions(cb, root);
    }

    @Test
    void addOnlinePredicate_shouldNotAddPredicateWhenIsOnlineNull() {
        List<Predicate> predicates = new ArrayList<>();

        eventService.addOnlinePredicate(request, cb, root, predicates);

        assertTrue(predicates.isEmpty());
        verify(cb, never()).equal(any(), any());
    }

    @Test
    void addOnlinePredicate_shouldAddPredicateWhenIsOnlineNotNull() {
        Boolean isOnline = true;
        request.setIsOnline(isOnline);
        List<Predicate> predicates = new ArrayList<>();
        eventService.addOnlinePredicate(request, cb, root, predicates);

        assertEquals(1, predicates.size());
        verify(cb).equal((root.get("isOnline")), (isOnline));
    }

    @Test
    void addOrganisationNamePredicate_shouldNotAddPredicateWhenOrganisationNameNull() {
        request.setOrganisationName(null);
        List<Predicate> predicates = new ArrayList<>();

        eventService.addOrganisationNamePredicate(request, cb, root, predicates);

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
        eventService.addOrganisationNamePredicate(request, cb, root, predicates);

        assertEquals(1, predicates.size());
        verify(cb).like((organisationJoin.get("name")), ("%" + organisationName + "%"));
    }

    @Test
    void testAddDateTimePredicates_BothStartsAtAndEndsAtNull() {
        request.setStartsAt(null);
        request.setEndsAt(null);

        List<Predicate> predicates = new ArrayList<>();
        eventService.addDateTimePredicates(request, cb, root, predicates);

        Assertions.assertTrue(predicates.isEmpty());
    }

    @Test
    void testAddOneTimePredicate_IsOneTimeTrue() {
        request.setIsOneTime(true);
        List<Predicate> predicates = new ArrayList<>();

        eventService.addOneTimePredicate(request, cb, root, predicates);
        Assertions.assertEquals(1, predicates.size());
    }

    @Test
    void testAddOneTimePredicate_IsOneTimeFalse() {
        request.setIsOneTime(false);
        List<Predicate> predicates = new ArrayList<>();
        eventService.addOneTimePredicate(request, cb, root, predicates);
        Assertions.assertEquals(1, predicates.size());
    }

    @Test
    void testAddExpiredPredicate_SortByExpiredTrue() {
        List<Predicate> predicates = new ArrayList<>();
        request.setSortByExpired(true);

        eventService.addExpiredPredicate(request, cb, root, predicates);
        Assertions.assertEquals(1, predicates.size());
    }

    @Test
    void testAddExpiredPredicate_SortByExpiredFalse() {
        List<Predicate> predicates = new ArrayList<>();
        request.setSortByExpired(false);

        eventService.addExpiredPredicate(request, cb, root, predicates);
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

        eventService.addAgePredicate(request, cb, root, predicates);
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

        eventService.addAgePredicate(request, cb, root, predicates);
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

        eventService.addAgePredicate(request, cb, root, predicates);

        Assertions.assertEquals(2, predicates.size());
    }

    @Test
    void testAddAgePredicate_MinAgeGreaterThanZeroAndMaxAgeZero() {
        List<Predicate> predicates = new ArrayList<>();
        request.setMinAge(18);
        request.setMaxAge(0);

        Predicate minAgePredicate = Mockito.mock(Predicate.class);
        when(cb.greaterThanOrEqualTo(root.get("minAge"), request.getMinAge())).thenReturn(minAgePredicate);

        eventService.addAgePredicate(request, cb, root, predicates);
        Assertions.assertEquals(1, predicates.size());
    }
}