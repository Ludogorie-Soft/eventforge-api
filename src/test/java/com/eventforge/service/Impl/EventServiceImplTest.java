package com.eventforge.service.Impl;

import com.eventforge.dto.EventResponse;
import com.eventforge.exception.EventRequestException;
import com.eventforge.model.Event;
import com.eventforge.repository.EventRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Root;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EventServiceImplTest {
    @Mock
    private EventRepository eventRepository;
    private EventServiceImpl eventServiceImpl;
    @InjectMocks
    private ModelMapper modelMapper;
    @Mock
    private EntityManager entityManager;
    @Mock
    private CriteriaBuilder criteriaBuilder;
    @Mock
    private CriteriaQuery<Event> criteriaQuery;
    @Mock
    private Root<Event> root;
    @Mock
    private TypedQuery<Event> typedQueryMock;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        eventServiceImpl = new EventServiceImpl(eventRepository, modelMapper, entityManager);
    }

    @Test
    void testShouldGetAllEvents() {
        List<Event> events = List.of(Event.builder().name("number1").build(),
                Event.builder().name("number2").build());

        when(eventRepository.findAll()).thenReturn(events);

        List<EventResponse> result = eventServiceImpl.getAllEvents();

        assertEquals(events.size(), result.size());
    }

    @Test
    void testGetEventByIdShouldExists() {
        Event event = Event.builder().name("number1").build();

        when(eventRepository.findById(event.getId())).thenReturn(Optional.of(event));

        EventResponse result = eventServiceImpl.getEventById(event.getId());
        verifyNoMoreInteractions(eventRepository);

        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("number1");
    }

    @Test
    void testGetEventIfNonExistingIdShouldThrowApiRequestException() {
        UUID eventId = UUID.fromString("8c1dadab-8f53-45ad-8d8e-c136803ffade");
        when(eventRepository.findById(eventId)).thenReturn(Optional.empty());
        assertThrows(EventRequestException.class, () -> eventServiceImpl.getEventById(eventId));

        verify(eventRepository, times(1)).findById(eventId);
        verifyNoMoreInteractions(eventRepository);
    }

    @Test
    void testGetEventWithGivenIdShouldShouldExists() {
        UUID eventId = UUID.fromString("8c1dadab-8f53-45ad-8d8e-c136803ffade");
        Event event = Event.builder().id(eventId).name("number1").build();

        when(eventRepository.findById(eventId)).thenReturn(Optional.of(event));
        EventResponse response = eventServiceImpl.getEventById(eventId);

        verify(eventRepository, times(1)).findById(eventId);
        verifyNoMoreInteractions(eventRepository);

        assertNotNull(response);
        assertEquals("number1", response.getName());
    }


    @Test
    void testFilterEventsByCriteria_AllNull() {

        String name = null;
        String description = null;
        String address = null;
        String organisationName = null;
        String date = null;

        when(entityManager.getCriteriaBuilder()).thenReturn(criteriaBuilder);
        when(criteriaBuilder.createQuery(Event.class)).thenReturn(criteriaQuery);
        when(criteriaQuery.from(Event.class)).thenReturn(root);
        when(entityManager.createQuery(criteriaQuery)).thenReturn(typedQueryMock);

        List<EventResponse> result = eventServiceImpl.filterEventsByCriteria(name, description, address, organisationName, date);

        assertEquals(0, result.size());
    }

    @Test
    void testFilterEventsByCriteria_NameProvided() {

        String name = "Example Name";
        String description = null;
        String address = null;
        String organisationName = null;
        String date = null;

        when(typedQueryMock.getResultList()).thenReturn(List.of(new Event()));

        when(entityManager.getCriteriaBuilder()).thenReturn(criteriaBuilder);
        when(criteriaBuilder.createQuery(Event.class)).thenReturn(criteriaQuery);
        when(criteriaQuery.from(Event.class)).thenReturn(root);
        when(entityManager.createQuery(criteriaQuery)).thenReturn(typedQueryMock);

        List<EventResponse> result = eventServiceImpl.filterEventsByCriteria(name, description, address, organisationName, date);

        assertEquals(1, result.size());
    }

    @Test
    void testFilterEventsByCriteria_NameAndDescriptionProvided() {

        String name = "Example Name";
        String description = "Music";
        String address = null;
        String organisationName = null;
        String date = null;

        when(typedQueryMock.getResultList()).thenReturn(List.of(new Event()));

        when(entityManager.getCriteriaBuilder()).thenReturn(criteriaBuilder);
        when(criteriaBuilder.createQuery(Event.class)).thenReturn(criteriaQuery);
        when(criteriaQuery.from(Event.class)).thenReturn(root);
        when(entityManager.createQuery(criteriaQuery)).thenReturn(typedQueryMock);

        List<EventResponse> result = eventServiceImpl.filterEventsByCriteria(name, description, address, organisationName, date);

        assertEquals(1, result.size());
    }

    @Test
    void testFilterEventsByCriteria_AddressAndStartsAtProvided() {

        String name = null;
        String description = null;
        String address = "Varna";
        String organisationName = null;
        String date = "2023-12-01";

        when(typedQueryMock.getResultList()).thenReturn(List.of(new Event()));

        when(entityManager.getCriteriaBuilder()).thenReturn(criteriaBuilder);
        when(criteriaBuilder.createQuery(Event.class)).thenReturn(criteriaQuery);
        when(criteriaQuery.from(Event.class)).thenReturn(root);
        when(entityManager.createQuery(criteriaQuery)).thenReturn(typedQueryMock);

        List<EventResponse> result = eventServiceImpl.filterEventsByCriteria(name, description, address, organisationName, date);

        assertEquals(1, result.size());
    }

    @Test
    void testFilterEventsByCriteria_OrganisationNameProvided() {
        String name = null;
        String description = null;
        String address = null;
        String organisationName = "Example Organisation";
        String date = null;

        when(typedQueryMock.getResultList()).thenReturn(List.of(new Event()));

        when(entityManager.getCriteriaBuilder()).thenReturn(criteriaBuilder);
        when(criteriaBuilder.createQuery(Event.class)).thenReturn(criteriaQuery);
        when(criteriaQuery.from(Event.class)).thenReturn(root);
        when(entityManager.createQuery(criteriaQuery)).thenReturn(typedQueryMock);

        when(root.get("organisation")).thenReturn(mock(Path.class));
        when(root.get("organisation").get("name")).thenReturn(mock(Path.class));

        List<EventResponse> result = eventServiceImpl.filterEventsByCriteria(name, description, address, organisationName, date);

        assertEquals(1, result.size());
    }
}