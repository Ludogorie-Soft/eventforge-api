package com.eventforge.service.service;

import com.eventforge.dto.request.CriteriaFilterRequest;
import com.eventforge.dto.request.PageRequestDto;
import com.eventforge.dto.response.CommonEventResponse;
import com.eventforge.factory.ResponseFactory;
import com.eventforge.model.Event;
import com.eventforge.service.EventService;
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
    private ResponseFactory responseFactory;

    private PageRequestDto pageRequest;

    private Pageable pageable;

    private CriteriaFilterRequest criteriaFilterRequest;

    private List<Event> mockEvents;

    private Page<Event> events;

    @BeforeEach
    void setUp(){
         this.pageRequest = new PageRequestDto(1, 10, Sort.Direction.DESC, "startsAt");
         this.pageable =new PageRequestDto().getPageable(pageRequest);
         this.criteriaFilterRequest = new CriteriaFilterRequest();
         this.mockEvents = Arrays.asList(new Event(), new Event());
         this.events = new PageImpl<>(mockEvents, Pageable.unpaged(), mockEvents.size());
    }

    @Test
    public void testGetAllActiveOneTimeEventsByPagination() {
        // Mock the behavior of the eventService to return some test events
        Page<Event> oneTimeEvents = this.events;
        when(eventService.getAllActiveOneTimeEvents(pageRequest)).thenReturn(oneTimeEvents);

        // Mock the behavior of the responseFactory to return some test CommonEventResponse objects
        List<CommonEventResponse> oneTimeEventsResponse = Arrays.asList(new CommonEventResponse(), new CommonEventResponse());
        when(responseFactory.buildCommonEventResponse(any())).thenReturn(oneTimeEventsResponse.get(0), oneTimeEventsResponse.get(1));

        // Call the method under test
        Page<CommonEventResponse> result = paginationService.getAllActiveOneTimeEventsByPagination(pageRequest);

        // Verify the interaction and the result
        verify(eventService).getAllActiveOneTimeEvents(pageRequest);
        verify(responseFactory, times(2)).buildCommonEventResponse(any());
        assertEquals(oneTimeEventsResponse, result.getContent());
        assertEquals(12, result.getTotalElements());
    }

    @Test
    public void testGetAllExpiredOneTimeEventsByPagination() {
        // Mock the behavior of the eventService to return some test events
        when(eventService.getAllExpiredOneTimeEvents(pageRequest)).thenReturn(events);

        // Mock the behavior of the responseFactory to return some test CommonEventResponse objects
        List<CommonEventResponse> oneTimeEventsResponse = Arrays.asList(new CommonEventResponse(), new CommonEventResponse());
        when(responseFactory.buildCommonEventResponse(any())).thenReturn(oneTimeEventsResponse.get(0), oneTimeEventsResponse.get(1));

        // Call the method under test
        Page<CommonEventResponse> result = paginationService.getAllExpiredOneTimeEventsByPagination(pageRequest);

        // Verify the interaction and the result
        verify(eventService).getAllExpiredOneTimeEvents(pageRequest);
        verify(responseFactory, times(2)).buildCommonEventResponse(any());
        assertEquals(oneTimeEventsResponse, result.getContent());
        assertEquals(12, result.getTotalElements());
    }

    @Test
    public void testGetAllActiveRecurrenceEventsByPagination() {
        // Mock the behavior of the eventService to return some test events

        when(eventService.getAllActiveRecurrenceEvents(pageRequest)).thenReturn(events);

        // Mock the behavior of the responseFactory to return some test CommonEventResponse objects
        List<CommonEventResponse> oneTimeEventsResponse = Arrays.asList(new CommonEventResponse(), new CommonEventResponse());
        when(responseFactory.buildCommonEventResponse(any())).thenReturn(oneTimeEventsResponse.get(0), oneTimeEventsResponse.get(1));

        // Call the method under test
        Page<CommonEventResponse> result = paginationService.getAllActiveRecurrenceEventsByPagination(pageRequest);

        // Verify the interaction and the result
        verify(eventService).getAllActiveRecurrenceEvents(pageRequest);
        verify(responseFactory, times(2)).buildCommonEventResponse(any());
        assertEquals(oneTimeEventsResponse, result.getContent());
        assertEquals(12, result.getTotalElements());
    }

    @Test
    public void testGetAllExpiredRecurrenceEventsByPagination() {
        // Mock the behavior of the eventService to return some test events

        when(eventService.getAllExpiredRecurrenceEvents(pageRequest)).thenReturn(events);

        // Mock the behavior of the responseFactory to return some test CommonEventResponse objects
        List<CommonEventResponse> oneTimeEventsResponse = Arrays.asList(new CommonEventResponse(), new CommonEventResponse());
        when(responseFactory.buildCommonEventResponse(any())).thenReturn(oneTimeEventsResponse.get(0), oneTimeEventsResponse.get(1));

        // Call the method under test
        Page<CommonEventResponse> result = paginationService.getAllExpiredRecurrenceEventsByPagination(pageRequest);

        // Verify the interaction and the result
        verify(eventService).getAllExpiredRecurrenceEvents(pageRequest);
        verify(responseFactory, times(2)).buildCommonEventResponse(any());
        assertEquals(oneTimeEventsResponse, result.getContent());
        assertEquals(12, result.getTotalElements());
    }

    @Test
    public void testGetEventsByCriteriaAndPagination() {
        // Mock the behavior of the eventService to return some test events

        when(eventService.filterEventsByCriteria(criteriaFilterRequest,pageRequest)).thenReturn(events);

        // Mock the behavior of the responseFactory to return some test CommonEventResponse objects
        List<CommonEventResponse> oneTimeEventsResponse = Arrays.asList(new CommonEventResponse(), new CommonEventResponse());
        when(responseFactory.buildCommonEventResponse(any())).thenReturn(oneTimeEventsResponse.get(0), oneTimeEventsResponse.get(1));

        // Call the method under test
        Page<CommonEventResponse> result = paginationService.getEventsByCriteriaAndPagination(criteriaFilterRequest,pageRequest);

        // Verify the interaction and the result
        verify(eventService).filterEventsByCriteria(criteriaFilterRequest,pageRequest);
        verify(responseFactory, times(2)).buildCommonEventResponse(any());
        assertEquals(oneTimeEventsResponse, result.getContent());
        assertEquals(12, result.getTotalElements());
    }

}

