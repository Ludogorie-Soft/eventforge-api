package com.eventforge.service.service;

import com.eventforge.dto.request.CriteriaFilterRequest;
import com.eventforge.dto.request.PageRequestDto;
import com.eventforge.dto.response.EventResponse;
import com.eventforge.dto.response.OrganisationResponse;
import com.eventforge.factory.ResponseFactory;
import com.eventforge.model.Event;
import com.eventforge.model.Organisation;
import com.eventforge.service.EventService;
import com.eventforge.service.OrganisationService;
import com.eventforge.service.PaginationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)

public class PaginationServiceTest {
    @InjectMocks
    private PaginationService paginationService;
    @Mock
    private EventService eventService;
    @Mock
    private OrganisationService organisationService;
    @Mock
    private ResponseFactory responseFactory;

    private PageRequestDto pageRequest;

    private Pageable pageable;

    private CriteriaFilterRequest criteriaFilterRequest;

    private List<Organisation> mockOrgs;

    private Page<Organisation> organisations;
    private List<Event> mockEvents;

    private Page<Event> events;

    @BeforeEach
    void setUp(){
         this.pageRequest = new PageRequestDto(1, 10, Sort.Direction.DESC, "startsAt");
         this.pageable =new PageRequestDto().getPageable(pageRequest);
         this.criteriaFilterRequest = new CriteriaFilterRequest();
         this.mockEvents = Arrays.asList(new Event(), new Event());
         this.events = new PageImpl<>(mockEvents, Pageable.unpaged(), mockEvents.size());
         this.mockOrgs = Arrays.asList(new Organisation() , new Organisation());
        this.organisations = new PageImpl<>(mockOrgs, Pageable.unpaged(), mockOrgs.size());

    }

    @Test
    void testGetAllOrganisationsForUnauthorizedUser(){
        Page<Organisation> organisations =this.organisations;
        String search = "searchCriteria";
        when(organisationService.getAllOrganisationsForUnauthorizedUser(pageRequest ,search)).thenReturn(organisations);

        List<OrganisationResponse> orgResponse = Arrays.asList(new OrganisationResponse() ,new OrganisationResponse());
        when(responseFactory.buildOrganisationResponse(any())).thenReturn(orgResponse.get(0), orgResponse.get(1));

        Page<OrganisationResponse> result = paginationService.getAllOrganisationsForUnauthorizedUser(pageRequest , search);

        verify(organisationService).getAllOrganisationsForUnauthorizedUser(pageRequest,search);
        verify(responseFactory, times(2)).buildOrganisationResponse(any());
        assertEquals(orgResponse, result.getContent());
        assertEquals(12, result.getTotalElements());
    }
    @Test
    public void testGetAllActiveOneTimeEventsByPagination() {
        // Mock the behavior of the eventService to return some test events
        Page<Event> oneTimeEvents = this.events;
        when(eventService.getAllActiveEvents(pageRequest)).thenReturn(oneTimeEvents);

        // Mock the behavior of the responseFactory to return some test CommonEventResponse objects
        List<EventResponse> oneTimeEventsResponse = Arrays.asList(new EventResponse(), new EventResponse());
        when(responseFactory.buildEventResponse(any())).thenReturn(oneTimeEventsResponse.get(0), oneTimeEventsResponse.get(1));

        // Call the method under test
        Page<EventResponse> result = paginationService.getAllActiveEventsByPagination(pageRequest);

        // Verify the interaction and the result
        verify(eventService).getAllActiveEvents(pageRequest);
        verify(responseFactory, times(2)).buildEventResponse(any());
        assertEquals(oneTimeEventsResponse, result.getContent());
        assertEquals(12, result.getTotalElements());
    }

    @Test
    public void testGetAllExpiredOneTimeEventsByPagination() {
        // Mock the behavior of the eventService to return some test events
        when(eventService.getAllExpiredEvents(pageRequest)).thenReturn(events);

        // Mock the behavior of the responseFactory to return some test CommonEventResponse objects
        List<EventResponse> oneTimeEventsResponse = Arrays.asList(new EventResponse(), new EventResponse());
        when(responseFactory.buildEventResponse(any())).thenReturn(oneTimeEventsResponse.get(0), oneTimeEventsResponse.get(1));

        // Call the method under test
        Page<EventResponse> result = paginationService.getAllExpiredEventsByPagination(pageRequest);

        // Verify the interaction and the result
        verify(eventService).getAllExpiredEvents(pageRequest);
        verify(responseFactory, times(2)).buildEventResponse(any());
        assertEquals(oneTimeEventsResponse, result.getContent());
        assertEquals(12, result.getTotalElements());
    }

    @Test
    public void testGetAllActiveRecurrenceEventsByPagination() {
        // Mock the behavior of the eventService to return some test events

        when(eventService.getAllActiveAds(pageRequest)).thenReturn(events);

        // Mock the behavior of the responseFactory to return some test CommonEventResponse objects
        List<EventResponse> oneTimeEventsResponse = Arrays.asList(new EventResponse(), new EventResponse());
        when(responseFactory.buildEventResponse(any())).thenReturn(oneTimeEventsResponse.get(0), oneTimeEventsResponse.get(1));

        // Call the method under test
        Page<EventResponse> result = paginationService.getAllActiveAdsByPagination(pageRequest);

        // Verify the interaction and the result
        verify(eventService).getAllActiveAds(pageRequest);
        verify(responseFactory, times(2)).buildEventResponse(any());
        assertEquals(oneTimeEventsResponse, result.getContent());
        assertEquals(12, result.getTotalElements());
    }

    @Test
    public void testGetAllExpiredRecurrenceEventsByPagination() {
        // Mock the behavior of the eventService to return some test events

        when(eventService.getAllExpiredAds(pageRequest)).thenReturn(events);

        // Mock the behavior of the responseFactory to return some test CommonEventResponse objects
        List<EventResponse> oneTimeEventsResponse = Arrays.asList(new EventResponse(), new EventResponse());
        when(responseFactory.buildEventResponse(any())).thenReturn(oneTimeEventsResponse.get(0), oneTimeEventsResponse.get(1));

        // Call the method under test
        Page<EventResponse> result = paginationService.getAllExpiredAdsByPagination(pageRequest);

        // Verify the interaction and the result
        verify(eventService).getAllExpiredAds(pageRequest);
        verify(responseFactory, times(2)).buildEventResponse(any());
        assertEquals(oneTimeEventsResponse, result.getContent());
        assertEquals(12, result.getTotalElements());
    }

    @Test
    public void testGetEventsByCriteriaAndPagination() {
        // Mock the behavior of the eventService to return some test events

        when(eventService.filterEventsByCriteria(criteriaFilterRequest,pageRequest)).thenReturn(events);

        // Mock the behavior of the responseFactory to return some test CommonEventResponse objects
        List<EventResponse> oneTimeEventsResponse = Arrays.asList(new EventResponse(), new EventResponse());
        when(responseFactory.buildEventResponse(any())).thenReturn(oneTimeEventsResponse.get(0), oneTimeEventsResponse.get(1));

        // Call the method under test
        Page<EventResponse> result = paginationService.getEventsByCriteriaAndPagination(criteriaFilterRequest,pageRequest);

        // Verify the interaction and the result
        verify(eventService).filterEventsByCriteria(criteriaFilterRequest,pageRequest);
        verify(responseFactory, times(2)).buildEventResponse(any());
        assertEquals(oneTimeEventsResponse, result.getContent());
        assertEquals(12, result.getTotalElements());
    }

}

