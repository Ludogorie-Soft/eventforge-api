package com.eventforge.service.Impl;

import com.eventforge.dto.EventRequest;
import com.eventforge.dto.EventResponse;
import com.eventforge.exception.EventRequestException;
import com.eventforge.model.Event;
import com.eventforge.model.Organisation;
import com.eventforge.model.User;
import com.eventforge.repository.EventRepository;
import com.eventforge.repository.OrganisationRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
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
    @Mock
    private OrganisationRepository organisationRepository;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        modelMapper = new ModelMapper();
        eventServiceImpl = new EventServiceImpl(eventRepository, organisationRepository, modelMapper, entityManager);
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
    void testGetEventIfNonExistingIdShouldThrowException() {
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
void testGetEventIfNonExistingItShouldThrowException() {
    String eventName = "number1";
    Event event = Event.builder().name(eventName).build();
    when(eventRepository.findByName(eventName)).thenReturn(Optional.empty());

    assertThatThrownBy(() -> eventServiceImpl.getEventByName(eventName))
            .isInstanceOf(EventRequestException.class)
            .hasMessageContaining("Събитие с име number1 не е намерено!");

    verify(eventRepository, times(1)).findByName(eventName);
    verifyNoMoreInteractions(eventRepository);
}
    @Test
    void testSaveEvent() {
        EventRequest eventRequest = EventRequest.builder().name("Event Name").build();
        Event event = eventServiceImpl.mapEventRequestToEvent(eventRequest);

        EventResponse expectedResponse = EventResponse.builder().name(eventRequest.getName()).build();
        when(eventRepository.save(any(Event.class))).thenReturn(event);

        EventResponse result = eventServiceImpl.saveEvent(eventRequest);
        verify(eventRepository).save(event);

        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo(expectedResponse.getName());
    }

    @Test
    void testGetEventWithGivenNameShouldShouldExists() {
        UUID eventId = UUID.fromString("8c1dadab-8f53-45ad-8d8e-c136803ffade");
        Event event = Event.builder().id(eventId).name("number1").build();

        when(eventRepository.findByName(event.getName())).thenReturn(Optional.of(event));
        EventResponse response = eventServiceImpl.getEventByName(event.getName());

        verify(eventRepository, times(1)).findByName(event.getName());
        verifyNoMoreInteractions(eventRepository);

        assertNotNull(response);
        assertEquals("number1", response.getName());
    }

    @Test
    void testUpdateEventIfExistsShouldBeUpdated() {
        UUID eventId = UUID.fromString("8c1dadab-8f53-45ad-8d8e-c136803ffade");
        EventRequest eventRequest = EventRequest.builder()
                .name("Updated Event")
                .description("Updated description")
                .address("Updated address")
                .isOnline(true)
                .startsAt(LocalDateTime.now())
                .endsAt(LocalDateTime.now().plusHours(1))
                .eventCategories(List.of("Category1", "Category2"))
                .build();

        Event existingEvent = Event.builder().id(eventId).build();

        when(eventRepository.findById(eventId)).thenReturn(Optional.of(existingEvent));
        when(eventRepository.save(any(Event.class))).thenReturn(existingEvent);

        eventServiceImpl.updateEvent(eventId, eventRequest);

        verify(eventRepository, times(1)).findById(eventId);
        verify(eventRepository, times(1)).save(any(Event.class));

        assertThat(existingEvent.getName()).isEqualTo(eventRequest.getName());
        assertThat(existingEvent.getDescription()).isEqualTo(eventRequest.getDescription());
        assertThat(existingEvent.getAddress()).isEqualTo(eventRequest.getAddress());
        assertThat(existingEvent.isOnline()).isEqualTo(eventRequest.isOnline());
        assertThat(existingEvent.getStartsAt()).isEqualTo(eventRequest.getStartsAt());
        assertThat(existingEvent.getEndsAt()).isEqualTo(eventRequest.getEndsAt());
        assertThat(existingEvent.getEventCategories()).isEqualTo(eventRequest.getEventCategories());
    }

    @Test
    void testUpdateEventThrowsExceptionWhenEventDoesNotExist() {
        UUID eventId = UUID.fromString("8c1dadab-8f53-45ad-8d8e-c136803ffade");
        EventRequest eventRequest = EventRequest.builder()
                .name("Updated Event")
                .description("Updated Event Description")
                .address("Updated Event Address")
                .build();

        when(eventRepository.findById(eventId)).thenReturn(Optional.empty());
        assertThrows(EventRequestException.class, () -> eventServiceImpl.updateEvent(eventId, eventRequest));

        verify(eventRepository, times(1)).findById(eventId);
        verifyNoMoreInteractions(eventRepository);
    }

    @Test
    void testDeleteExistingEventShouldBeDelete() {
        UUID eventId = UUID.fromString("8c1dadab-8f53-45ad-8d8e-c136803ffade");
        eventServiceImpl.deleteEvent(eventId);

        verify(eventRepository, times(1)).deleteById(eventId);
        verifyNoMoreInteractions(eventRepository);
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

        var orgJoin = mock(Join.class);
        var userJoin = mock(Join.class);
        var isNonLockedPath = mock(Path.class);
        Predicate isNonLockedPredicate = mock(Predicate.class);

        orgJoin = mock(Join.class);

        when(root.join("organisation")).thenReturn(orgJoin);
        when(orgJoin.join("user")).thenReturn(userJoin);
        when(userJoin.get("isNonLocked")).thenReturn(isNonLockedPath);
        when(criteriaBuilder.isTrue(isNonLockedPath)).thenReturn(isNonLockedPredicate);

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

        when(entityManager.getCriteriaBuilder()).thenReturn(criteriaBuilder);
        when(criteriaBuilder.createQuery(Event.class)).thenReturn(criteriaQuery);
        when(criteriaQuery.from(Event.class)).thenReturn(root);
        when(entityManager.createQuery(criteriaQuery)).thenReturn(typedQueryMock);

        var orgJoin = mock(Join.class);
        var userJoin = mock(Join.class);
        var isNonLockedPath = mock(Path.class);
        Predicate isNonLockedPredicate = mock(Predicate.class);

        orgJoin = mock(Join.class);

        when(root.join("organisation")).thenReturn(orgJoin);
        when(orgJoin.join("user")).thenReturn(userJoin);
        when(userJoin.get("isNonLocked")).thenReturn(isNonLockedPath);
        when(criteriaBuilder.isTrue(isNonLockedPath)).thenReturn(isNonLockedPredicate);

        List<Event> events = new ArrayList<>();
        events.add(new Event());
        when(typedQueryMock.getResultList()).thenReturn(events);

        List<EventResponse> result = eventServiceImpl.filterEventsByCriteria(name, description, address, organisationName, date);


        assertEquals(1, result.size());
    }

    @Test
    void testFilterEventsByAllCriteriaProvided() {
        String name = "Example Name";
        String description = "null";
        String address = "null";
        String organisationName = "Example Organization";
        String date = "null";

        when(typedQueryMock.getResultList()).thenReturn(List.of(new Event()));

        when(entityManager.getCriteriaBuilder()).thenReturn(criteriaBuilder);
        when(criteriaBuilder.createQuery(Event.class)).thenReturn(criteriaQuery);
        when(criteriaQuery.from(Event.class)).thenReturn(root);
        when(entityManager.createQuery(criteriaQuery)).thenReturn(typedQueryMock);

        Join orgJoin = mock(Join.class);
        Join<Organisation, User> userJoin = mock(Join.class);
        Predicate isNonLockedPredicate = Mockito.<Predicate>mock(Predicate.class);

        when(root.join("organisation")).thenReturn(orgJoin);
        when(orgJoin.join("user")).thenReturn(userJoin);
        when(criteriaBuilder.isTrue(userJoin.get("isNonLocked"))).thenReturn(isNonLockedPredicate);

        when(root.get("organisation")).thenReturn(mock(Path.class));
        when(root.get("organisation").get("name")).thenReturn(mock(Path.class));

        List<EventResponse> result = eventServiceImpl.filterEventsByCriteria(name, description, address, organisationName, date);

        assertEquals(1, result.size());
    }
}