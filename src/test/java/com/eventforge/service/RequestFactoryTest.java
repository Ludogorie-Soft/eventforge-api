package com.eventforge.service;


import com.eventforge.dto.request.EventRequest;
import com.eventforge.dto.request.UpdateAccountRequest;
import com.eventforge.exception.EventRequestException;
import com.eventforge.factory.RequestFactory;
import com.eventforge.model.Event;
import com.eventforge.model.Image;
import com.eventforge.model.Organisation;
import com.eventforge.model.User;
import com.eventforge.repository.EventRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RequestFactoryTest {

    @Mock
    private UserService userService;

    @Mock
    private OrganisationService organisationService;

    @Mock
    private Utils utils;

    @Mock
    private OrganisationPriorityService organisationPriorityService;

    @InjectMocks
    private RequestFactory requestFactory;

    @Mock
    private EventRepository eventRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        requestFactory = new RequestFactory(userService, organisationService, utils, organisationPriorityService, eventRepository);
    }

    @Test
    void testCreateUpdateAccountRequestThenReturnUpdateAccountRequest() {
        String token = "exampleToken";
        User user = new User();
        user.setId(1L);
        Organisation organisation = new Organisation();
        organisation.setUser(user);
        Set<String> chosenPriorities = new HashSet<>();
        chosenPriorities.add("Priority1");
        chosenPriorities.add("Priority2");
        Set<String> allPriorities = new HashSet<>();
        allPriorities.add("Priority1");
        allPriorities.add("Priority2");

        when(userService.getLoggedUserByToken(token)).thenReturn(user);
        when(organisationService.getOrganisationByUserId(user.getId())).thenReturn(organisation);
        when(utils.convertListOfOrganisationPrioritiesToString(organisation.getOrganisationPriorities())).thenReturn(chosenPriorities);
        when(organisationPriorityService.getAllPriorityCategories()).thenReturn(allPriorities);

        UpdateAccountRequest expectedRequest = UpdateAccountRequest.builder()
                .name(organisation.getName())
                .bullstat(organisation.getBullstat())
                .chosenPriorities(chosenPriorities)
                .organisationPurpose(organisation.getOrganisationPurpose())
                .address(organisation.getAddress())
                .website(organisation.getWebsite())
                .facebookLink(organisation.getFacebookLink())
                .fullName(user.getFullName())
                .phoneNumber(user.getPhoneNumber())
                .charityOption(organisation.getCharityOption())
                .allPriorities(allPriorities)
                .build();

        UpdateAccountRequest result = requestFactory.createUpdateAccountRequest(token);

        verify(organisationService).getOrganisationByUserId(user.getId());
        verify(utils).convertListOfOrganisationPrioritiesToString(organisation.getOrganisationPriorities());
        verify(organisationPriorityService).getAllPriorityCategories();

        assertEquals(expectedRequest, result);
    }

    @Test
    void testCreateUpdateAccountRequestThenReturnNull() {
        String token = "tokenExample";

        when(userService.getLoggedUserByToken(token)).thenReturn(null);

        UpdateAccountRequest result = requestFactory.createUpdateAccountRequest(token);

        assertNull(result);
        verify(userService, times(1)).getLoggedUserByToken(eq(token));
        verifyNoMoreInteractions(organisationService, utils, organisationPriorityService);
    }
    @Test
    void createEventRequestForUpdateOperation_ShouldReturnEventRequest() {
        // Arrange
        Long eventId = 1L;
        String token = "exampleToken";
        Image eventImage = new Image();
        eventImage.setUrl("picture");
        User user = new User();
        user.setId(1L);
        Event foundEvent = new Event();
        foundEvent.setId(eventId);
        foundEvent.setName("Example Event");
        foundEvent.setDescription("Example Description");
        foundEvent.setIsOnline(true);
        foundEvent.setAddress("Example Address");
        foundEvent.setEventCategories("Category1, Category2");
        foundEvent.setEventImage(eventImage);
        foundEvent.setPrice(10.0);
        foundEvent.setMinAge(18);
        foundEvent.setMaxAge(40);
        foundEvent.setIsOneTime(true);
        foundEvent.setStartsAt(LocalDateTime.now());
        foundEvent.setEndsAt(LocalDateTime.now());
        foundEvent.setRecurrenceDetails("Example Recurrence Details");
        when(userService.getLoggedUserByToken(eq(token))).thenReturn(user);
        when(eventRepository.findEventByIdAndUserId(eq(user.getId()), eq(eventId))).thenReturn(foundEvent);

        // Act
        EventRequest result = requestFactory.createEventRequestForUpdateOperation(eventId, token);

        // Assert
        assertNotNull(result);
        assertEquals("Example Event", result.getName());
        assertEquals("Example Description", result.getDescription());
        assertEquals(true, result.getIsOnline());
        assertEquals("Example Address", result.getAddress());
        assertEquals("Category1, Category2", result.getEventCategories());
        assertEquals(10.0, result.getPrice());
        assertEquals(18, result.getMinAge());
        assertEquals(40, result.getMaxAge());
        assertEquals(true, result.getIsOneTime());
        // Add more assertions for the remaining properties
        verify(userService, times(1)).getLoggedUserByToken(eq(token));
        verify(eventRepository, times(1)).findEventByIdAndUserId(eq(user.getId()), eq(eventId));
    }

    @Test
    void createEventRequestForUpdateOperation_ShouldThrowEventRequestExceptionWhenEventNotFound() {
        // Arrange
        Long eventId = 1L;
        String token = "exampleToken";
        User user = new User();
        user.setId(1L);
        when(userService.getLoggedUserByToken(eq(token))).thenReturn(user);
        when(eventRepository.findEventByIdAndUserId(eq(user.getId()), eq(eventId))).thenReturn(null);

        // Act and Assert
        assertThrows(EventRequestException.class, () -> requestFactory.createEventRequestForUpdateOperation(eventId, token));
        verify(userService, times(1)).getLoggedUserByToken(eq(token));
        verify(eventRepository, times(1)).findEventByIdAndUserId(eq(user.getId()), eq(eventId));
    }
}



