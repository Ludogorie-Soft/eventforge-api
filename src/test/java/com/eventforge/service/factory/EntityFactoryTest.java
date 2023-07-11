package com.eventforge.service.factory;

import com.eventforge.constants.Role;
import com.eventforge.dto.request.EventRequest;
import com.eventforge.dto.request.RegistrationRequest;
import com.eventforge.factory.EntityFactory;
import com.eventforge.model.*;
import com.eventforge.service.EventService;
import com.eventforge.service.ImageService;
import com.eventforge.service.OrganisationService;
import com.eventforge.service.UserService;
import com.eventforge.service.Utils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EntityFactoryTest {
    @Mock
    private OrganisationService organisationService;
    @Mock
    private Utils utils;
    @Mock
    private UserService userService;
    @Mock
    private EventService eventService;
    @Mock
    private ImageService imageService;

    @InjectMocks
    private EntityFactory entityFactory;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        entityFactory = new EntityFactory(organisationService, utils, userService, eventService, imageService);
    }

    @Test
    void testCreateEvent() {
        String authHeader = "exampleAuthHeader";
        EventRequest eventRequest = EventRequest.builder()
                .name("Test Event")
                .description("Event description")
                .address("Event address")
                .eventCategories("Category1, Category2")
                .isOnline(true)
                .startsAt(LocalDateTime.of(2023, 1, 1, 10, 0))
                .endsAt(LocalDateTime.of(2023, 1, 1, 12, 0))
                .price(0.0)
                .build();

        User user = User.builder()
                .id(1L)
                .build();

        Organisation organisation = Organisation.builder()
                .user(user)
                .build();

        when(userService.getLoggedUserByToken(authHeader)).thenReturn(user);
        when(organisationService.getOrganisationByUserId(user.getId())).thenReturn(organisation);
        Event result = entityFactory.createEvent(eventRequest, authHeader);

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

        when(userService.getUserByEmail(registrationRequest.getUsername())).thenReturn(null);
        User result = entityFactory.createUser(registrationRequest);

        verify(userService).getUserByEmail(registrationRequest.getUsername());
        assertEquals(newUser.getUsername(), result.getUsername());
        assertEquals(newUser.getPassword(), result.getPassword());
        assertEquals(newUser.getPhoneNumber(), result.getPhoneNumber());
        assertEquals(newUser.getFullName(), result.getFullName());
    }
}
