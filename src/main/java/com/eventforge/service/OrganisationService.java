package com.eventforge.service;

import com.eventforge.dto.request.UpdateAccountRequest;
import com.eventforge.dto.response.OrganisationResponse;
import com.eventforge.dto.response.OrganisationResponseForAdmin;
import com.eventforge.factory.ResponseFactory;
import com.eventforge.model.Organisation;
import com.eventforge.model.OrganisationPriority;
import com.eventforge.model.User;
import com.eventforge.repository.OrganisationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;


@Service
@RequiredArgsConstructor
@Slf4j
public class OrganisationService {

    private final OrganisationRepository organisationRepository;
    private final ModelMapper mapper;
    private final UserService userService;
    private final ResponseFactory responseFactory;

    private final Utils utils;

    public void saveOrganisationInDb(Organisation organisation){
        organisationRepository.save(organisation);
        log.info("Успешна регистрация");
    }
    public List<OrganisationResponse> getAllOrganisationsForUnauthorizedUser(String name){
        if(name == null || name.isEmpty()){
            return organisationRepository.findAllOrganisations().stream().map(responseFactory::buildOrganisationResponse).toList();
        }
        return organisationRepository.findAllOrganisationsForUserByName(name).stream().map(responseFactory::buildOrganisationResponse).toList();
    }


    public List<OrganisationResponseForAdmin> getAllApprovedOrganisationsForAdmin(){
        return organisationRepository.findAllApprovedOrganisationsForAdmin().stream().map(responseFactory::buildOrganisationResponseForAdmin).toList();
    }

    public List<OrganisationResponseForAdmin> getAllUnapprovedOrganisationForAdmin(){
        return organisationRepository.findAllUnapprovedOrganisationsForAdmin().stream().map(responseFactory::buildOrganisationResponseForAdmin).toList();
    }

    public Organisation getOrganisationByUserId(Long userId){
        return organisationRepository.findOrganisationByUserId(userId);
    }
    public Organisation getOrganisationByUserUsername(String username){
        return organisationRepository.findOrganisationByEmail(username);
    }


    public void updateOrganisation(UpdateAccountRequest request, String token) {
        User currentLoggedUser = userService.getLoggedUserByToken(token);
        Set<OrganisationPriority> organisationPriorities =utils.
                assignOrganisationPrioritiesToOrganisation(request.getChosenPriorities(), request.getOptionalCategory());
        if(currentLoggedUser!=null) {
            currentLoggedUser.setFullName(request.getFullName());
            currentLoggedUser.setPhoneNumber(request.getPhoneNumber());
            userService.saveUserInDb(currentLoggedUser);
            Organisation organisation = getOrganisationByUserUsername(currentLoggedUser.getUsername());
            organisation.setName(request.getName());
            organisation.setWebsite(request.getWebsite());
            organisation.setFacebookLink(request.getFacebookLink());
            organisation.setBullstat(request.getBullstat());
            organisation.setUser(currentLoggedUser);
            organisation.setAddress(request.getAddress());
            organisation.setOrganisationPriorities(organisationPriorities);
            organisation.setCharityOption(request.getCharityOption());
            organisation.setOrganisationPurpose(request.getOrganisationPurpose());
            organisationRepository.save(organisation);
            log.info("User with email {} successfully updated his account settings" , currentLoggedUser.getUsername() );
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
