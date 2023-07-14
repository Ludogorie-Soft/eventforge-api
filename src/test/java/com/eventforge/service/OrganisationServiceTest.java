package com.eventforge.service;

import com.eventforge.dto.request.UpdateAccountRequest;
import com.eventforge.dto.response.OrganisationResponse;
import com.eventforge.exception.OrganisationRequestException;
import com.eventforge.factory.ResponseFactory;
import com.eventforge.model.Organisation;
import com.eventforge.model.User;
import com.eventforge.repository.OrganisationRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
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
    private ResponseFactory responseFactory;
    @Mock
    private Utils utils;
    @Mock
    private ModelMapper mapper;
    private OrganisationService organisationService;

    @BeforeEach
    void init() {
        organisationService = new OrganisationService(organizationRepository, mapper, userService,responseFactory,utils);
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
    void testGetOrganisationDetailsByIdWithCondition_ValidId() {
        // Arrange
        Long organisationId = 1L;
        Organisation organisation = new Organisation(); // Create a test organisation object
        when(organizationRepository.findOrganisationById(organisationId)).thenReturn(organisation);
        OrganisationResponse expectedResponse = new OrganisationResponse(); // Create an expected response object
        when(responseFactory.buildOrganisationResponse(organisation)).thenReturn(expectedResponse);

        // Act
        OrganisationResponse actualResponse = organisationService.getOrganisationDetailsByIdWithCondition(organisationId);

        // Assert
        Assertions.assertEquals(expectedResponse, actualResponse);
        Mockito.verify(organizationRepository, Mockito.times(1)).findOrganisationById(organisationId);
        Mockito.verify(responseFactory, Mockito.times(1)).buildOrganisationResponse(organisation);
    }

    @Test
    void testGetOrganisationDetailsByIdWithCondition_InvalidId() {
        // Arrange
        Long organisationId = 2L;
        Mockito.when(organizationRepository.findOrganisationById(organisationId)).thenReturn(null);

        // Act and Assert
        Assertions.assertThrows(OrganisationRequestException.class, () -> organisationService.getOrganisationDetailsByIdWithCondition(organisationId));
        Mockito.verify(organizationRepository, Mockito.times(1)).findOrganisationById(organisationId);
    }

    @Test
    void testGetOrganisationDetailsByIdWithoutCondition_ValidId() {
        // Arrange
        Long organisationId = 1L;
        Organisation organisation = new Organisation(); // Create a test organisation object
        Optional<Organisation> organisationOptional = Optional.of(organisation);
        Mockito.when(organizationRepository.findById(organisationId)).thenReturn(organisationOptional);
        OrganisationResponse expectedResponse = new OrganisationResponse(); // Create an expected response object
        Mockito.when(responseFactory.buildOrganisationResponse(organisation)).thenReturn(expectedResponse);

        // Act
        OrganisationResponse actualResponse = organisationService.getOrganisationDetailsByIdWithoutCondition(organisationId);

        // Assert
        Assertions.assertEquals(expectedResponse, actualResponse);
        Mockito.verify(organizationRepository, Mockito.times(1)).findById(organisationId);
        Mockito.verify(responseFactory, Mockito.times(1)).buildOrganisationResponse(organisation);
    }


    @Test
    void testGetOrganisationDetailsByIdWithoutCondition_InvalidId() {
        // Arrange
        Long organisationId = 2L;
        Optional<Organisation> organisationOptional = Optional.empty();
        Mockito.when(organizationRepository.findById(organisationId)).thenReturn(organisationOptional);

        // Act and Assert
        Assertions.assertThrows(OrganisationRequestException.class, () -> organisationService.getOrganisationDetailsByIdWithoutCondition(organisationId));
        Mockito.verify(organizationRepository, Mockito.times(1)).findById(organisationId);
    }
}