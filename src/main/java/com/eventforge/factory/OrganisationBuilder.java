package com.eventforge.factory;

import com.eventforge.dto.RegistrationRequest;
import com.eventforge.model.Organisation;
import com.eventforge.model.OrganisationPriority;
import com.eventforge.model.User;
import com.eventforge.service.OrganisationPriorityService;
import com.eventforge.service.OrganisationService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class OrganisationBuilder {

    private final OrganisationService organisationService;
    private final UserBuilder userBuilder;
    private final OrganisationPriorityService organisationPriorityService;

    private final ModelMapper mapper;


    public User createOrganisation(RegistrationRequest request) {
        User user = userBuilder.createUser(request);
        Organisation org = Organisation.builder()
                .name(request.getName())
                .bullstat(request.getBullstat())
                .user(user)
                .address(request.getAddress())
                .organisationPriorities(assignOrganisationPrioritiesToOrganisation(request.getOrganisationPriorities()))
                .website(request.getWebsite())
                .facebookLink(request.getFacebookLink())
                .charityOption(request.getCharityOption())
                .organisationPurpose(request.getOrganisationPurpose())
                .build();
        organisationService.saveOrganisationInDb(org);
        return user;
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
