package com.eventforge.factory;

import com.eventforge.dto.response.EventResponse;
import com.eventforge.model.Event;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ResponseFactory {

    public EventResponse buildEventResponse(Event event , String organisationName){
        return EventResponse.builder()
                .id(event.getId())
                .name(event.getName())
                .description(event.getDescription())
                .address(event.getAddress())
                .eventCategories(event.getEventCategories())
                .organisationName(organisationName)
                .isOnline(event.getIsOnline())
                .createdAt(event.getCreatedAt())
                .updatedAt(event.getUpdatedAt())
                .startsAt(event.getStartsAt())
                .endsAt(event.getEndsAt())
                .build();
    }
}
