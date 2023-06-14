package com.eventforge.service;

import com.eventforge.dto.request.UpdateAccountRequest;
import com.eventforge.dto.response.OrganisationResponse;
import com.eventforge.model.Organisation;
import com.eventforge.model.User;
import com.eventforge.repository.OrganisationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrganisationServiceTest {
    @Mock
    private OrganisationRepository organizationRepository;
    @Mock
    private UserService userService;
    @Mock
    private Utils utils;
    @Mock
    private ModelMapper mapper;
    private OrganisationService organisationService;
    @BeforeEach
    public void init(){
        organisationService = new OrganisationService(organizationRepository,mapper,userService,utils);
    }
    @Test
    void testSaveOrganisationInDb() {
        Organisation organisation = new Organisation();
        organisationService.saveOrganisationInDb(organisation);
        verify(organizationRepository).save(organisation);
    }

    @Test
    void testUpdateOrganisation_ValidTokenAndUser() {
        List<Organisation> organisationList = List.of(
                Organisation.builder().name("org1").build(),
                Organisation.builder().name("org2").build()
        );
        User currentLoggedUser = User.builder()
                .username("testuser")
                .organisations(organisationList)
                .build();

        UpdateAccountRequest request = new UpdateAccountRequest();
        request.setFullName("John Doe");
        request.setPhoneNumber("123456789");
        request.setOrganisationPurpose(organisationList.get(0).getName());

        Organisation organisation = Organisation.builder().build();
        when(userService.getLoggedUserByToken(anyString())).thenReturn(currentLoggedUser);
        when(organisationService.getOrganisationByUserUsername(anyString())).thenReturn(organisation);

        organisationService.updateOrganisation(request, "validToken");

        verify(userService).getLoggedUserByToken("validToken");
        verify(userService).saveUserInDb(currentLoggedUser);
        verify(organizationRepository).save(organisation);
        assertEquals("John Doe", currentLoggedUser.getFullName());
        assertEquals("123456789", currentLoggedUser.getPhoneNumber());
    }
    @Test
    void testGetOrgByName_ValidOrgName() {
        String orgName = "Test Org";
        Organisation organisation = Organisation.builder()
                .name(orgName)
                .build();
        OrganisationResponse expectedResponse = OrganisationResponse.builder()
                .name(orgName)
                .build();

        when(organizationRepository.findOrganisationByName(orgName)).thenReturn(Optional.of(organisation));
        when(mapper.map(Optional.of(organisation), OrganisationResponse.class)).thenReturn(expectedResponse);

        OrganisationResponse response = organisationService.getOrgByName(orgName);

        verify(organizationRepository).findOrganisationByName(orgName);
        verify(mapper).map(Optional.of(organisation), OrganisationResponse.class);
        assertThat(response).isEqualTo(expectedResponse);
    }
    @Test
    void testGetOrganisationByUserId_ValidUserId() {
        Long userId = 1L;
        Organisation expectedOrganisation = Organisation.builder()
                .id(1L)
                .name("Test Org")
                .build();

        when(organizationRepository.findOrganisationByUserId(userId)).thenReturn(expectedOrganisation);

        Organisation result = organisationService.getOrganisationByUserId(userId);

        verify(organizationRepository).findOrganisationByUserId(userId);
        assertThat(result).isEqualTo(expectedOrganisation);
    }

    @Test
    void testGetOrganisationByUserId_InvalidUserId() {
        Long userId = 2L;

        when(organizationRepository.findOrganisationByUserId(userId)).thenReturn(null);
        Organisation result = organisationService.getOrganisationByUserId(userId);

        verify(organizationRepository).findOrganisationByUserId(userId);
        assertThat(result).isNull();
    }
    @Test
    void testGetOrganisationById_OrganisationExists() {
        Long organisationId = 1L;

        when(organizationRepository.findById(organisationId)).thenReturn(Optional.of(new Organisation()));
        OrganisationResponse response = organisationService.getOrganisationById(organisationId);

        verify(organizationRepository).findById(organisationId);
    }
    @Test
    void testGetOrganisationById_OrganisationDoesNotExist() {
        Long organisationId = 1L;
        when(organizationRepository.findById(organisationId)).thenReturn(Optional.empty());

        OrganisationResponse response = organisationService.getOrganisationById(organisationId);

        verify(organizationRepository).findById(organisationId);
        assertThat(response).isNull();
    }
}