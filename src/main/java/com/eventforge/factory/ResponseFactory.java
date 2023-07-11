package com.eventforge.factory;

import com.eventforge.dto.response.*;
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
        String logoData = logo.getUrl();

        return OrganisationResponseForAdmin.builder().
                userId(org.getUser().getId())
                .logo(logoData)
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
        Long imageId = eventPicture.getId();

        String eventPictureData = eventPicture.getUrl();


        return OneTimeEventResponse.builder()
                .id(event.getId())
                .imageId(imageId)
                .imageUrl(eventPictureData)
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

        String logoData = logo.getUrl();

        String backgroundData = background.getUrl();

        return OrganisationResponse.builder().
                orgId(org.getId())
                .logo(logoData)
                .background(backgroundData)
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
        Long imageId = event.getEventImage() != null ? event.getEventImage().getId() : null;
        String eventPictureData = event.getEventImage().getUrl();


        return RecurrenceEventResponse.builder()
                .id(event.getId())
                .imageId(imageId)
                .imageUrl(eventPictureData)
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

    public CommonEventResponse buildCommonEventResponse(Event event) {
        CommonEventResponse eventResponse = new CommonEventResponse();

        eventResponse.setId(event.getId());
        eventResponse.setImageId(event.getEventImage().getId());
        eventResponse.setImageUrl(event.getEventImage().getUrl());
        eventResponse.setName(event.getName());
        eventResponse.setOrganisationName(event.getOrganisation().getName());
        eventResponse.setOnline(event.getIsOnline());
        eventResponse.setDescription(event.getDescription());
        eventResponse.setAddress(event.getAddress());
        eventResponse.setEventCategories(event.getEventCategories());
        eventResponse.setPrice(utils.convertPriceToString(event.getPrice()));
        eventResponse.setAgeBoundary(utils.convertAgeToString(event.getMinAge(), event.getMaxAge()));
        eventResponse.setStartsAt(event.getStartsAt());
        eventResponse.setEndsAt(event.getEndsAt());
        eventResponse.setIsOneTime(utils.convertIsOneTimeToString(event.getIsOneTime()));
        eventResponse.setRecurrenceDetails(event.getRecurrenceDetails());

        return eventResponse;
    }
}
