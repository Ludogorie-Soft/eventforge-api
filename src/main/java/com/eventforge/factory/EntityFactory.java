package com.eventforge.factory;

import com.eventforge.dto.EventRequest;
import com.eventforge.model.Event;
import com.eventforge.model.Organisation;
import com.eventforge.repository.OrganisationRepository;
import com.eventforge.service.OrganisationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EntityFactory {

    private final OrganisationRepository organisationRepository;

    public Event createEvent(EventRequest eventRequest){
        Organisation organisation = organisationRepository.findById(eventRequest.getOrganisationId()).orElseThrow(()-> new RuntimeException("Няма намерена организация"));
        Event event = Event.builder()
                .name(eventRequest.getName())
                .description(eventRequest.getDescription())
                .address(eventRequest.getAddress())
                .eventCategories(eventRequest.getEventCategories())
                .organisation(organisation)
                .isOnline(eventRequest.getIsOnline())
                .startsAt(eventRequest.getStartsAt())
                .endsAt(eventRequest.getEndsAt())
                .build();
        return event;
    }
}
