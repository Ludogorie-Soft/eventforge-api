package com.eventforge.service;

import com.eventforge.model.OrganisationPriority;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

import java.security.SecureRandom;
import java.text.DecimalFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class Utils {
    private final OrganisationPriorityService organisationPriorityService;
    private final PasswordEncoder passwordEncoder;
    private static final SecureRandom random = new SecureRandom();
    private static final int NEW_GENERATED_PASSWORD_LENGTH = 15;

    private static final DecimalFormat decimalFormat = new DecimalFormat("0.00");

    public String convertIsOneTimeToString(Boolean isOneTime) {
        if (isOneTime) {
            return "еднократно";
        }
        return "регулярно";
    }

    public String convertPriceToString(Double price) {
        if (price < 1) {
            return "безплатно";
        }

        return decimalFormat.format(price) + " лева";
    }

    public String convertAgeToString(Integer minAge, Integer maxAge) {
        if (minAge == 0 && maxAge == 0) {
            return "Няма ограничение за възрастта";
        }
        if (minAge > 0 && maxAge == 0) {
            return "Минимална възраст: " + minAge + " години";
        }
        if (minAge == 0 && maxAge > 0) {
            return "Максимална възраст: " + maxAge + " години";
        }
        return "Възрастов диапазон: " + minAge + " - " + maxAge + " години";
    }

    public Set<OrganisationPriority> assignOrganisationPrioritiesToOrganisation(Set<String> priorityCategories, String optionalCategory) {
        Set<OrganisationPriority> organisationPriorities = new HashSet<>();
        List<OrganisationPriority> newOrganisationPriorities;
        if (optionalCategory != null && !optionalCategory.isEmpty()) {
            newOrganisationPriorities = createOrganisationPriority(optionalCategory);
            if (!newOrganisationPriorities.isEmpty()) {
                organisationPriorities.addAll(newOrganisationPriorities);

            }
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

    //this method is when we display the organisations for the end user-client
    public Set<String> convertListOfOrganisationPrioritiesToString(Set<OrganisationPriority> organisationPriorities){
        Set<String> orgPriorities = new HashSet<>();
        for(OrganisationPriority priority : organisationPriorities){
            orgPriorities.add(priority.getCategory());
        }
        return orgPriorities;
    }

    //this method is цаллед upon update for organisation
    public Set<String> convertListOfStaticOrganisationPrioritiesToString(Set<OrganisationPriority> staticOrganisationPriorityCategories , int staticOrgPrioritiesSize) {
        Set<String> setOfOrgPriorities = new HashSet<>();
        if (staticOrganisationPriorityCategories != null && staticOrganisationPriorityCategories.size() > 0) {
            for (OrganisationPriority priority : staticOrganisationPriorityCategories) {
                if(priority.getId() <= staticOrgPrioritiesSize){
                    setOfOrgPriorities.add(priority.getCategory());
                }
            }
        }
        return setOfOrgPriorities;
    }

    //this method is called upon update for organisation
    public String convertListOfOptionalOrganisationPrioritiesToString(Set<OrganisationPriority> optionalOrganisationPriorityCategories, int staticOrgPrioritiesSize){
        StringJoiner optionalOrgCategories = new StringJoiner(", ");
        if(optionalOrganisationPriorityCategories != null && optionalOrganisationPriorityCategories.size() > 0){
            for(OrganisationPriority optionalPriority : optionalOrganisationPriorityCategories){
                if(optionalPriority.getId() > staticOrgPrioritiesSize){
                    optionalOrgCategories.add(optionalPriority.getCategory());
                }
            }
        }
        return optionalOrgCategories.toString();
    }


    public String convertStringListToString(List<String> stringList) {
        List<String> lowercaseList = stringList.stream()
                .map(String::toLowerCase)
                .collect(Collectors.toList());

        return String.join(",", lowercaseList);
    }

    public List<OrganisationPriority> createOrganisationPriority(String priority) {
        List<String> categories = splitStringByComma(priority);
        OrganisationPriority organisationPriority;
        List<OrganisationPriority> prioritiesToReturn = new ArrayList<>();
        for (String category : categories) {
            organisationPriority = organisationPriorityService.getOrganisationPriorityByCategory(category);
            if (organisationPriority == null) {
                organisationPriority = new OrganisationPriority(category);
                organisationPriorityService.saveOrganisationPriority(organisationPriority);
            }
            prioritiesToReturn.add(organisationPriority);
        }
        return prioritiesToReturn;
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

    public String generateRandomPassword() {
        String chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789!@#*-_*";
        List<Character> charList = new ArrayList<>();
        for (char c : chars.toCharArray()) {
            charList.add(c);
        }

        Collections.shuffle(charList);
        StringBuilder password = new StringBuilder();

        for (int i = 0; i < NEW_GENERATED_PASSWORD_LENGTH; i++) {
            password.append(charList.get(random.nextInt(charList.size())));
        }

        return password.toString();
    }

    public boolean isPasswordValid(String password, String hashedPassword) {
        return passwordEncoder.matches(password, hashedPassword);
    }

    public String encodePassword(String password) {
        return passwordEncoder.encode(password);
    }
}
