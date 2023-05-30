package com.eventforge.factory;

import com.eventforge.dto.RegistrationRequest;
import com.eventforge.model.Organisation;
import com.eventforge.model.User;
import com.eventforge.repository.OrganisationRepository;
import com.eventforge.service.OrganisationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrganisationBuilder {

    private final OrganisationService organisationService;
    private final UserBuilder userBuilder;

    public User createOrganisation(RegistrationRequest request){
        User user = userBuilder.createUser(request);
        Organisation org = Organisation.builder()
                .name(request.getName())
                .bullstat(request.getBullstat())
                .user(user)
                .address(request.getAddress())
                .website(request.getWebsite())
                .facebookLink(request.getFacebookLink())
                .charityOption(request.getOptionalCharity())
                .purposeOfOrganisation(request.getPurposeOfOrganisation())
                .build();
        organisationService.saveOrganisationInDb(org);
        return user;
    }

}
