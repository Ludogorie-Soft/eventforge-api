package com.eventforge.service;

import com.eventforge.dto.*;
import com.eventforge.exception.EventRequestException;
import com.eventforge.exception.GlobalException;
import com.eventforge.exception.OrganisationRequestException;
import com.eventforge.model.Event;
import com.eventforge.model.Organisation;
import com.eventforge.repository.OrganisationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrganisationService {

    private final OrganisationRepository organisationRepository;
    private final ModelMapper mapper;


    public void saveOrganisationInDb(Organisation organisation){
        organisationRepository.save(organisation);
        log.info("Успешна регистрация");
    }
    public void updateOrganisation(UUID organisationId, OrganisationRequest organisationRequest) {
        Organisation organisation = organisationRepository.findById(organisationId).orElseThrow(() -> new OrganisationRequestException("Организация с номер " + organisationId + " не е намерен!"));
        organisation.setName(organisationRequest.getName());
        organisation.setBullstat(organisationRequest.getBullstat());
        organisation.setUser(organisationRequest.getUser());
        organisation.setAddress(organisationRequest.getAddress());
        organisation.setOrganisationPriorities(organisationRequest.getOrganisationPriorities());
        organisation.setCharityOption(organisationRequest.getCharityOption());
        organisation.setPurposeOfOrganisation(organisationRequest.getPurposeOfOrganisation());
        organisationRepository.save(organisation);
    }
    public OrganisationResponse getOrganisationById(UUID organisationId) {
        return organisationRepository.findById(organisationId).map(organisation ->
                mapper.map(organisation, OrganisationResponse.class)).orElseThrow(() -> new OrganisationRequestException("Организация с номер " + organisationId + " не е намерен!"));
    }

}
