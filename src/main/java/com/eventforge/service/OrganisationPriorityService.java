package com.eventforge.service;

import com.eventforge.model.OrganisationPriority;
import com.eventforge.repository.OrganisationPriorityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class OrganisationPriorityService {

    private final OrganisationPriorityRepository organisationPriorityRepository;





    public Set<String> getAllPriorityCategories(){
        return organisationPriorityRepository.getAllOrganisationPriorityCategories();
    }


    public OrganisationPriority getOrganisationPriorityCategoriesByCategory(String category){
        return organisationPriorityRepository.findByCategory(category);
    }
}
