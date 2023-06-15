package com.eventforge.factory;

import com.eventforge.dto.response.OneTimeEventResponse;
import com.eventforge.dto.response.OrganisationResponse;
import com.eventforge.dto.response.RecurrenceEventResponse;
import com.eventforge.model.Event;
import com.eventforge.model.Image;
import com.eventforge.model.Organisation;
import com.eventforge.repository.ImageRepository;
import com.eventforge.service.Utils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class ResponseFactory {

    private final Utils utils;

    private final ImageRepository imageRepository;

    public OrganisationResponse buildOrganisationResponse(Organisation org){
        Image logo = imageRepository.findOrganisationLogoByOrgId(org.getId());
        Image background = imageRepository.findOrganisationCoverPictureByOrgId(org.getId());
        Set<String> orgPriorities = utils.convertListOfOrganisationPrioritiesToString(org.getOrganisationPriorities());
        return  OrganisationResponse.builder().
                orgId(org.getId())
                .logo(logo.getUrl())
                .background(background.getUrl())
                .name(org.getName())
                .bullstat(org.getBullstat())
                .username(org.getUser().getUsername())
                .phone(org.getUser().getPhoneNumber())
                .address(org.getAddress())
                .charityOption(org.getCharityOption())
                .organisationPurpose(org.getOrganisationPurpose())
                .organisationPriorities(orgPriorities)
                .registeredAt(org.getRegisteredAt())
                .updatedAt(org.getUpdatedAt())
                .build();
    }

    public OneTimeEventResponse buildOneTimeEventResponse(Event event) {
//        String eventCategories = utils.convertStringListToString(event.getEventCategories());

        return OneTimeEventResponse.builder()
                .id(event.getId())
                .name(event.getName())
                .description(event.getDescription())
                .address(event.getAddress())
                .eventCategories(event.getEventCategories())
                .organisationName(event.getOrganisation().getName())
                .price(utils.convertPriceToString(event.getPrice()))
                .ageBoundary(utils.convertAgeToString(event.getMinAge(), event.getMaxAge()))
                .startsAt(event.getStartsAt())
                .endsAt(event.getEndsAt())
                .build();
    }

    public RecurrenceEventResponse buildRecurrenceEventResponse(Event event) {
        return RecurrenceEventResponse.builder()
                .id(event.getId())
                .name(event.getName())
                .description(event.getDescription())
                .address(event.getAddress())
                .eventCategories(event.getEventCategories())
                .organisationName(event.getOrganisation().getName())
                .price(utils.convertPriceToString(event.getPrice()))
                .ageBoundary(utils.convertAgeToString(event.getMinAge(), event.getMaxAge()))
                .startsAt(event.getStartsAt())
                .endsAt(event.getEndsAt())
                .recurrenceDetails(event.getRecurrenceDetails())
                .build();
    }
}
