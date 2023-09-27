package com.eventforge.service.service;

import com.eventforge.model.OrganisationPriority;
import com.eventforge.service.OrganisationPriorityService;
import com.eventforge.service.Utils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TestUtils {

    @Mock
    private OrganisationPriorityService organisationPriorityService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private Utils utils;



    @Test
    public void convertIsOneTimeToString_WhenIsOneTimeIsTrue_ShouldReturnCorrectString() {
        // Arrange
        Boolean isEvent = true;


        // Act
        String result = utils.convertIsEventToString(isEvent);

        // Assert
        assertEquals("събитие", result);
    }

    @Test
    public void convertIsOneTimeToString_WhenIsOneTimeIsFalse_ShouldReturnCorrectString() {
        // Arrange
        Boolean isEvent = false;
        // Act
        String result = utils.convertIsEventToString(isEvent);

        // Assert
        assertEquals("обява", result);
    }
    @Test
 void testConvertPriceToString() {
        String result1 = utils.convertPriceToString(0.5);
        assertEquals("безплатно", result1, "Should return 'безплатно' when price is less than 1");

        String result2 = utils.convertPriceToString(1.0);
        assertEquals("1.00 лева", result2, "Should return '1.0 лева' when price is equal to 1");

        String result3 = utils.convertPriceToString(5.99);
        assertEquals("5.99 лева", result3, "Should return the price with 'лева' when price is greater than 1");
    }
    @Test
    void testConvertAgeToString() {
        String result1 = utils.convertAgeToString(0, 0);
        assertEquals("Няма ограничение на възрастта", result1, "Should return 'Няма ограничение на възрастта'");

        String result2 = utils.convertAgeToString(18, 0);
        assertEquals("Минимална възраст: 18 години", result2, "Should return 'Минимална възраст: 18 години'");

        String result3 = utils.convertAgeToString(0, 65);
        assertEquals("Максимална възраст: 65 години", result3, "Should return 'Максимална възраст: 65 години'");

        String result4 = utils.convertAgeToString(18, 65);
        assertEquals("Възрастов диапазон: 18 - 65 години", result4, "Should return 'Възрастов диапазон: 18 - 65 години'");
    }

    @Test
    public void testAssignOrganisationPrioritiesToOrganisation_OptionalCategoryAndPriorityCategories_Null() {

        // Invoke the method
        Set<OrganisationPriority> result = utils.assignOrganisationPrioritiesToOrganisation(null, null);

        // Verify the interactions
        verify(organisationPriorityService, never()).getOrganisationPriorityByCategory(anyString());

        // Assert the result
        assertEquals(Collections.emptySet(), result);
    }

    @Test
    public void testAssignOrganisationPrioritiesToOrganisation_OptionalCategory_Null() {
        // Mock the required dependencies
        Set<String> priorityCategories = new HashSet<>(Arrays.asList("category1", "category2"));
        OrganisationPriority existingPriority1 = new OrganisationPriority("category1");
        OrganisationPriority existingPriority2 = new OrganisationPriority("category2");
        when(organisationPriorityService.getOrganisationPriorityByCategory("category1")).thenReturn(existingPriority1);
        when(organisationPriorityService.getOrganisationPriorityByCategory("category2")).thenReturn(existingPriority2);


        // Invoke the method
        Set<OrganisationPriority> result = utils.assignOrganisationPrioritiesToOrganisation(priorityCategories, null);

        // Verify the interactions
        verify(organisationPriorityService, times(1)).getOrganisationPriorityByCategory("category1");
        verify(organisationPriorityService, times(1)).getOrganisationPriorityByCategory("category2");

        // Assert the result
        assertEquals(2, result.size());
        assertTrue(result.contains(existingPriority1));
        assertTrue(result.contains(existingPriority2));
    }

    @Test
    public void testConvertListOfOrganisationPrioritiesToString() {
        // Create a mock for the OrganisationPriority
        OrganisationPriority priority1 = mock(OrganisationPriority.class);
        OrganisationPriority priority2 = mock(OrganisationPriority.class);
        OrganisationPriority priority3 = mock(OrganisationPriority.class);

        // Set the behavior of the mock objects
        when(priority1.getCategory()).thenReturn("Category1");
        when(priority2.getCategory()).thenReturn("Category2");
        when(priority3.getCategory()).thenReturn("Category3");

        // Create a HashSet of mock OrganisationPriority objects
        Set<OrganisationPriority> organisationPriorities = new HashSet<>();
        organisationPriorities.add(priority1);
        organisationPriorities.add(priority2);
        organisationPriorities.add(priority3);

        // Create an instance of the class containing the method to be tested


        // Call the method to be tested
        Set<String> result = utils.convertListOfOrganisationPrioritiesToString(organisationPriorities);

        // Verify the expected behavior
        Set<String> expected = new HashSet<>();
        expected.add("Category1");
        expected.add("Category2");
        expected.add("Category3");

        assertEquals(expected, result);
        // Additional assertions can be added to verify other aspects of the method's behavior if needed
    }

    @Test
    public void testConvertListOfOptionalOrganisationPrioritiesToString() {
        // Mock OrganisationPriority objects
        OrganisationPriority priority1 = mock(OrganisationPriority.class);
        OrganisationPriority priority2 = mock(OrganisationPriority.class);
        when(priority1.getId()).thenReturn(5L); // Assuming some ID greater than staticOrgPrioritiesSize
        when(priority2.getId()).thenReturn(2L); // Assuming some ID less than staticOrgPrioritiesSize

        // Create a Set of OrganisationPriority objects
        Set<OrganisationPriority> orgPriorities = new HashSet<>();
        orgPriorities.add(priority1);
        orgPriorities.add(priority2);

        // Mock the size of staticOrgPrioritiesSize
        int staticOrgPrioritiesSize = 3;



        // Call the method under test
        String result = utils.convertListOfOptionalOrganisationPrioritiesToString(orgPriorities, staticOrgPrioritiesSize);

        // Verify the behavior and the result
        verify(priority1, times(1)).getId();
        verify(priority2, times(1)).getId();
        assertEquals("Priority 1 Category", "Priority 1 Category", result);
    }

    @Test
    public void testCreateOrganisationPriority_NewCategory() {
        // Mock the required dependencies
        when(organisationPriorityService.getOrganisationPriorityByCategory(anyString())).thenReturn(null);


        // Prepare the input data
        String priority = "category1";

        // Invoke the method
        List<OrganisationPriority> result = utils.createOrganisationPriority(priority);

        // Verify the interactions
        verify(organisationPriorityService, times(1)).getOrganisationPriorityByCategory("category1");
        verify(organisationPriorityService, times(1)).saveOrganisationPriority(any(OrganisationPriority.class));

        // Assert the result
        assertEquals(1, result.size());
        assertEquals("category1", result.get(0).getCategory());
    }

    @Test
    public void testCreateOrganisationPriority_ExistingCategory() {
        // Mock the required dependencies
        OrganisationPriority existingPriority = new OrganisationPriority("category1");
        when(organisationPriorityService.getOrganisationPriorityByCategory("category1")).thenReturn(existingPriority);

        // Prepare the input data
        String priority = "category1";

        // Invoke the method
        List<OrganisationPriority> result = utils.createOrganisationPriority(priority);

        // Verify the interactions
        verify(organisationPriorityService, times(1)).getOrganisationPriorityByCategory("category1");
        verify(organisationPriorityService, never()).saveOrganisationPriority(any(OrganisationPriority.class));

        // Assert the result
        assertEquals(1, result.size());
        assertEquals(existingPriority, result.get(0));
    }

    @Test
    public void testCreateOrganisationPriority_MultipleCategories() {
        // Mock the required dependencies
        OrganisationPriority existingPriority = new OrganisationPriority("category1");
        when(organisationPriorityService.getOrganisationPriorityByCategory("category1")).thenReturn(existingPriority);
        when(organisationPriorityService.getOrganisationPriorityByCategory("category2")).thenReturn(null);

        // Prepare the input data
        String priority = "category1,category2";

        // Invoke the method
        List<OrganisationPriority> result = utils.createOrganisationPriority(priority);

        // Verify the interactions
        verify(organisationPriorityService, times(1)).getOrganisationPriorityByCategory("category1");
        verify(organisationPriorityService, times(1)).getOrganisationPriorityByCategory("category2");
        verify(organisationPriorityService, times(1)).saveOrganisationPriority(any(OrganisationPriority.class));

        // Assert the result
        assertEquals(2, result.size());
        assertEquals(existingPriority.getCategory() ,result.get(1).getCategory());
        assertEquals("category1", result.get(1).getCategory());
    }

    @Test
    void testSplitStringByComma() {
        String input = "apple, banana, orange, apple, banana, strawberry";
        List<String> result = utils.splitStringByComma(input);

        Set<String> expected = new HashSet<>(Arrays.asList("apple", "banana", "orange", "strawberry"));
        assertEquals(expected.size(), result.size());
        assertTrue(expected.containsAll(result));
    }

    @Test
    void testSplitStringByComma_EmptyInput() {
        String input = "";

        List<String> result = utils.splitStringByComma(input);

        List<String> expected = List.of();
        assertEquals(expected, result);
    }

    @Test
    void testSplitStringByComma_NullInput() {
        List<String> result = utils.splitStringByComma(null);

        List<String> expected = List.of();
        assertEquals(expected, result);
    }

    @Test
     void testGenerateErrorStringFromMethodArgumentNotValidException() {
        List<ObjectError> globalErrors = new ArrayList<>();
        List<FieldError> fieldErrors = new ArrayList<>();

        globalErrors.add(new ObjectError("object1", "Global error message 1"));
        globalErrors.add(new ObjectError("object2", "Global error message 2"));

        fieldErrors.add(new FieldError("field1", "field1", "Field error message 1"));
        fieldErrors.add(new FieldError("field2", "field2", "Field error message 2"));

        String result = utils.generateErrorStringFromMethodArgumentNotValidException(globalErrors, fieldErrors);

        String expected = "object1: Global error message 1/ object2: Global error message 2/ field1: Field error message 1/ field2: Field error message 2";
        assertEquals(expected, result);
    }

    @Test
     void testGenerateErrorStringFromMethodArgumentNotValidException_EmptyErrors() {
        List<ObjectError> globalErrors = new ArrayList<>();
        List<FieldError> fieldErrors = new ArrayList<>();

        String result = utils.generateErrorStringFromMethodArgumentNotValidException(globalErrors, fieldErrors);
        assertEquals("", result);
    }

    @Test
    void testGenerateErrorStringFromMethodArgumentNotValidException_OnlyGlobalErrors() {
        List<ObjectError> globalErrors = new ArrayList<>();
        List<FieldError> fieldErrors = new ArrayList<>();

        globalErrors.add(new ObjectError("object1", "Global error message 1"));
        globalErrors.add(new ObjectError("object2", "Global error message 2"));
        String result = utils.generateErrorStringFromMethodArgumentNotValidException(globalErrors, fieldErrors);

        String expected = "object1: Global error message 1/ object2: Global error message 2";
        assertEquals(expected, result);
    }

    @Test
     void testGenerateErrorStringFromMethodArgumentNotValidException_OnlyFieldErrors() {
        List<ObjectError> globalErrors = new ArrayList<>();
        List<FieldError> fieldErrors = new ArrayList<>();

        fieldErrors.add(new FieldError("field1", "field1", "Field error message 1"));
        fieldErrors.add(new FieldError("field2", "field2", "Field error message 2"));

        String result = utils.generateErrorStringFromMethodArgumentNotValidException(globalErrors, fieldErrors);

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

