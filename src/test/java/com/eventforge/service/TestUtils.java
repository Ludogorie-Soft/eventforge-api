package com.eventforge.service;

import com.eventforge.constants.OrganisationPriorityCategory;
import com.eventforge.model.OrganisationPriority;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
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

    private Utils utils;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        utils = new Utils(organisationPriorityService, passwordEncoder);
    }
    @Test
 void testReturnOrderByAscendingByDefaultIfParamNotProvided() {
        String result1 = utils.returnOrderByAscendingByDefaultIfParamNotProvided(null);
        assertEquals("ASC", result1, "Should return 'ASC' when order is null");

        String result2 = utils.returnOrderByAscendingByDefaultIfParamNotProvided("");
        assertEquals("ASC", result2, "Should return 'ASC' when order is empty");

        String result3 = utils.returnOrderByAscendingByDefaultIfParamNotProvided("DESC");
        assertEquals("DESC", result3, "Should return the same order when order is provided");

        String result4 = utils.returnOrderByAscendingByDefaultIfParamNotProvided("ASC");
        assertEquals("ASC", result4, "Should return the same order when order is provided");
    }
    @Test
 void testConvertPriceToString() {
        String result1 = utils.convertPriceToString(0.5);
        assertEquals("безплатно", result1, "Should return 'безплатно' when price is less than 1");

        String result2 = utils.convertPriceToString(1.0);
        assertEquals("1.0 лева", result2, "Should return '1.0 лева' when price is equal to 1");

        String result3 = utils.convertPriceToString(5.99);
        assertEquals("5.99 лева", result3, "Should return the price with 'лева' when price is greater than 1");
    }
    @Test
    void testConvertAgeToString() {
        String result1 = utils.convertAgeToString(0, 0);
        assertEquals("Няма ограничение за възрастта", result1, "Should return 'Няма ограничение за възрастта'");

        String result2 = utils.convertAgeToString(18, 0);
        assertEquals("Минимална възраст: 18 години", result2, "Should return 'Минимална възраст: 18 години'");

        String result3 = utils.convertAgeToString(0, 65);
        assertEquals("Максимална възраст: 65 години", result3, "Should return 'Максимална възраст: 65 години'");

        String result4 = utils.convertAgeToString(18, 65);
        assertEquals("Възрастов диапазон: 18 - 65 години", result4, "Should return 'Възрастов диапазон: 18 - 65 години'");
    }

    @Test
    void testAssignOrganisationPrioritiesToOrganisation_withOptionalCategory() {
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

        Set<OrganisationPriority> result = utils.assignOrganisationPrioritiesToOrganisation(priorityCategories, optionalCategory);

        verify(organisationPriorityService).getOrganisationPriorityByCategory(optionalCategory);
        verify(organisationPriorityService).saveOrganisationPriority(newOrganisationPriority);
        verify(organisationPriorityService).getOrganisationPriorityByCategory("Category1");
        verify(organisationPriorityService).getOrganisationPriorityByCategory("Category2");

        Set<OrganisationPriority> expected = new HashSet<>(Arrays.asList(newOrganisationPriority, category1, category2));
        assertEquals(expected, result);
    }

    @Test
    void testAssignOrganisationPrioritiesToOrganisation_withoutOptionalCategory() {
        Set<String> priorityCategories = new HashSet<>(Arrays.asList("Category1", "Category2"));

        OrganisationPriority category1 = new OrganisationPriority("Category1");
        OrganisationPriority category2 = new OrganisationPriority("Category2");
        when(organisationPriorityService.getOrganisationPriorityByCategory("Category1")).thenReturn(category1);
        when(organisationPriorityService.getOrganisationPriorityByCategory("Category2")).thenReturn(category2);

        Set<OrganisationPriority> result = utils.assignOrganisationPrioritiesToOrganisation(priorityCategories, null);

        verify(organisationPriorityService).getOrganisationPriorityByCategory("Category1");
        verify(organisationPriorityService).getOrganisationPriorityByCategory("Category2");
        verify(organisationPriorityService, never()).saveOrganisationPriority(any(OrganisationPriority.class));

        Set<OrganisationPriority> expected = new HashSet<>(Arrays.asList(category1, category2));
        assertEquals(expected, result);
    }

    @Test
    void testConvertListOfOrganisationPrioritiesToString() {
        OrganisationPriority priority1 = new OrganisationPriority("Category1");
        OrganisationPriority priority2 = new OrganisationPriority("Category2");
        Set<OrganisationPriority> organisationPriorityCategories = new HashSet<>(Arrays.asList(priority1, priority2));

        Set<String> result = utils.convertListOfOrganisationPrioritiesToString(organisationPriorityCategories);

        Set<String> expected = new HashSet<>(Arrays.asList("Category1", "Category2"));
        assertEquals(expected, result);
    }

    @Test
    void testConvertStringListToString() {
        List<String> stringList = Arrays.asList("Apple", "Banana", "Orange");

        String result = utils.convertStringListToString(stringList);

        String expected = "apple,banana,orange";
        assertEquals(expected, result);
    }

    @Test
    void testCreateOrganisationPriority() {
        String priority = "somePriority";
        OrganisationPriority expectedPriority = new OrganisationPriority(priority);

        when(organisationPriorityService.getOrganisationPriorityByCategory(priority)).thenReturn(null);

        OrganisationPriority result = utils.createOrganisationPriority(priority);

        assertEquals(expectedPriority, result);

        verify(organisationPriorityService, times(1)).saveOrganisationPriority(expectedPriority);
    }

    @Test
    void testCreateOrganisationPriority_ExistingPriority() {
        String priority = "existingPriority";

        when(organisationPriorityService.getOrganisationPriorityByCategory(priority)).thenReturn(new OrganisationPriority(priority));
        OrganisationPriority result = utils.createOrganisationPriority(priority);

        assertNull(result);

        verify(organisationPriorityService, never()).saveOrganisationPriority(any());
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

