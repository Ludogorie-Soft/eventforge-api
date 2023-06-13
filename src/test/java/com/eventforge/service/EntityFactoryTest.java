package com.eventforge.service;

import com.eventforge.constants.Role;
import com.eventforge.dto.request.EventRequest;
import com.eventforge.dto.request.RegistrationRequest;
import com.eventforge.factory.EntityFactory;
import com.eventforge.model.Event;
import com.eventforge.model.Organisation;
import com.eventforge.model.User;
import com.eventforge.service.Impl.EventServiceImpl;
import com.eventforge.service.Impl.ImageServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class EntityFactoryTest {
    @Mock
    private OrganisationService organisationService;

    @Mock
    private Utils utils;

    @Mock
    private UserService userService;

    @Mock
    private  EventServiceImpl eventService;
    @Mock
    private  ImageServiceImpl imageService;

    @InjectMocks
    private EntityFactory entityFactory;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        entityFactory = new EntityFactory(organisationService ,utils ,userService , eventService , imageService);
    }

    @Test
    void testCreateEvent() {
        // Mock data
        String authHeader = "exampleAuthHeader";
        EventRequest eventRequest = EventRequest.builder()
                .name("Test Event")
                .description("Event description")
                .address("Event address")
                .eventCategories("Category1, Category2")
                .isOnline(true)
                .startsAt(LocalDateTime.of(2023, 1, 1, 10, 0))
                .endsAt(LocalDateTime.of(2023, 1, 1, 12, 0))
                .build();

        User user = User.builder()
                .id(1L)
                .build();

        Organisation organisation = Organisation.builder()
                .user(user)
                .build();

        // Mock interactions
        when(userService.getLoggedUserByToken(authHeader)).thenReturn(user);
        when(organisationService.getOrganisationByUserId(user.getId())).thenReturn(organisation);
        // Invoke the method under test
        Event result = entityFactory.createEvent(eventRequest, authHeader);

        // Verify the interactions and assertions
        assertEquals(eventRequest.getName(), result.getName());
        assertEquals(eventRequest.getDescription(), result.getDescription());
        assertEquals(eventRequest.getAddress(), result.getAddress());
        assertEquals(("Category1, Category2"), result.getEventCategories());
        assertEquals(organisation, result.getOrganisation());
        assertTrue(result.getIsOnline());
        assertEquals(LocalDateTime.of(2023, 1, 1, 10, 0), result.getStartsAt());
        assertEquals(LocalDateTime.of(2023, 1, 1, 12, 0), result.getEndsAt());
    }


    @Test
    void testCreateUser() {
        // Mock data
        RegistrationRequest registrationRequest = RegistrationRequest.builder()
                .username("test@example.com")
                .phoneNumber("1234567890")
                .fullName("John Doe")
                .build();

        User newUser = User.builder()
                .username("test@example.com")
                .role(Role.ORGANISATION.toString())
                .phoneNumber("1234567890")
                .fullName("John Doe")
                .isEnabled(false)
                .isNonLocked(true)
                .build();

        // Mock interactions
        when(userService.getUserByEmail(registrationRequest.getUsername())).thenReturn(null);


        // Invoke the method under test
        User result = entityFactory.createUser(registrationRequest);

        // Verify the interactions and assertions
        verify(userService).getUserByEmail(registrationRequest.getUsername());
        assertEquals(newUser.getUsername(), result.getUsername());
        assertEquals(newUser.getPassword(), result.getPassword());
        assertEquals(newUser.getPhoneNumber(), result.getPhoneNumber());
        assertEquals(newUser.getFullName(), result.getFullName());
    }

}
