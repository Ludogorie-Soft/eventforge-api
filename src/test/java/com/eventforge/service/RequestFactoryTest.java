package com.eventforge.service;


import com.eventforge.dto.request.EventRequest;
import com.eventforge.dto.request.UpdateAccountRequest;
import com.eventforge.factory.RequestFactory;
import com.eventforge.model.Event;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
    }
    @Test
    void testFiundEventForUpdateOperation() {
        Long eventId = 1L;

        when(eventRepository.findById(anyLong())).thenReturn(Optional.empty());

        EventRequest result = requestFactory.createEventRequestForUpdateOperation(eventId);
        verify(eventRepository).findById(eventId);

        Assertions.assertNull(result);
    }
    @Test
    void testCreateEventRequestForUpdateOperation() {
        Long eventId = 1L;
        Event existingEvent = Event.builder()
                .id(eventId)
                .name("Test Event")
                .description("Event description")
                .isOnline(true)
                .address("Event address")
                .eventCategories("Category1, Category2")
                .price(10.0)
                .minAge(18)
                .maxAge(50)
                .isOneTime(false)
                .startsAt(LocalDateTime.of(2023, 1, 1, 10, 0))
                .endsAt(LocalDateTime.of(2023, 1, 1, 12, 0))
                .recurrenceDetails("Recurrence details")
                .build();

        when(eventRepository.findById(anyLong())).thenReturn(Optional.of(existingEvent));

        EventRequest result = requestFactory.createEventRequestForUpdateOperation(eventId);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(existingEvent.getName(), result.getName());
        Assertions.assertEquals(existingEvent.getDescription(), result.getDescription());
        Assertions.assertEquals(existingEvent.getIsOnline(), result.getIsOnline());
        Assertions.assertEquals(existingEvent.getAddress(), result.getAddress());
        Assertions.assertEquals(existingEvent.getEventCategories(), result.getEventCategories());
        Assertions.assertEquals(existingEvent.getPrice(), result.getPrice());
        Assertions.assertEquals(existingEvent.getMinAge(), result.getMinAge());
        Assertions.assertEquals(existingEvent.getMaxAge(), result.getMaxAge());
        Assertions.assertEquals(existingEvent.getIsOneTime(), result.getIsOneTime());
        Assertions.assertEquals(existingEvent.getStartsAt(), result.getStartsAt());
        Assertions.assertEquals(existingEvent.getEndsAt(), result.getEndsAt());
        Assertions.assertEquals(existingEvent.getRecurrenceDetails(), result.getRecurrenceDetails());
    }
}


