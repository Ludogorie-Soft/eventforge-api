package com.eventforge.service;

import com.eventforge.constants.OrganisationPriorityCategory;
import com.eventforge.model.OrganisationPriority;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class Utils {
    private final OrganisationPriorityService organisationPriorityService;
    private final PasswordEncoder passwordEncoder;
    public  Set<OrganisationPriority> assignOrganisationPrioritiesToOrganisation(Set<String> priorityCategories, String optionalCategory) {
        Set<OrganisationPriority> organisationPriorities = new HashSet<>();
        OrganisationPriority newOrganisationPriority;
        if (optionalCategory != null && !optionalCategory.isEmpty()) {
            newOrganisationPriority = createOrganisationPriority(optionalCategory);
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

    public Set<String>convertListOfOrganisationPrioritiesToString(Set<OrganisationPriority> organisationPriorityCategories){
        Set<String> setOfOrgPriorities = new HashSet<>();
        if(organisationPriorityCategories!= null || organisationPriorityCategories.size()>0){
            for(OrganisationPriority priority : organisationPriorityCategories){
                setOfOrgPriorities.add(priority.getCategory());
            }
        }
        return setOfOrgPriorities;
    }


        public String convertStringListToString(List<String> stringList) {
            List<String> lowercaseList = stringList.stream()
                    .map(String::toLowerCase)
                    .collect(Collectors.toList());

            return String.join(",", lowercaseList);
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

    public String generateErrorStringFromMethodArgumentNotValidException(List<ObjectError> globalErrors, List<FieldError> fieldErrors) {
        StringJoiner joiner = new StringJoiner("/ ");

        if (!globalErrors.isEmpty()) {
            for (ObjectError error : globalErrors) {
                joiner.add(error.getObjectName() + ": " + error.getDefaultMessage());
            }
        }

        if (!fieldErrors.isEmpty()) {
            for (FieldError error : fieldErrors) {
                joiner.add(error.getField() + ": " + error.getDefaultMessage());
            }
        }

        return joiner.toString();
    }

    public boolean isPasswordValid(String password , String hashedPassword){
        return passwordEncoder.matches(password, hashedPassword);
    }
    public String encodePassword(String password){
        return passwordEncoder.encode(password);
    }
}
