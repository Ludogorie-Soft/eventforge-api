package com.eventforge.service;

import com.eventforge.dto.RegistrationRequest;
import com.eventforge.exception.GlobalException;
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

    private final UserService userService;

    public String registerUser(RegistrationRequest request){
        Organisation org = new Organisation();
        org.setName(request.getName());
        org.setBullstat(request.getBullstat());
        org.setPurposeOfOrganisation(request.getPurposeOfOrganisation());
        org.setUser(userService.createUser(request.getEmail(), request.getPassword()));
        organisationRepository.save(org);
        log.info("Created");
//       return "Успешно се регистрирахте";
        throw new GlobalException("Proba");
    }

}
