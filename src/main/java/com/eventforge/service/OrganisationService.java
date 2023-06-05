package com.eventforge.service;

import com.eventforge.dto.EventResponse;
import com.eventforge.dto.OrganisationRequest;
import com.eventforge.dto.OrganisationResponse;
import com.eventforge.model.Organisation;
import com.eventforge.model.User;
import com.eventforge.repository.OrganisationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

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

    public Organisation getOrganisationByUserUsername(String username){
        return organisationRepository.findOrganisationByEmail(username);
    }
    public void updateOrganisation(OrganisationRequest organisationRequest , String token) {
        User currentLoggedUser = userService.getLoggedUserByToken(token);
        if(currentLoggedUser!=null) {
            currentLoggedUser.setUsername(organisationRequest.getUsername());
            currentLoggedUser.setName(organisationRequest.getFullName());
            currentLoggedUser.setPhone(organisationRequest.getPhone());
            userService.saveUserInDb(currentLoggedUser);
            Organisation organisation = getOrganisationByUserUsername(currentLoggedUser.getUsername());
            organisation.setName(organisationRequest.getName());
            organisation.setBullstat(organisationRequest.getBullstat());
            organisation.setUser(currentLoggedUser);
            organisation.setAddress(organisationRequest.getAddress());
            organisation.setOrganisationPriorities(organisationRequest.getOrganisationPriorities());
            organisation.setCharityOption(organisationRequest.getCharityOption());
            organisation.setPurposeOfOrganisation(organisationRequest.getPurposeOfOrganisation());
            organisationRepository.save(organisation);
        }
    }
    public List<OrganisationResponse> getAllOrganisations() {
        return organisationRepository.findAll().stream().map(event -> mapper.map(event, OrganisationResponse.class)).toList();
    }
    public OrganisationResponse getOrganisationById(UUID organisationId) {
        Optional<Organisation> organisationResponse = organisationRepository.findById(organisationId);
        return mapper.map(organisationResponse , OrganisationResponse.class);
    }

    public OrganisationResponse getOrgByName(String orgName){
        Optional<Organisation> organisationResponse = organisationRepository.findOrganisationByName(orgName);
        return mapper.map(organisationResponse , OrganisationResponse.class);
    }

}
