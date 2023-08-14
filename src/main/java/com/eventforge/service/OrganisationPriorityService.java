package com.eventforge.service;

import com.eventforge.model.OrganisationPriority;
import com.eventforge.repository.OrganisationPriorityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrganisationPriorityService {

    private final OrganisationPriorityRepository organisationPriorityRepository;


    public OrganisationPriority getOrganisationPriorityByCategory(String category){
        return organisationPriorityRepository.findByCategory(category);
    }

    public void saveOrganisationPriority(OrganisationPriority organisationPriority){
        organisationPriorityRepository.save(organisationPriority);
    }
}
