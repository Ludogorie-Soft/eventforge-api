package com.eventforge.service.factory;

import com.eventforge.dto.response.CommonEventResponse;
import com.eventforge.dto.response.OrganisationResponse;
import com.eventforge.dto.response.OrganisationResponseForAdmin;
import com.eventforge.factory.ResponseFactory;
import com.eventforge.model.*;
import com.eventforge.repository.EventRepository;
import com.eventforge.repository.ImageRepository;
import com.eventforge.service.Utils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ResponseFactoryTest {
    @Mock
    private Utils mockUtils;

    @Mock
    private ImageRepository imageRepository;

    @Mock
    private EventRepository eventRepository;
    @InjectMocks
    private ResponseFactory responseFactory;


    @Test
    void testBuildOrganisationResponseForAdmin() {
        // Arrange

        Organisation org = new Organisation();
        org.setId(1L);
        org.setName("Organisation");
        User user = new User();
        user.setId(2L);
        user.setFullName("Test Test");
        user.setPhoneNumber("12345");
        user.setUsername("test@example.com");
        user.setIsEnabled(true);
        user.setIsApprovedByAdmin(true);
        user.setIsNonLocked(true);
        user.setRegisteredAt(LocalDateTime.now());
        org.setUser(user);
        List<Event> events = new ArrayList<>();
        events.add(new Event());
        org.setEvents(events);
        // Act
        OrganisationResponseForAdmin response = responseFactory.buildOrganisationResponseForAdmin(org);

        // Assert
        assertEquals(org.getName(), response.getOrgName());
        assertEquals(org.getUser().getFullName(), response.getFullName());
        assertEquals(org.getUser().getPhoneNumber(), response.getPhoneNumber());
        assertEquals(org.getUser().getId(), response.getUserId());
        assertEquals(org.getUser().getUsername(), response.getEmail());
        assertEquals(org.getUser().getIsEnabled(), response.isEnabled());
        assertEquals(org.getUser().getIsApprovedByAdmin(), response.isApprovedByAdmin());
        assertEquals(org.getUser().getIsNonLocked(), response.isNonLocked());
        assertEquals(org.getUser().getRegisteredAt(), response.getRegisteredAt());
    }



    @Test
    void testBuildCommonEventResponse(){
        Organisation org = mock(Organisation.class);
        // Create a mock event object
        Event event = mock(Event.class);
        Image eventPicture = mock(Image.class);
        when(event.getEventImage()).thenReturn(eventPicture);
        when(event.getId()).thenReturn(1L);
        when(eventPicture.getId()).thenReturn(2L);
        when(eventPicture.getUrl()).thenReturn("event-picture-url");
        when(event.getName()).thenReturn("Event Name");
        when(event.getDescription()).thenReturn("Event Description");
        when(event.getAddress()).thenReturn("Event Address");
        when(event.getOrganisation()).thenReturn(org);
        when(org.getName()).thenReturn("organisation");
        when(event.getRecurrenceDetails()).thenReturn("details");
        // Mock other event properties accordingly
        when(mockUtils.convertIsOneTimeToString(event.getIsOneTime())).thenReturn("one-time");
        when(mockUtils.convertPriceToString(event.getPrice())).thenReturn("100 USD");
        when(mockUtils.convertAgeToString(event.getMinAge(), event.getMaxAge())).thenReturn("18+");

        CommonEventResponse eventResponseResult = responseFactory.buildCommonEventResponse(event);

        assertEquals("organisation" ,eventResponseResult.getOrganisationName());
        assertEquals(event.getId(), eventResponseResult.getId());
        assertEquals(eventPicture.getId(), eventResponseResult.getImageId());
        assertEquals(eventPicture.getUrl(), eventResponseResult.getImageUrl());
        assertEquals(event.getName(), eventResponseResult.getName());
        assertEquals(event.getDescription(), eventResponseResult.getDescription());
        assertEquals(event.getAddress(), eventResponseResult.getAddress());


    }
}
