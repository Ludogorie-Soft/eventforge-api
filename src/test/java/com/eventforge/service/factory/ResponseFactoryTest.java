//package com.eventforge.service.factory;
//
//import com.eventforge.dto.response.*;
//import com.eventforge.factory.ResponseFactory;
//import com.eventforge.model.*;
//import com.eventforge.repository.EventRepository;
//import com.eventforge.repository.ImageRepository;
//import com.eventforge.service.Utils;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//import java.time.LocalDateTime;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Set;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.mockito.Mockito.mock;
//import static org.mockito.Mockito.when;
//
//@ExtendWith(MockitoExtension.class)
//public class ResponseFactoryTest {
//    @Mock
//    private Utils mockUtils;
//
//    @Mock
//    private ImageRepository imageRepository;
//
//    @Mock
//    private EventRepository eventRepository;
//    @InjectMocks
//    private ResponseFactory responseFactory;
//
//
//    @Test
//    void testBuildOrganisationResponseForAdmin() {
//        // Arrange
//
//        Organisation org = new Organisation();
//        org.setId(1L);
//        org.setName("Organisation");
//        User user = new User();
//        user.setId(2L);
//        user.setFullName("Test Test");
//        user.setPhoneNumber("12345");
//        user.setUsername("test@example.com");
//        user.setIsEnabled(true);
//        user.setIsApprovedByAdmin(true);
//        user.setIsNonLocked(true);
//        user.setRegisteredAt(LocalDateTime.now());
//        org.setUser(user);
//        List<Event> events = new ArrayList<>();
//        events.add(new Event());
//        org.setEvents(events);
//        // Act
//        OrganisationResponseForAdmin response = responseFactory.buildOrganisationResponseForAdmin(org);
//
//        // Assert
//        assertEquals(org.getName(), response.getOrgName());
//        assertEquals(org.getUser().getFullName(), response.getFullName());
//        assertEquals(org.getUser().getPhoneNumber(), response.getPhoneNumber());
//        assertEquals(org.getUser().getId(), response.getUserId());
//        assertEquals(org.getUser().getUsername(), response.getEmail());
//        assertEquals(org.getUser().getIsEnabled(), response.isEnabled());
//        assertEquals(org.getUser().getIsApprovedByAdmin(), response.isApprovedByAdmin());
//        assertEquals(org.getUser().getIsNonLocked(), response.isNonLocked());
//        assertEquals(org.getUser().getRegisteredAt(), response.getRegisteredAt());
//    }
//
////    @Test
////    void testBuildOneTimeEventResponse() {
////
////
////        // Create a mock event object
////        Event event = mock(Event.class);
////        Image eventPicture = mock(Image.class);
////        Organisation organisation = mock(Organisation.class);
////        when(event.getEventImage()).thenReturn(eventPicture);
////        when(eventPicture.getId()).thenReturn(1L);
////        when(eventPicture.getUrl()).thenReturn("event-picture-url");
////        when(event.getId()).thenReturn(2L);
////        when(event.getName()).thenReturn("Event Name");
////        when(event.getDescription()).thenReturn("Event Description");
////        when(event.getAddress()).thenReturn("Event Address");
////        when(event.getOrganisation()).thenReturn(organisation);
////        when(organisation.getName()).thenReturn("organisation");
////        // Mock other event properties accordingly
////
////        when(mockUtils.convertPriceToString(event.getPrice())).thenReturn("100 USD");
////        when(mockUtils.convertAgeToString(event.getMinAge(), event.getMaxAge())).thenReturn("18+");
////        // Act
////        OneTimeEventResponse response = responseFactory.buildOneTimeEventResponse(event);
////
////        // Assert
////        assertEquals("organisation" ,response.getOrganisationName());
////        assertEquals(event.getId(), response.getId());
////        assertEquals(eventPicture.getId(), response.getImageId());
////        assertEquals(eventPicture.getUrl(), response.getImageUrl());
////        assertEquals(event.getName(), response.getName());
////        assertEquals(event.getDescription(), response.getDescription());
////        assertEquals(event.getAddress(), response.getAddress());
////        // Assert other properties accordingly
////    }
//
//    @Test
//    void testBuildOrganisationResponse() {
//
//
//        // Create a mock organisation object
//        Organisation org = mock(Organisation.class);
//        Image logo = mock(Image.class);
//        Image background = mock(Image.class);
//
//        when(org.getId()).thenReturn(1L);
//        when(org.getName()).thenReturn("Organisation Name");
//        when(org.getBullstat()).thenReturn("Bullstat");
//        when(org.getAddress()).thenReturn("Organisation Address");
//        when(org.getCharityOption()).thenReturn("Charity Option");
//        when(org.getOrganisationPurpose()).thenReturn("Organisation Purpose");
//        when(org.getRegisteredAt()).thenReturn(LocalDateTime.now().minusDays(5));
//        when(org.getUpdatedAt()).thenReturn(LocalDateTime.now().minusDays(3));
//
//        when(imageRepository.findOrganisationLogoByOrgId(org.getId())).thenReturn(logo);
//        when(imageRepository.findOrganisationCoverPictureByOrgId(org.getId())).thenReturn(background);
//
//        when(logo.getUrl()).thenReturn("logo-url");
//        when(background.getUrl()).thenReturn("background-url");
//
//        // Mock the behavior of the Utils class
//
//        Set<OrganisationPriority> organisationPriorities = Set.of(
//                new OrganisationPriority("hahaha"),
//                new OrganisationPriority("wtf")
//        );
//        when(org.getOrganisationPriorities()).thenReturn(organisationPriorities);
//
//        when(mockUtils.convertListOfOrganisationPrioritiesToString(org.getOrganisationPriorities()))
//                .thenReturn(Set.of("hahaha", "wtf"));
//
//
//
//
//
//        // Act
//        OrganisationResponse response = responseFactory.buildOrganisationResponse(org);
//
//        // Assert
//        assertEquals(org.getId(), response.getOrgId());
//        assertEquals("logo-url", response.getLogo());
//        assertEquals("background-url", response.getBackground());
//        assertEquals(org.getName(), response.getName());
//        assertEquals(org.getBullstat(), response.getBullstat());
//        assertEquals(org.getAddress(), response.getAddress());
//        assertEquals(org.getCharityOption(), response.getCharityOption());
//        assertEquals(org.getOrganisationPurpose(), response.getOrganisationPurpose());
//        assertEquals(Set.of("hahaha", "wtf"), response.getOrganisationPriorities());
//        assertEquals(org.getRegisteredAt(), response.getRegisteredAt());
//        assertEquals(org.getUpdatedAt(), response.getUpdatedAt());
//    }
//
//    @Test
//    void testBuildRecurrenceEventResponse() {
//
//        Organisation org = mock(Organisation.class);
//        // Create a mock event object
//        Event event = mock(Event.class);
//        Image eventPicture = mock(Image.class);
//        when(event.getEventImage()).thenReturn(eventPicture);
//        when(event.getId()).thenReturn(1L);
//        when(eventPicture.getId()).thenReturn(2L);
//        when(eventPicture.getUrl()).thenReturn("event-picture-url");
//        when(event.getName()).thenReturn("Event Name");
//        when(event.getDescription()).thenReturn("Event Description");
//        when(event.getAddress()).thenReturn("Event Address");
//        when(event.getOrganisation()).thenReturn(org);
//        when(org.getName()).thenReturn("organisation");
//        // Mock other event properties accordingly
//
//
//        when(mockUtils.convertPriceToString(event.getPrice())).thenReturn("100 USD");
//        when(mockUtils.convertAgeToString(event.getMinAge(), event.getMaxAge())).thenReturn("18+");
//
//
//
//        // Act
//        RecurrenceEventResponse response = responseFactory.buildRecurrenceEventResponse(event);
//
//        // Assert
//        assertEquals(event.getId(), response.getId());
//        assertEquals(eventPicture.getId(), response.getImageId());
//        assertEquals(eventPicture.getUrl(), response.getImageUrl());
//        assertEquals(event.getName(), response.getName());
//        assertEquals(event.getDescription(), response.getDescription());
//        assertEquals(event.getAddress(), response.getAddress());
//        assertEquals(event.getEventCategories(), response.getEventCategories());
//        assertEquals(event.getOrganisation().getName(), response.getOrganisationName());
//        assertEquals("100 USD", response.getPrice());
//        assertEquals("18+", response.getAgeBoundary());
//        assertEquals(event.getStartsAt(), response.getStartsAt());
//        assertEquals(event.getEndsAt(), response.getEndsAt());
//        assertEquals(event.getRecurrenceDetails(), response.getRecurrenceDetails());
//        // Assert other properties accordingly
//    }
//
//    @Test
//    void testBuildCommonEventResponse(){
//        Organisation org = mock(Organisation.class);
//        // Create a mock event object
//        Event event = mock(Event.class);
//        Image eventPicture = mock(Image.class);
//        when(event.getEventImage()).thenReturn(eventPicture);
//        when(event.getId()).thenReturn(1L);
//        when(eventPicture.getId()).thenReturn(2L);
//        when(eventPicture.getUrl()).thenReturn("event-picture-url");
//        when(event.getName()).thenReturn("Event Name");
//        when(event.getDescription()).thenReturn("Event Description");
//        when(event.getAddress()).thenReturn("Event Address");
//        when(event.getOrganisation()).thenReturn(org);
//        when(org.getName()).thenReturn("organisation");
//        when(event.getRecurrenceDetails()).thenReturn("details");
//        // Mock other event properties accordingly
//        when(mockUtils.convertIsOneTimeToString(event.getIsOneTime())).thenReturn("one-time");
//        when(mockUtils.convertPriceToString(event.getPrice())).thenReturn("100 USD");
//        when(mockUtils.convertAgeToString(event.getMinAge(), event.getMaxAge())).thenReturn("18+");
//
//        CommonEventResponse eventResponseResult = responseFactory.buildCommonEventResponse(event);
//
//        assertEquals("organisation" ,eventResponseResult.getOrganisationName());
//        assertEquals(event.getId(), eventResponseResult.getId());
//        assertEquals(eventPicture.getId(), eventResponseResult.getImageId());
//        assertEquals(eventPicture.getUrl(), eventResponseResult.getImageUrl());
//        assertEquals(event.getName(), eventResponseResult.getName());
//        assertEquals(event.getDescription(), eventResponseResult.getDescription());
//        assertEquals(event.getAddress(), eventResponseResult.getAddress());
//
//
//    }
//}
