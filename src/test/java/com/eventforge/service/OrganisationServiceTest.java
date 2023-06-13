package com.eventforge.service;

import com.eventforge.model.Organisation;
import com.eventforge.repository.OrganisationRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class OrganisationServiceTest {
    @Mock
    private OrganisationRepository organizationRepository;
    @Mock
    private ModelMapper mapper;
    @Mock
    private UserService userService;

    @InjectMocks
    private OrganisationService organisationService;

    @Test
    void testSaveOrganisationInDb() {
        Organisation organisation = new Organisation();
        organisationService.saveOrganisationInDb(organisation);
        verify(organizationRepository).save(organisation);
    }
}