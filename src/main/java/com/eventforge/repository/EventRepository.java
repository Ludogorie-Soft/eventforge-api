package com.eventforge.repository;

import com.eventforge.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {
      String LEGAL_USER_CONDITION ="e.organisation.user.isNonLocked = true AND e.organisation.user.isApprovedByAdmin = true";
      String UNEXPIRED_CONDITION = "e.endsAt >= ?1";
      String EXPIRED_CONDITION = "e.endsAt < ?1";




    // queries accessible for everyone!
    @Query("SELECT e FROM Event e WHERE e.isOneTime = true AND "+LEGAL_USER_CONDITION + " AND e.organisation.id = :orgId ORDER BY e.createdAt ASC")
    List<Event> findAllOneTimeEventsByOrganisationId(Long orgId);
    @Query("SELECT e FROM Event e WHERE e.isOneTime = false AND "+LEGAL_USER_CONDITION +" AND e.organisation.id = :orgId ORDER BY e.createdAt ASC")
    List<Event> findAllRecurrenceEventsByOrganisationId(Long orgId);
    @Query("SELECT e FROM Event e WHERE e.isOneTime = true AND "+ LEGAL_USER_CONDITION+" AND "+UNEXPIRED_CONDITION + " ORDER BY e.startsAt ASC")
    List<Event> findAllActiveOneTimeEvents(LocalDateTime date , String order);

    @Query("SELECT e FROM Event e WHERE e.isOneTime = false AND "+ LEGAL_USER_CONDITION+" AND "+UNEXPIRED_CONDITION + " ORDER BY e.startsAt ASC")
    List<Event> findAllActiveRecurrenceEvents(LocalDateTime date , String order);

    @Query("SELECT e FROM Event e WHERE e.isOneTime = true AND "+LEGAL_USER_CONDITION+ " AND "+EXPIRED_CONDITION + " ORDER BY e.endsAt ASC")
    List<Event> findAllExpiredOneTimeEvents(LocalDateTime passedDate , String order);
    @Query("SELECT e FROM Event e WHERE e.isOneTime = false AND "+LEGAL_USER_CONDITION+" AND "+EXPIRED_CONDITION + "ORDER BY e.endsAt ASC")
    List<Event> findAllExpiredRecurrenceEvents(LocalDateTime passedDate , String order);

    // queries accessible for organisations!
    @Query("SELECT e FROM Event e WHERE e.organisation.user.id = :userId AND e.organisation.user.isNonLocked = true ORDER BY e.startsAt DESC")
    List<Event> findAllEventsForOrganisationByUserId(Long userId);


    @Query("select e from Event e where e.organisation.user.id = :userId AND e.name LIKE %:name% ORDER BY e.startsAt DESC")
    List<Event> findAllEventsForOrganisationByUserIdAndName(Long userId , String name );


    @Query("SELECT e FROM Event e WHERE e.organisation.user.id = :userId AND e.id = :eventId")
    Event findEventByIdAndUserId(Long userId , Long eventId);
}