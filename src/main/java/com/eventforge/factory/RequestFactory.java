package com.eventforge.factory;

import com.eventforge.dto.request.EventRequest;
import com.eventforge.dto.request.UpdateAccountRequest;
import com.eventforge.model.Event;
import com.eventforge.model.Organisation;
import com.eventforge.model.User;
import com.eventforge.repository.EventRepository;
import com.eventforge.service.Impl.EventServiceImpl;
import com.eventforge.service.OrganisationPriorityService;
import com.eventforge.service.OrganisationService;
import com.eventforge.service.UserService;
import com.eventforge.service.Utils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class RequestFactory {

    private final UserService userService;

    private final OrganisationService organisationService;

    private final Utils utils;

    private final OrganisationPriorityService organisationPriorityService;

    private final EventRepository eventRepository;


    public UpdateAccountRequest createUpdateAccountRequest(String token){
        User user = userService.getLoggedUserByToken(token);
        if(user != null){
            Organisation organisation = organisationService.getOrganisationByUserId(user.getId());
            Set<String> getChosenOrganisationPriorities = utils.convertListOfOrganisationPrioritiesToString(organisation.getOrganisationPriorities());
            Set<String> allOrganisationPriorities = organisationPriorityService.getAllPriorityCategories();

            return UpdateAccountRequest.builder()
                    .name(organisation.getName())
                    .bullstat(organisation.getBullstat())
                    .chosenPriorities(getChosenOrganisationPriorities)
                    .organisationPurpose(organisation.getOrganisationPurpose())
                    .address(organisation.getAddress())
                    .website(organisation.getWebsite())
                    .facebookLink(organisation.getFacebookLink())
                    .fullName(user.getFullName())
                    .phoneNumber(user.getPhoneNumber())
                    .charityOption(organisation.getCharityOption())
                    .allPriorities(allOrganisationPriorities)
                    .build();
        }
        return null;
    }


    public EventRequest createEventRequestForUpdateOperation(Long id){
        Optional<Event> foundEvent = eventRepository.findById(id);
        if(foundEvent.isPresent()){
            Event eventToUpdate = foundEvent.get();

            return EventRequest.builder()
                    .name(eventToUpdate.getName())
                    .description(eventToUpdate.getDescription())
                    .isOnline(eventToUpdate.getIsOnline())
                    .address(eventToUpdate.getAddress())
                    .eventCategories(eventToUpdate.getEventCategories())
                    .price(eventToUpdate.getPrice())
                    .minAge(eventToUpdate.getMinAge())
                    .maxAge(eventToUpdate.getMaxAge())
                    .isOneTime(eventToUpdate.getIsOneTime())
                    .startsAt(eventToUpdate.getStartsAt())
                    .endsAt(eventToUpdate.getEndsAt())
                    .recurrenceDetails(eventToUpdate.getRecurrenceDetails())
                    .build();
        }
       return null;
    }
}
