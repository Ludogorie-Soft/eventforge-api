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
        List<Event> oneTimeEvents = eventService.getAllActiveOneTimeEvents(pageRequest);
        List<Event> pageSlice = getPageSliceForEvents(oneTimeEvents, pageRequest);
        List<CommonEventResponse>oneTimeEventsResponse = pageSlice.stream().map(responseFactory::buildCommonEventResponse).toList();

        return new PageImpl<>(oneTimeEventsResponse, new PageRequestDto().getPageable(pageRequest), oneTimeEvents.size());
    }

    public Page<CommonEventResponse> getAllExpiredOneTimeEventsByPagination(PageRequestDto pageRequest){
        List<Event> oneTimeEvents = eventService.getAllExpiredOneTimeEvents(pageRequest);
        List<Event> pageSlice = getPageSliceForEvents( oneTimeEvents , pageRequest);
        List<CommonEventResponse> oneTimeEventsResponse = pageSlice.stream().map(responseFactory::buildCommonEventResponse).toList();

        return new PageImpl<>(oneTimeEventsResponse , new PageRequestDto().getPageable(pageRequest) , oneTimeEvents.size());
    }

    public Page<CommonEventResponse> getAllActiveRecurrenceEventsByPagination(PageRequestDto pageRequest){
        List<Event> recurrenceEvents = eventService.getAllActiveRecurrenceEvents(pageRequest);
        List<Event> pageSlice = getPageSliceForEvents(recurrenceEvents , pageRequest);
        List<CommonEventResponse> recurrenceEventsResponse = pageSlice.stream().map(responseFactory::buildCommonEventResponse).toList();

        return new PageImpl<>(recurrenceEventsResponse , new PageRequestDto().getPageable(pageRequest) , recurrenceEvents.size());
    }

    public Page<CommonEventResponse> getAllExpiredRecurrenceEventsByPagination(PageRequestDto pageRequest){
        List<Event> recurrenceEvents = eventService.getAllExpiredRecurrenceEvents(pageRequest);
        List<Event> pageSlice = getPageSliceForEvents(recurrenceEvents , pageRequest);
        List<CommonEventResponse> recurrenceEventsResponse = pageSlice.stream().map(responseFactory::buildCommonEventResponse).toList();

        return new PageImpl<>(recurrenceEventsResponse , new PageRequestDto().getPageable(pageRequest) , recurrenceEvents.size());
    }

    public Page<CommonEventResponse> getEventsByCriteriaAndPagination(CriteriaFilterRequest criteriaFilterRequest , PageRequestDto pageRequest){
        List<Event> events = eventService.filterEventsByCriteria(criteriaFilterRequest , pageRequest);
        List<Event> pageSlice = getPageSliceForEvents(events ,pageRequest);
        List<CommonEventResponse> eventsByCriteria = pageSlice.stream().map(responseFactory::buildCommonEventResponse).toList();

        return new PageImpl<>(eventsByCriteria , new PageRequestDto().getPageable(pageRequest) , events.size());
    }
    private List<Event> getPageSliceForEvents(List<Event> events, PageRequestDto pageRequest) {
        PagedListHolder<Event> pagedListHolder = new PagedListHolder<>(events);
        pagedListHolder.setPage(pageRequest.getPageNo());
        pagedListHolder.setPageSize(pageRequest.getPageSize());
        sortPageSliceForEvents(pagedListHolder.getPageList(), pageRequest);
        return pagedListHolder.getPageList();
    }

    private void sortPageSliceForEvents(List<Event> pageSlice, PageRequestDto pageRequest) {
        boolean ascending = pageRequest.getSort().isAscending();
        PropertyComparator.sort(pageSlice, new MutableSortDefinition(pageRequest.getSortByColumn(), true, ascending));
    }
}
