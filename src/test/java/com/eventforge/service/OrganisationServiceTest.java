package com.eventforge.service;

import com.eventforge.model.Organisation;
import com.eventforge.model.User;
import com.eventforge.repository.OrganisationRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrganisationServiceTest {
    @Mock
    private OrganisationRepository organizationRepository;
    @InjectMocks
    private OrganisationService organisationService;

    @Test
    void testSaveOrganisationInDb() {
        Organisation organisation = new Organisation();
        organisationService.saveOrganisationInDb(organisation);
        verify(organizationRepository).save(organisation);
    }
}