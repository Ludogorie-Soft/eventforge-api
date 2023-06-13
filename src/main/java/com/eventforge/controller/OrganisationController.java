package com.eventforge.controller;

import com.eventforge.dto.request.ChangePasswordRequest;
import com.eventforge.dto.request.EventRequest;
import com.eventforge.dto.request.UpdateAccountRequest;
import com.eventforge.dto.response.container.EventResponseContainer;
import com.eventforge.dto.response.OneTimeEventResponse;
import com.eventforge.dto.response.OrganisationResponse;
import com.eventforge.dto.response.RecurrenceEventResponse;
import com.eventforge.factory.EntityFactory;
import com.eventforge.factory.RequestFactory;
import com.eventforge.service.Impl.EventServiceImpl;
import com.eventforge.service.OrganisationService;
import com.eventforge.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/organisation")
public class OrganisationController {

    private static final String AUTHORIZATION = "Authorization";
    private final UserService userService;
    private final OrganisationService organisationService;
    private final RequestFactory requestFactory;

    private final EntityFactory entityFactory;

    private final EventServiceImpl eventService;

    @GetMapping("/account-update")
    public ResponseEntity<UpdateAccountRequest> updateAccountRequestResponseEntity(@RequestHeader(AUTHORIZATION) String authHeader) {
        return new ResponseEntity<>(requestFactory.createUpdateAccountRequest(authHeader), HttpStatus.OK);
    }

    @PutMapping("/update")
    public ResponseEntity<String> updateAccount(@RequestHeader(AUTHORIZATION) String authHeader, @Valid @RequestBody UpdateAccountRequest request) {
        organisationService.updateOrganisation(request, authHeader);
        return new ResponseEntity<>("Успешно обновихте аканта си.", HttpStatus.OK);
    }

    @PutMapping("/update-password")
    public ResponseEntity<String> changePassword(@RequestHeader(AUTHORIZATION) String token, @Valid @RequestBody ChangePasswordRequest request) {
        return new ResponseEntity<>(userService.changeAccountPassword(token, request), HttpStatus.OK);
    }
    @GetMapping("/{organisationId}")
    public ResponseEntity<OrganisationResponse> getOrganisation(@PathVariable("organisationId") Long uuid) {
        return ResponseEntity.ok(organisationService.getOrganisationById(uuid));
    }

    @GetMapping("/getOrgByName/{name}")
    public ResponseEntity<OrganisationResponse> getOrganisationByName(@PathVariable("name") String name) {
        return ResponseEntity.ok(organisationService.getOrgByName(name));
    }

    @GetMapping("/showAllEvents")
    public ResponseEntity<EventResponseContainer> getAllEventsByOrganisation(@RequestHeader(AUTHORIZATION) String authHeader) {
        List<OneTimeEventResponse> oneTimeEvents = eventService.getAllOneTimeEventsByUserId(authHeader);
        List<RecurrenceEventResponse> recurrenceEvents = eventService.getAllRecurrenceEventsByUserId(authHeader);
        EventResponseContainer eventResponseContainer = new EventResponseContainer(oneTimeEvents, recurrenceEvents);
        return new ResponseEntity<>(eventResponseContainer, HttpStatus.OK);
    }

    @GetMapping("/getEventsByName")
    public ResponseEntity<EventResponseContainer> getEventsByNameAndByOrganisation(@RequestHeader(AUTHORIZATION) String authHeader,
                                                                  @RequestParam(value = "oneTimeEventName" ,required = false)String oneTimeEventName,
                                                                  @RequestParam(value = "recurrenceEventName" ,required = false)String recurrenceEventName){
        List<OneTimeEventResponse> oneTimeEventsByName = eventService.getOneTimeEventsByNameByUserId(authHeader , oneTimeEventName);
        List<RecurrenceEventResponse> recurrenceEventsByNames = eventService.getRecurrenceEventByNameByUserId(authHeader , recurrenceEventName);
        EventResponseContainer eventResponseContainer = new EventResponseContainer(oneTimeEventsByName , recurrenceEventsByNames);
        return new ResponseEntity<>(eventResponseContainer , HttpStatus.OK);
    }

    @PostMapping("/create-event")
    public ResponseEntity<String> createEvent(@RequestBody EventRequest eventRequest, @RequestHeader(AUTHORIZATION) String authHeader) {
        entityFactory.createEvent(eventRequest , authHeader);
        return new ResponseEntity<>("Успешно създано събитие", HttpStatus.CREATED);
    }

    @GetMapping("/update-event/{id}")
    public ResponseEntity<EventRequest> getEventToUpdateByIdAndByOrganisation(@RequestHeader(AUTHORIZATION) String authHeader , @PathVariable("id") Long id){
        return new ResponseEntity<>(requestFactory.createEventRequestForUpdateOperation(id) , HttpStatus.CREATED);
    }
    @PutMapping("update/{id}")
    public ResponseEntity<String> updateEventByOrganisation(@PathVariable("id") Long id,
                                              @Valid @RequestBody EventRequest eventRequest) {
        eventService.updateEvent(id, eventRequest);
        return new ResponseEntity<>("Всички промени са извършени успешно", HttpStatus.OK);
    }

    @DeleteMapping("/delete-event/{id}")
    public ResponseEntity<String> deleteEventByUserId(@RequestHeader(AUTHORIZATION) String authHeader , @PathVariable("id")Long id){
        eventService.deleteEventById(id);
        return new ResponseEntity<>("Успешно изтрихте събитието" , HttpStatus.OK);
    }
}
