package com.eventforge.service;

import com.eventforge.model.OrganisationPriority;
import com.eventforge.repository.OrganisationPriorityRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashSet;
import java.util.Set;

@ExtendWith(MockitoExtension.class)
public class OrganisationPriorityServiceTest {
    @Mock
    private OrganisationPriorityRepository organisationPriorityRepository;

    @InjectMocks
    private OrganisationPriorityService organisationPriorityService;

    @Test
    public void testGetAllPriorityCategories() {
        // Arrange
        Set<String> expectedCategories = new HashSet<>();
        expectedCategories.add("Category1");
        expectedCategories.add("Category2");
        Mockito.when(organisationPriorityRepository.getAllOrganisationPriorityCategories())
                .thenReturn(expectedCategories);

        // Act
        Set<String> actualCategories = organisationPriorityService.getAllPriorityCategories();

        // Assert
        Assertions.assertEquals(expectedCategories, actualCategories);
        Mockito.verify(organisationPriorityRepository, Mockito.times(1))
                .getAllOrganisationPriorityCategories();
    }

    @Test
    public void testGetOrganisationPriorityByCategory() {
        // Arrange
        String category = "Category1";
        OrganisationPriority expectedPriority = new OrganisationPriority();
        expectedPriority.setCategory(category);
        Mockito.when(organisationPriorityRepository.findByCategory(category))
                .thenReturn(expectedPriority);

        // Act
        OrganisationPriority actualPriority = organisationPriorityService.getOrganisationPriorityByCategory(category);

        // Assert
        Assertions.assertEquals(expectedPriority, actualPriority);
        Mockito.verify(organisationPriorityRepository, Mockito.times(1))
                .findByCategory(category);
    }

    @Test
    public void testSaveOrganisationPriority() {
        // Arrange
        OrganisationPriority organisationPriority = new OrganisationPriority();

        // Act
        organisationPriorityService.saveOrganisationPriority(organisationPriority);

        // Assert
        Mockito.verify(organisationPriorityRepository, Mockito.times(1))
                .save(organisationPriority);
    }
}
