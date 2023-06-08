package com.eventforge.service;

import com.eventforge.constants.OrganisationPriorityCategory;
import com.eventforge.model.OrganisationPriority;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class Utils {
    private final OrganisationPriorityService organisationPriorityService;
    public  Set<OrganisationPriority> assignOrganisationPrioritiesToOrganisation(Set<String> priorityCategories, String optionalCategory) {
        Set<OrganisationPriority> organisationPriorities = new HashSet<>();
        OrganisationPriority newOrganisationPriority = null;
        if (optionalCategory != null && !optionalCategory.isEmpty()) {
            newOrganisationPriority = createOrganisationPriority(optionalCategory);
        }
        if (newOrganisationPriority != null) {
            organisationPriorities.add(newOrganisationPriority);
        }

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

    public OrganisationPriority createOrganisationPriority(String priority) {
        OrganisationPriority organisationPriority = null;
        if (organisationPriorityService.getOrganisationPriorityByCategory(priority) == null) {
            organisationPriority = new OrganisationPriority(priority);
            organisationPriorityService.saveOrganisationPriority(organisationPriority);
            OrganisationPriorityCategory.addNewOrganisationPriorityCategory(priority);

        }
        return organisationPriority;
    }

    public List<String> splitStringByComma(String input) {
        List<String> result = new ArrayList<>();
        if (input != null && !input.isEmpty()) {
            String[] splitStrings = input.split(",");
            Set<String> uniqueWords = new HashSet<>();
            for (String str : splitStrings) {
                String trimmedString = str.trim();
                if (!trimmedString.isEmpty()) {
                    uniqueWords.add(trimmedString.toLowerCase());
                }
            }
            result.addAll(uniqueWords);
        }
        return result;
    }


}
