package com.eventforge.service;

import com.eventforge.dto.request.OrganisationRequest;
import com.eventforge.dto.OrganisationResponse;
import com.eventforge.model.Organisation;
import com.eventforge.model.User;
import com.eventforge.repository.OrganisationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import java.util.Optional;


@Service
@RequiredArgsConstructor
@Slf4j
public class OrganisationService {

    private final OrganisationRepository organisationRepository;
    private final ModelMapper mapper;
    private final UserService userService;

    public void saveOrganisationInDb(Organisation organisation){
        organisationRepository.save(organisation);
        log.info("Успешна регистрация");
    }


    public Organisation getOrganisationByUserId(Long userId){
        return organisationRepository.findOrganisationByUserId(userId);
    }
    public Organisation getOrganisationByUserUsername(String username){
        return organisationRepository.findOrganisationByEmail(username);
    }
    public void updateOrganisation(OrganisationRequest organisationRequest , String token) {
        User currentLoggedUser = userService.getLoggedUserByToken(token);
        if(currentLoggedUser!=null) {
            currentLoggedUser.setUsername(organisationRequest.getUsername());
            currentLoggedUser.setFullName(organisationRequest.getFullName());
            currentLoggedUser.setPhoneNumber(organisationRequest.getPhone());
            userService.saveUserInDb(currentLoggedUser);
            Organisation organisation = getOrganisationByUserUsername(currentLoggedUser.getUsername());
            organisation.setName(organisationRequest.getName());
            organisation.setBullstat(organisationRequest.getBullstat());
            organisation.setUser(currentLoggedUser);
            organisation.setAddress(organisationRequest.getAddress());
            organisation.setOrganisationPriorities(organisationRequest.getOrganisationPriorities());
            organisation.setCharityOption(organisationRequest.getCharityOption());
            organisation.setOrganisationPurpose(organisationRequest.getOrganisationPurpose());
            organisationRepository.save(organisation);
        }
    }
    public OrganisationResponse getOrganisationById(Long organisationId) {
        Optional<Organisation> organisationResponse = organisationRepository.findById(organisationId);
        return mapper.map(organisationResponse , OrganisationResponse.class);
    }

    public OrganisationResponse getOrgByName(String orgName){
        Optional<Organisation> organisationResponse = organisationRepository.findOrganisationByName(orgName);
        return mapper.map(organisationResponse , OrganisationResponse.class);
    }

}
