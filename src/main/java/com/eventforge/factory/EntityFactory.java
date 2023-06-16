package com.eventforge.factory;

import com.eventforge.constants.ImageType;
import com.eventforge.constants.Role;
import com.eventforge.dto.request.EventRequest;
import com.eventforge.dto.request.RegistrationRequest;
import com.eventforge.exception.EmailAlreadyTakenException;
import com.eventforge.model.Event;
import com.eventforge.model.Organisation;
import com.eventforge.model.OrganisationPriority;
import com.eventforge.model.User;
import com.eventforge.service.Impl.EventServiceImpl;
import com.eventforge.service.Impl.ImageServiceImpl;
import com.eventforge.service.OrganisationService;
import com.eventforge.service.UserService;
import com.eventforge.service.Utils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class EntityFactory {
    private final OrganisationService organisationService;

    private final Utils utils;
    private final UserService userService;

    private final EventServiceImpl eventService;

    private final ImageServiceImpl imageService;


    public Event createEvent(EventRequest eventRequest, String authHeader) {
        User user = userService.getLoggedUserByToken(authHeader);
        Organisation organisation = organisationService.getOrganisationByUserId(user.getId());
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
        eventService.saveEvent(event);
        imageService.uploadImageToFileSystem(eventRequest.getImage(),ImageType.EVENT_PICTURE ,null, event);
        return event;
    }


    public User createOrganisation(RegistrationRequest request) {
        User user = createUser(request);
        Set<OrganisationPriority> organisationPriorities = utils.
                assignOrganisationPrioritiesToOrganisation(request.getOrganisationPriorities(), request.getOptionalCategory());

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
        imageService.uploadImageToFileSystem(request.getLogo(), ImageType.LOGO, org , null);
        imageService.uploadImageToFileSystem(request.getBackgroundCover(),ImageType.COVER , org , null);
        return user;
    }


    public User createUser(RegistrationRequest request) {
        if (userService.getUserByEmail(request.getUsername()) == null) {
            User user = User.builder()
                    .username(request.getUsername())
                    .password(utils.encodePassword(request.getPassword()))
                    .role(Role.ORGANISATION.toString())
                    .phoneNumber(request.getPhoneNumber())
                    .fullName(request.getFullName())
                    .isEnabled(false)
                    .isNonLocked(true)
                    .build();
            userService.saveUserInDb(user);
            return user;
        } else {
            log.warn("Неуспешна регистрация");
            throw new EmailAlreadyTakenException();
        }

    }
}