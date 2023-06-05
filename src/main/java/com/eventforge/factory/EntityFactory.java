package com.eventforge.factory;

import com.eventforge.dto.EventRequest;
import com.eventforge.dto.RegistrationRequest;
import com.eventforge.enums.Role;
import com.eventforge.exception.GlobalException;
import com.eventforge.model.Event;
import com.eventforge.model.Organisation;
import com.eventforge.model.OrganisationPriority;
import com.eventforge.model.User;
import com.eventforge.service.OrganisationPriorityService;
import com.eventforge.service.OrganisationService;
import com.eventforge.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class EntityFactory {
    private final OrganisationService organisationService;

    private final UserService userService;

    private final PasswordEncoder passwordEncoder;

    public Event createEvent(EventRequest eventRequest , String authHeader){
        User user = userService.getLoggedUserByToken(authHeader);
        Organisation organisation = organisationService.getOrganisationByUserId(user.getId());
        return Event.builder()
                .name(eventRequest.getName())
                .description(eventRequest.getDescription())
                .address(eventRequest.getAddress())
                .eventCategories(eventRequest.getEventCategories())
                .organisation(organisation)
                .isOnline(eventRequest.getIsOnline())
                .startsAt(eventRequest.getStartsAt())
                .endsAt(eventRequest.getEndsAt())
                .build();

    }


    private final OrganisationPriorityService organisationPriorityService;



    public User createOrganisation(RegistrationRequest request) {
        User user = createUser(request);
        Set<OrganisationPriority>organisationPriorities = assignOrganisationPrioritiesToOrganisation(request.getOrganisationPriorities());

        Organisation org = Organisation.builder()
                .name(request.getName())
                .bullstat(request.getBullstat())
                .user(user)
                .address(request.getAddress())
                .organisationPriorities(organisationPriorities)
                .website(request.getWebsite())
                .facebookLink(request.getFacebookLink())
                .charityOption(request.getCharityOption())
                .organisationPurpose(request.getOrganisationPurpose())
                .build();

        organisationService.saveOrganisationInDb(org);
        return user;
    }



    public User createUser(RegistrationRequest request) {
        User user = userService.getUserByEmail(request.getUsername());
        if (user == null) {
            User user1 = User.builder()
                    .username(request.getUsername())
                    .password(passwordEncoder.encode(request.getPassword()))
                    .role(Role.ORGANISATION.toString())
                    .phoneNumber(request.getPhoneNumber())
                    .fullName(request.getFullName())
                    .isEnabled(false)
                    .isNonLocked(true)
                    .build();
            userService.saveUserInDb(user1);
            return user1;
        } else {
            log.warn("Неуспешна регистрация");
            throw new GlobalException(String.format("Потребител с електронна поща %s вече съществува", request.getUsername()));
        }

    }


    private Set<OrganisationPriority> assignOrganisationPrioritiesToOrganisation(Set<String> priorityCategories) {
        Set<OrganisationPriority> organisationPriorities = new HashSet<>();
        if (priorityCategories != null) {
            for (String category : priorityCategories) {
                OrganisationPriority organisationPriority = organisationPriorityService.getOrganisationPriorityByCategory(category);
                if (organisationPriority != null) {
                    organisationPriorities.add(organisationPriority);
                }
            }
        }
        return organisationPriorities;
    }
}
