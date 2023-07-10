package com.eventforge.factory;

import com.eventforge.dto.request.EventRequest;
import com.eventforge.dto.request.UpdateAccountRequest;
import com.eventforge.exception.EventRequestException;
import com.eventforge.model.Event;
import com.eventforge.model.Image;
import com.eventforge.model.Organisation;
import com.eventforge.model.User;
import com.eventforge.repository.EventRepository;
import com.eventforge.repository.ImageRepository;
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


    public UpdateAccountRequest createUpdateAccountRequest(String token) {
        User user = userService.getLoggedUserByToken(token);
        if (user != null) {
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


    public EventRequest createEventRequestForUpdateOperation(Long eventId, String token) {
        User user = userService.getLoggedUserByToken(token);
        Event foundEvent = eventRepository.findEventByIdAndUserId(user.getId(), eventId);
            if(foundEvent==null){
               throw new EventRequestException("Няма намерено събитие с посоченият от вас идентификационен номер: " + eventId);
            } else {
                return EventRequest.builder()
                        .name(foundEvent.getName())
                        .description(foundEvent.getDescription())
                        .isOnline(foundEvent.getIsOnline())
                        .address(foundEvent.getAddress())
                        .eventCategories(foundEvent.getEventCategories())
                        .price(foundEvent.getPrice())
                        .minAge(foundEvent.getMinAge())
                        .imageUrl(foundEvent.getEventImage().getUrl())
                        .maxAge(foundEvent.getMaxAge())
                        .isOneTime(foundEvent.getIsOneTime())
                        .startsAt(foundEvent.getStartsAt())
                        .endsAt(foundEvent.getEndsAt())
                        .recurrenceDetails(foundEvent.getRecurrenceDetails())
                        .build();
            }

    }
}
