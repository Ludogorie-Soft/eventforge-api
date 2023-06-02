package com.eventforge.service;

import com.eventforge.model.Organisation;
import com.eventforge.repository.OrganisationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrganisationService {

    private final OrganisationRepository organisationRepository;


    public void saveOrganisationInDb(Organisation organisation) {
        organisationRepository.save(organisation);
        log.info("Успешна регистрация");
    }

}
