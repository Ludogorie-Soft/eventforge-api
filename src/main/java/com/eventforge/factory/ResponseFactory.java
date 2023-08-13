package com.eventforge.factory;

import com.eventforge.dto.response.CommonEventResponse;
import com.eventforge.dto.response.OrganisationResponse;
import com.eventforge.dto.response.OrganisationResponseForAdmin;
import com.eventforge.model.Event;
import com.eventforge.model.Image;
import com.eventforge.model.Organisation;
import com.eventforge.repository.EventRepository;
import com.eventforge.repository.ImageRepository;
import com.eventforge.service.Utils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ResponseFactory {

    private final Utils utils;
    private final ImageRepository imageRepository;
    private final EventRepository eventRepository;

    public OrganisationResponseForAdmin buildOrganisationResponseForAdmin(Organisation org) {

        return OrganisationResponseForAdmin.builder().
                userId(org.getUser().getId())
                .orgId(org.getId())
                .orgName(org.getName())
                .fullName(org.getUser().getFullName())
                .phoneNumber(org.getUser().getPhoneNumber())
                .email(org.getUser().getUsername())
                .bullstat(org.getBullstat())
                .isEnabled(org.getUser().getIsEnabled())
                .isApprovedByAdmin(org.getUser().getIsApprovedByAdmin())
                .isNonLocked(org.getUser().getIsNonLocked())
                .registeredAt(org.getUser().getRegisteredAt())
                .updatedAt(org.getUser().getUpdatedAt())
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
                .email(org.getUser().getUsername())
                .address(org.getAddress())
                .website(org.getWebsite())
                .facebookLink(org.getFacebookLink())
                .charityOption(org.getCharityOption())
                .organisationPurpose(org.getOrganisationPurpose())
                .organisationPriorities(orgPriorities)
                .expiredEvents(fetchExpiredEvents(org.getId()))
                .activeEvents(fetchActiveEvents(org.getId()))
                .upcomingEvents(fetchUpcomingEvents(org.getId()))
                .build();
    }

    private List<CommonEventResponse> fetchExpiredEvents(Long orgId) {

        return eventRepository.findAllExpiredEvents(orgId, LocalDateTime.now()).stream()
                .map(this::buildCommonEventResponse)
                .toList();
    }

    private List<CommonEventResponse> fetchActiveEvents(Long orgId) {
        return eventRepository.findAllActiveEvents(orgId, LocalDateTime.now()).stream()
                .map(this::buildCommonEventResponse)
                .toList();
    }

    private List<CommonEventResponse> fetchUpcomingEvents(Long orgId) {
        return eventRepository.findAllUpcomingEvents(orgId, LocalDateTime.now()).stream()
                .map(this::buildCommonEventResponse)
                .toList();
    }


    public CommonEventResponse buildCommonEventResponse(Event event) {
        CommonEventResponse eventResponse = new CommonEventResponse();

        eventResponse.setId(event.getId());
        eventResponse.setOrgId(event.getOrganisation().getId());
        eventResponse.setImageId(event.getEventImage().getId());
        eventResponse.setImageUrl(event.getEventImage().getUrl());
        eventResponse.setName(event.getName());
        eventResponse.setOrganisationName(event.getOrganisation().getName());
        eventResponse.setOnline(event.getIsOnline());
        eventResponse.setDescription(event.getDescription());
        eventResponse.setAddress(event.getAddress());
        if (event.getFacebookLink() != null) {
            eventResponse.setFacebookLink(event.getFacebookLink());
        }
        eventResponse.setEventCategories(event.getEventCategories());
        eventResponse.setPrice(utils.convertPriceToString(event.getPrice()));
        eventResponse.setAgeBoundary(utils.convertAgeToString(event.getMinAge(), event.getMaxAge()));
        eventResponse.setStartsAt(event.getStartsAt());
        eventResponse.setEndsAt(event.getEndsAt());
        eventResponse.setIsOneTime(utils.convertIsOneTimeToString(event.getIsOneTime()));
        if (event.getRecurrenceDetails() != null && !event.getIsOneTime()) {
            eventResponse.setRecurrenceDetails(event.getRecurrenceDetails());
        } else {
            eventResponse.setRecurrenceDetails(null);
        }

        return eventResponse;
    }
}
