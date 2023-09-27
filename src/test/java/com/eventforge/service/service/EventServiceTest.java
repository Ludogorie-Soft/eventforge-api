package com.eventforge.service.service;

import com.eventforge.dto.request.CriteriaFilterRequest;
import com.eventforge.dto.request.EventRequest;
import com.eventforge.dto.response.EventResponse;
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
import org.mockito.junit.jupiter.MockitoExtension;

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
    void testGetAllEventsByUserIdAndNameForOrganisation_NullOrEmptyName() {
        // Arrange
        String token = "your_token";

        User user = new User(); // Create a user object for testing
        user.setId(1L);

        List<Event> events = Arrays.asList(new Event(), new Event()); // Create a list of events for testing
        List<EventResponse> expectedResponses = Arrays.asList(new EventResponse(), new EventResponse());

        when(userService.getLoggedUserByToken(token)).thenReturn(user);
        when(eventRepository.findAllEventsAndAdsForOrganisationByUserId(user.getId())).thenReturn(events);
        when(responseFactory.buildEventResponse(any(Event.class))).thenReturn(new EventResponse());

        // Act
        List<EventResponse> actualResponses = eventService.getAllEventsAndAdsByUserIdForOrganisation(token);

        // Assert
        assertEquals(expectedResponses.size(), actualResponses.size());
        // Add additional assertions as needed
        verify(userService).getLoggedUserByToken(token);
        verify(eventRepository).findAllEventsAndAdsForOrganisationByUserId(user.getId());
        verify(responseFactory, times(events.size())).buildEventResponse(any(Event.class));
        verifyNoMoreInteractions(eventRepository);
    }


    @Test
    void saveEvent_shouldSaveEvent() {
        Event event = new Event();
        eventService.saveEvent(event);
        verify(eventRepository).save((event));
    }

    @Test
    void testGetEventDetailWithConditionsById_EventFound() {
        Long eventId = 1L;
        Event event = new Event(); // Create a test Event object

        // Mock the behavior of the eventRepository
        when(eventRepository.findEventByIdWithCondition(eventId)).thenReturn(event);

        // Mock the behavior of the responseFactory
        EventResponse expectedResponse = new EventResponse(); // Create a test response object
        when(responseFactory.buildEventResponse(event)).thenReturn(expectedResponse);

        // Call the method being tested
        EventResponse actualResponse = eventService.getEventDetailWithConditionsById(eventId);

        // Verify the expected interactions and outcomes
        Assertions.assertEquals(expectedResponse, actualResponse);
    }

    @Test
    void testGetEventDetailWithConditionsById_EventNotFound() {
        Long eventId = 1L;

        // Mock the behavior of the eventRepository to return null
        when(eventRepository.findEventByIdWithCondition(eventId)).thenReturn(null);

        // Call the method being tested and verify the exception is thrown
        Assertions.assertThrows(EventRequestException.class,
                () -> eventService.getEventDetailWithConditionsById(eventId),
                "Търсеното от вас събитие не е намерено.");
    }

    @Test
    void getEventDetailsWithoutConditionsById_whenFound() {
        Long eventId = 10L;
        Event event = new Event();
        when(eventRepository.findById(eventId)).thenReturn(Optional.of(event));

        EventResponse expectedResponse = new EventResponse(); // Create a test response object
        when(responseFactory.buildEventResponse(event)).thenReturn(expectedResponse);

        // Call the method being tested
        EventResponse actualResponse = eventService.getEventDetailsWithoutConditionsById(eventId);

        // Verify the expected interactions and outcomes
        Assertions.assertEquals(expectedResponse, actualResponse);
    }

    @Test
    void getEventDetailsWithoutConditionsById_whenNotFound() {
        Long eventId = 10L;

        Assertions.assertThrows(EventRequestException.class,
                () -> eventService.getEventDetailsWithoutConditionsById(eventId),
                "Търсеното от вас събитие не е намерено.");
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
        when(eventRepository.findEventByIdAndUserId(user.getId(), eventId)).thenReturn(event);
        eventService.deleteEventByIdAndUserIdForOrganisation(eventId, token);
        verify(eventRepository).delete(event);

        ArgumentCaptor<Event> argumentCaptor = ArgumentCaptor.forClass(Event.class);
        verify(eventRepository).delete(argumentCaptor.capture());

        assertEquals(event, argumentCaptor.getValue());
    }

    @Test
    void testDeleteEventByIdAndUserIdForOrganisation_WhenEventIsNull() {
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
    void testDeleteEventByIdForAdmin() {
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
        EventRequest eventRequest = EventRequest.builder().name("Updated Event").description("Updated description").imageUrl("image").price(10.00).build();

        when(userService.getLoggedUserByToken(token)).thenReturn(user);
        when(eventRepository.findEventByIdAndUserId(user.getId(), eventId)).thenReturn(event);

        eventService.updateEvent(eventId, eventRequest, token);
        verify(imageService).saveImageToDb(null, null, eventRequest.getImageUrl(), null, event);
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
        when(eventRepository.findEventByIdAndUserId(user.getId(), eventId)).thenReturn(null);

        assertThrows(EventRequestException.class, () -> eventService.updateEvent(eventId, eventRequest, token));


    }

    @Test
    void testAddOneTimePredicate_IsOneTimeTrue() {
        request.setIsEvent(true);
        List<Predicate> predicates = new ArrayList<>();

        eventService.addIsEventPredicate(request, cb, root, predicates);
        Assertions.assertEquals(1, predicates.size());
    }

    @Test
    void testAddOneTimePredicate_IsOneTimeFalse() {
        request.setIsEvent(false);
        List<Predicate> predicates = new ArrayList<>();
        eventService.addIsEventPredicate(request, cb, root, predicates);
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

}