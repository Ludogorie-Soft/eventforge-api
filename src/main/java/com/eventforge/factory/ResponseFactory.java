package com.eventforge.factory;

import com.eventforge.dto.response.EventResponse;
import com.eventforge.model.Event;
import com.eventforge.service.Utils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ResponseFactory {

    private final Utils utils;
    public EventResponse buildEventResponse(Event event , String organisationName){
        String eventCategories = utils.convertStringListToString(event.getEventCategories());
        return EventResponse.builder()
                .id(event.getId())
                .name(event.getName())
                .description(event.getDescription())
                .address(event.getAddress())
                .eventCategories(eventCategories)
                .organisationName(organisationName)
                .isOnline(event.getIsOnline())
                .startsAt(event.getStartsAt())
                .endsAt(event.getEndsAt())
                .build();
    }
}
