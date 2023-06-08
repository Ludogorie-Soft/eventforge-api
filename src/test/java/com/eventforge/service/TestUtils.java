package com.eventforge.service;

import com.eventforge.constants.OrganisationPriorityCategory;
import com.eventforge.model.OrganisationPriority;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.verification.VerificationMode;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TestUtils {

    @Mock
    private OrganisationPriorityService organisationPriorityService;

    @Mock
    private PasswordEncoder passwordEncoder;

    private Utils utils;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        utils = new Utils(organisationPriorityService, passwordEncoder);
    }

    @Test
    void testAssignOrganisationPrioritiesToOrganisation_withOptionalCategory() {
        // Mock data
        Set<String> priorityCategories = new HashSet<>(Arrays.asList("Category1", "Category2"));
        String optionalCategory = "OptionalCategory";

        OrganisationPriority newOrganisationPriority = new OrganisationPriority(optionalCategory);
        when(organisationPriorityService.getOrganisationPriorityByCategory(optionalCategory)).thenReturn(null);
        doAnswer(invocation -> {
            OrganisationPriority priority = invocation.getArgument(0);
            assertEquals(newOrganisationPriority, priority);
            OrganisationPriorityCategory.addNewOrganisationPriorityCategory(optionalCategory);
            return null;
        }).when(organisationPriorityService).saveOrganisationPriority(any(OrganisationPriority.class));

        OrganisationPriority category1 = new OrganisationPriority("Category1");
        OrganisationPriority category2 = new OrganisationPriority("Category2");
        when(organisationPriorityService.getOrganisationPriorityByCategory("Category1")).thenReturn(category1);
        when(organisationPriorityService.getOrganisationPriorityByCategory("Category2")).thenReturn(category2);

        // Invoke the method under test
        Set<OrganisationPriority> result = utils.assignOrganisationPrioritiesToOrganisation(priorityCategories, optionalCategory);

        // Verify the interactions and assertions
        verify(organisationPriorityService).getOrganisationPriorityByCategory(optionalCategory);
        verify(organisationPriorityService).saveOrganisationPriority(newOrganisationPriority);
        verify(organisationPriorityService).getOrganisationPriorityByCategory("Category1");
        verify(organisationPriorityService).getOrganisationPriorityByCategory("Category2");

        Set<OrganisationPriority> expected = new HashSet<>(Arrays.asList(newOrganisationPriority, category1, category2));
        assertEquals(expected, result);
    }

    @Test
    void testAssignOrganisationPrioritiesToOrganisation_withoutOptionalCategory() {
        // Mock data
        Set<String> priorityCategories = new HashSet<>(Arrays.asList("Category1", "Category2"));

        OrganisationPriority category1 = new OrganisationPriority("Category1");
        OrganisationPriority category2 = new OrganisationPriority("Category2");
        when(organisationPriorityService.getOrganisationPriorityByCategory("Category1")).thenReturn(category1);
        when(organisationPriorityService.getOrganisationPriorityByCategory("Category2")).thenReturn(category2);

        // Invoke the method under test
        Set<OrganisationPriority> result = utils.assignOrganisationPrioritiesToOrganisation(priorityCategories, null);

        // Verify the interactions and assertions
        verify(organisationPriorityService).getOrganisationPriorityByCategory("Category1");
        verify(organisationPriorityService).getOrganisationPriorityByCategory("Category2");
        verify(organisationPriorityService, never()).saveOrganisationPriority(any(OrganisationPriority.class));

        Set<OrganisationPriority> expected = new HashSet<>(Arrays.asList(category1, category2));
        assertEquals(expected, result);
    }

    @Test
    void testConvertListOfOrganisationPrioritiesToString() {
        // Mock data
        OrganisationPriority priority1 = new OrganisationPriority("Category1");
        OrganisationPriority priority2 = new OrganisationPriority("Category2");
        Set<OrganisationPriority> organisationPriorityCategories = new HashSet<>(Arrays.asList(priority1, priority2));

        // Invoke the method under test
        Set<String> result = utils.convertListOfOrganisationPrioritiesToString(organisationPriorityCategories);

        // Verify the result
        Set<String> expected = new HashSet<>(Arrays.asList("Category1", "Category2"));
        assertEquals(expected, result);
    }

    @Test
    void testConvertStringListToString() {
        // Mock data
        List<String> stringList = Arrays.asList("Apple", "Banana", "Orange");

        // Invoke the method under test
        String result = utils.convertStringListToString(stringList);

        // Verify the result
        String expected = "apple,banana,orange";
        assertEquals(expected, result);
    }

    @Test
    public void testCreateOrganisationPriority() {
        // Arrange
        String priority = "somePriority";
        OrganisationPriority expectedPriority = new OrganisationPriority(priority);

        // Mock the behavior of the organisationPriorityService
        when(organisationPriorityService.getOrganisationPriorityByCategory(priority)).thenReturn(null);

        // Act
        OrganisationPriority result = utils.createOrganisationPriority(priority);

        // Assert
        assertEquals(expectedPriority, result);

        // Verify that the saveOrganisationPriority method is called
        verify(organisationPriorityService, times(1)).saveOrganisationPriority(expectedPriority);
    }

    @Test
    public void testCreateOrganisationPriority_ExistingPriority() {
        // Arrange
        String priority = "existingPriority";

        // Mock the behavior of the organisationPriorityService
        when(organisationPriorityService.getOrganisationPriorityByCategory(priority)).thenReturn(new OrganisationPriority(priority));

        // Act
        OrganisationPriority result = utils.createOrganisationPriority(priority);

        // Assert
        assertNull(result);

        // Verify that the saveOrganisationPriority method is not called
        verify(organisationPriorityService, never()).saveOrganisationPriority(any());
    }

    @Test
    public void testSplitStringByComma() {
        // Arrange
        String input = "apple, banana, orange, apple, banana, strawberry";

        // Act
        List<String> result = utils.splitStringByComma(input);

        // Assert
        Set<String> expected = new HashSet<>(Arrays.asList("apple", "banana", "orange", "strawberry"));
        assertEquals(expected.size(), result.size());
        assertTrue(expected.containsAll(result));
    }

    @Test
    public void testSplitStringByComma_EmptyInput() {
        // Arrange
        String input = "";

        // Act
        List<String> result = utils.splitStringByComma(input);

        // Assert
        List<String> expected = Arrays.asList();
        assertEquals(expected, result);
    }
    @Test
    public void testSplitStringByComma_NullInput() {
        // Arrange
        String input = null;

        // Act
        List<String> result = utils.splitStringByComma(input);

        // Assert
        List<String> expected = Arrays.asList();
        assertEquals(expected, result);
    }

    @Test
    public void testGenerateErrorStringFromMethodArgumentNotValidException() {
        // Arrange
        List<ObjectError> globalErrors = new ArrayList<>();
        List<FieldError> fieldErrors = new ArrayList<>();

        globalErrors.add(new ObjectError("object1", "Global error message 1"));
        globalErrors.add(new ObjectError("object2", "Global error message 2"));

        fieldErrors.add(new FieldError("field1", "field1", "Field error message 1"));
        fieldErrors.add(new FieldError("field2", "field2", "Field error message 2"));

        // Act
        String result = utils.generateErrorStringFromMethodArgumentNotValidException(globalErrors, fieldErrors);

        // Assert
        String expected = "object1: Global error message 1/ object2: Global error message 2/ field1: Field error message 1/ field2: Field error message 2";
        assertEquals(expected, result);
    }

    @Test
    public void testGenerateErrorStringFromMethodArgumentNotValidException_EmptyErrors() {
        // Arrange
        List<ObjectError> globalErrors = new ArrayList<>();
        List<FieldError> fieldErrors = new ArrayList<>();

        // Act
        String result = utils.generateErrorStringFromMethodArgumentNotValidException(globalErrors, fieldErrors);

        // Assert
        assertEquals("", result);
    }

    @Test
    public void testGenerateErrorStringFromMethodArgumentNotValidException_OnlyGlobalErrors() {
        // Arrange
        List<ObjectError> globalErrors = new ArrayList<>();
        List<FieldError> fieldErrors = new ArrayList<>();

        globalErrors.add(new ObjectError("object1", "Global error message 1"));
        globalErrors.add(new ObjectError("object2", "Global error message 2"));

        // Act
        String result = utils.generateErrorStringFromMethodArgumentNotValidException(globalErrors, fieldErrors);

        // Assert
        String expected = "object1: Global error message 1/ object2: Global error message 2";
        assertEquals(expected, result);
    }

    @Test
    public void testGenerateErrorStringFromMethodArgumentNotValidException_OnlyFieldErrors() {
        // Arrange
        List<ObjectError> globalErrors = new ArrayList<>();
        List<FieldError> fieldErrors = new ArrayList<>();

        fieldErrors.add(new FieldError("field1", "field1", "Field error message 1"));
        fieldErrors.add(new FieldError("field2", "field2", "Field error message 2"));

        // Act
        String result = utils.generateErrorStringFromMethodArgumentNotValidException(globalErrors, fieldErrors);

        // Assert
        String expected = "field1: Field error message 1/ field2: Field error message 2";
        assertEquals(expected, result);
    }

    @Test
    void testIsPasswordValid() {
        String rawPassword = "password123";
        String hashedPassword = "$2a$10$9g28eFxm9HyryZgtDxMvwe7TsNq9JLlt5r3rRJblu.qXsG3mhc5a2";

        when(passwordEncoder.matches(rawPassword, hashedPassword)).thenReturn(true);

        assertTrue(utils.isPasswordValid(rawPassword, hashedPassword));

        verify(passwordEncoder, times(1)).matches(rawPassword, hashedPassword);
    }

    @Test
    void testIsPasswordInvalid() {
        String rawPassword = "password123";
        String hashedPassword = "$2a$10$9g28eFxm9HyryZgtDxMvwe7TsNq9JLlt5r3rRJblu.qXsG3mhc5a2";

        when(passwordEncoder.matches(rawPassword, hashedPassword)).thenReturn(false);

        assertFalse(utils.isPasswordValid(rawPassword, hashedPassword));

        verify(passwordEncoder, times(1)).matches(rawPassword, hashedPassword);
    }

    @Test
    void testEncodePassword() {
        String rawPassword = "password123";
        String encodedPassword = "$2a$10$9g28eFxm9HyryZgtDxMvwe7TsNq9JLlt5r3rRJblu.qXsG3mhc5a2";

        when(passwordEncoder.encode(rawPassword)).thenReturn(encodedPassword);

        String result = utils.encodePassword(rawPassword);

        assertEquals(encodedPassword, result);

        verify(passwordEncoder, times(1)).encode(rawPassword);
    }
}

