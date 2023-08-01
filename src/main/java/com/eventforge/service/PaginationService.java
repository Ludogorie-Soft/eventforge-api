package com.eventforge.service;

import com.eventforge.dto.request.CriteriaFilterRequest;
import com.eventforge.dto.request.PageRequestDto;
import com.eventforge.dto.response.CommonEventResponse;
import com.eventforge.factory.ResponseFactory;
import com.eventforge.model.Event;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.support.MutableSortDefinition;
import org.springframework.beans.support.PagedListHolder;
import org.springframework.beans.support.PropertyComparator;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PaginationService {

    private final EventService eventService;

    private final ResponseFactory responseFactory;



    public Page<CommonEventResponse> getAllActiveOneTimeEventsByPagination(PageRequestDto pageRequest) {
        Page<Event> oneTimeEvents = eventService.getAllActiveOneTimeEvents(pageRequest);
        List<CommonEventResponse>oneTimeEventsResponse = oneTimeEvents.stream().map(responseFactory::buildCommonEventResponse).toList();

        return new PageImpl<>(oneTimeEventsResponse, new PageRequestDto().getPageable(pageRequest), oneTimeEvents.getTotalElements());
    }

    public Page<CommonEventResponse> getAllExpiredOneTimeEventsByPagination(PageRequestDto pageRequest){
        Page<Event> oneTimeEvents = eventService.getAllExpiredOneTimeEvents(pageRequest);
        List<CommonEventResponse> oneTimeEventsResponse = oneTimeEvents.stream().map(responseFactory::buildCommonEventResponse).toList();

        return new PageImpl<>(oneTimeEventsResponse , new PageRequestDto().getPageable(pageRequest) , oneTimeEvents.getTotalElements());
    }

    public Page<CommonEventResponse> getAllActiveRecurrenceEventsByPagination(PageRequestDto pageRequest){
        Page<Event> recurrenceEvents = eventService.getAllActiveRecurrenceEvents(pageRequest);
        List<CommonEventResponse> recurrenceEventsResponse = recurrenceEvents.stream().map(responseFactory::buildCommonEventResponse).toList();

        return new PageImpl<>(recurrenceEventsResponse , new PageRequestDto().getPageable(pageRequest) , recurrenceEvents.getTotalElements());
    }

    public Page<CommonEventResponse> getAllExpiredRecurrenceEventsByPagination(PageRequestDto pageRequest){
        Page<Event> recurrenceEvents = eventService.getAllExpiredRecurrenceEvents(pageRequest);
        List<CommonEventResponse> recurrenceEventsResponse = recurrenceEvents.stream().map(responseFactory::buildCommonEventResponse).toList();

        return new PageImpl<>(recurrenceEventsResponse , new PageRequestDto().getPageable(pageRequest) , recurrenceEvents.getTotalElements());
    }

    public Page<CommonEventResponse> getEventsByCriteriaAndPagination(CriteriaFilterRequest criteriaFilterRequest , PageRequestDto pageRequest){
        Page<Event> events = eventService.filterEventsByCriteria(criteriaFilterRequest , pageRequest);
        List<CommonEventResponse> eventsByCriteria = events.stream().map(responseFactory::buildCommonEventResponse).toList();

        return new PageImpl<>(eventsByCriteria , new PageRequestDto().getPageable(pageRequest) , events.getTotalElements());
    }

}
