package com.eventforge.factory;

import com.eventforge.dto.response.OneTimeEventResponse;
import com.eventforge.dto.response.OrganisationResponse;
import com.eventforge.dto.response.OrganisationResponseForAdmin;
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

    public OrganisationResponseForAdmin buildOrganisationResponseForAdmin(Organisation org) {
        Image logo = imageRepository.findOrganisationLogoByOrgId(org.getId());
        Integer countEvents = org.getEvents().size();

        String logoUrl = (logo != null) ? logo.getUrl() : null;

        return OrganisationResponseForAdmin.builder().
                userId(org.getUser().getId())
                .logo(logoUrl)
                .email(org.getUser().getUsername())
                .isEnabled(org.getUser().getIsEnabled())
                .isApprovedByAdmin(org.getUser().getIsApprovedByAdmin())
                .isNonLocked(org.getUser().getIsNonLocked())
                .countEvents(countEvents)
                .registeredAt(org.getUser().getRegisteredAt())
                .updatedAt(org.getUser().getUpdatedAt())
                .build();
    }



    public OneTimeEventResponse buildOneTimeEventResponse(Event event) {
        Image eventPicture = event.getEventImage();
        Long imageId = eventPicture != null ? eventPicture.getId() : null;
        return OneTimeEventResponse.builder()
                .id(event.getId())
                .imageId(imageId)
                .imageUrl(eventPicture != null ? eventPicture.getUrl() : null)
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
    public OrganisationResponse buildOrganisationResponse(Organisation org) {
        Image logo = imageRepository.findOrganisationLogoByOrgId(org.getId());
        Image background = imageRepository.findOrganisationCoverPictureByOrgId(org.getId());
        Set<String> orgPriorities = utils.convertListOfOrganisationPrioritiesToString(org.getOrganisationPriorities());

        String logoUrl = (logo != null) ? logo.getUrl() : null;
        String backgroundUrl = (background != null) ? background.getUrl() : null;

        return OrganisationResponse.builder().
                orgId(org.getId())
                .logo(logoUrl)
                .background(backgroundUrl)
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
