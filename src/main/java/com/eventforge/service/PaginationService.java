package com.eventforge.service;

import com.eventforge.dto.request.CriteriaFilterRequest;
import com.eventforge.dto.request.PageRequestDto;
import com.eventforge.dto.response.EventResponse;
import com.eventforge.dto.response.OrganisationResponse;
import com.eventforge.factory.ResponseFactory;
import com.eventforge.model.Event;
import com.eventforge.model.Organisation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PaginationService {

    private final EventService eventService;

    private final ResponseFactory responseFactory;

    private final OrganisationService organisationService;

    public Page<OrganisationResponse> getAllOrganisationsForUnauthorizedUser(PageRequestDto pageRequest , String search){
        Page<Organisation> organisations = organisationService.getAllOrganisationsForUnauthorizedUser(pageRequest,search);
        List<OrganisationResponse> mappedOrganisations =organisations.stream().map(responseFactory::buildOrganisationResponse).toList();

        return new PageImpl<>(mappedOrganisations , new PageRequestDto().getPageable(pageRequest) ,organisations.getTotalElements());
    }



    public Page<EventResponse> getAllActiveEventsByPagination(PageRequestDto pageRequest) {
        Page<Event> oneTimeEvents = eventService.getAllActiveEvents(pageRequest);
        List<EventResponse>oneTimeEventsResponse = oneTimeEvents.stream().map(responseFactory::buildEventResponse).toList();

        return new PageImpl<>(oneTimeEventsResponse, new PageRequestDto().getPageable(pageRequest), oneTimeEvents.getTotalElements());
    }

    public Page<EventResponse> getAllExpiredEventsByPagination(PageRequestDto pageRequest){
        Page<Event> oneTimeEvents = eventService.getAllExpiredEvents(pageRequest);
        List<EventResponse> oneTimeEventsResponse = oneTimeEvents.stream().map(responseFactory::buildEventResponse).toList();

        return new PageImpl<>(oneTimeEventsResponse , new PageRequestDto().getPageable(pageRequest) , oneTimeEvents.getTotalElements());
    }

    public Page<EventResponse> getAllActiveAdsByPagination(PageRequestDto pageRequest){
        Page<Event> recurrenceEvents = eventService.getAllActiveAds(pageRequest);
        List<EventResponse> recurrenceEventsResponse = recurrenceEvents.stream().map(responseFactory::buildEventResponse).toList();

        return new PageImpl<>(recurrenceEventsResponse , new PageRequestDto().getPageable(pageRequest) , recurrenceEvents.getTotalElements());
    }

    public Page<EventResponse> getAllExpiredAdsByPagination(PageRequestDto pageRequest){
        Page<Event> recurrenceEvents = eventService.getAllExpiredAds(pageRequest);
        List<EventResponse> recurrenceEventsResponse = recurrenceEvents.stream().map(responseFactory::buildEventResponse).toList();

        return new PageImpl<>(recurrenceEventsResponse , new PageRequestDto().getPageable(pageRequest) , recurrenceEvents.getTotalElements());
    }

    public Page<EventResponse> getEventsByCriteriaAndPagination(CriteriaFilterRequest criteriaFilterRequest , PageRequestDto pageRequest){
        Page<Event> events = eventService.filterEventsByCriteria(criteriaFilterRequest , pageRequest);
        List<EventResponse> eventsByCriteria = events.stream().map(responseFactory::buildEventResponse).toList();
        long elements = events.getTotalElements();
        return new PageImpl<>(eventsByCriteria , new PageRequestDto().getPageable(pageRequest) ,elements);
    }

}
