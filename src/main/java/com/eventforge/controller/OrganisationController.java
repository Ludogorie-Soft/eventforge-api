package com.eventforge.controller;

import com.eventforge.dto.request.ChangePasswordRequest;
import com.eventforge.dto.request.EventRequest;
import com.eventforge.dto.request.UpdateAccountRequest;
import com.eventforge.dto.response.OneTimeEventResponse;
import com.eventforge.dto.response.OrganisationResponse;
import com.eventforge.dto.response.RecurrenceEventResponse;
import com.eventforge.dto.response.container.EventResponseContainer;
import com.eventforge.factory.EntityFactory;
import com.eventforge.factory.RequestFactory;
import com.eventforge.model.Event;
import com.eventforge.model.Image;
import com.eventforge.model.Organisation;
import com.eventforge.model.User;
import com.eventforge.repository.ImageRepository;
import com.eventforge.service.Impl.EventServiceImpl;
import com.eventforge.service.Impl.ImageServiceImpl;
import com.eventforge.service.OrganisationService;
import com.eventforge.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

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

    private final ImageServiceImpl imageService;

    private final ImageRepository imageRepository;

    @GetMapping("/show-pictures")
    public ResponseEntity<List<String>> getOrganisationLogoAndCover(@RequestHeader(AUTHORIZATION)String authHeader){
        User user = userService.getLoggedUserByToken(authHeader);
        Organisation org = organisationService.getOrganisationByUserId(user.getId());
        Image logo = imageRepository.findOrganisationLogoByOrgId(org.getId());
        Image cover = imageRepository.findOrganisationCoverPictureByOrgId(org.getId());
        return new ResponseEntity<>(Arrays.asList(logo.getUrl(),cover.getUrl()),HttpStatus.OK);
    }

    @PostMapping("/change-picture")
    public ResponseEntity<String> updateLogo(@RequestHeader(AUTHORIZATION)String authHeader,@RequestParam(value = "logo" , required = false) String logo , @RequestParam(value = "cover" , required = false)String cover){
        User user = userService.getLoggedUserByToken(authHeader);
        Organisation organisation = organisationService.getOrganisationByUserId(user.getId());
        imageService.saveImageToDb(logo, null , null, organisation , null);
        imageService.saveImageToDb(null , cover , null , organisation , null);
            return new ResponseEntity<>("Успешно променихте логото/корицата!" , HttpStatus.OK);
    }
    
//    @PostMapping("/event-picture-upload/{eventId}")
//    public ResponseEntity<String> updateEventPicture(@RequestHeader(AUTHORIZATION) String authHeader,@RequestParam("eventPicture") String eventPicture ,@PathVariable("eventId")Long eventId){
//        Optional<Event> event = eventService.findEventById(eventId);
//        event.ifPresent(value -> imageService.saveImageToDb(null, null, eventPicture, null, value));
//        return new ResponseEntity<>("Успешно променихте снимката на събитието" ,HttpStatus.OK);
//    }

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

    @GetMapping("/show-my-events")
    public ResponseEntity<EventResponseContainer> showAllOrganisationEvents(@RequestHeader(AUTHORIZATION) String authHeader,
                                                                  @RequestParam(value = "oneTimeEventName" ,required = false)String oneTimeEventName,
                                                                  @RequestParam(value = "recurrenceEventName" ,required = false)String recurrenceEventName){
        List<OneTimeEventResponse> oneTimeEventsByName = eventService.getOneTimeEventsByNameByUserId(authHeader , oneTimeEventName);
        List<RecurrenceEventResponse> recurrenceEventsByNames = eventService.getRecurrenceEventByNameByUserId(authHeader , recurrenceEventName);
        EventResponseContainer eventResponseContainer = new EventResponseContainer(oneTimeEventsByName , recurrenceEventsByNames);
        return new ResponseEntity<>(eventResponseContainer , HttpStatus.OK);
    }
    @GetMapping("/create-event")
    public ResponseEntity<EventRequest> getEventRequest(@RequestHeader(AUTHORIZATION)String authHeader){
        return new ResponseEntity<>(new EventRequest() ,HttpStatus.OK);
    }
    @PostMapping("create-event")
    public ResponseEntity<String> submitCreatedEvent(@Valid @RequestBody EventRequest eventRequest, @RequestHeader(AUTHORIZATION) String authHeader) {
        entityFactory.createEvent(eventRequest , authHeader);
        return new ResponseEntity<>("Успешно създадохте събитие", HttpStatus.CREATED);
    }

    @GetMapping("/update-event/{id}")
    public ResponseEntity<EventRequest> getEventToUpdateByIdAndByOrganisation(@RequestHeader(AUTHORIZATION) String authHeader , @PathVariable("id") Long id){
        EventRequest eventRequest = requestFactory.createEventRequestForUpdateOperation(id, authHeader);
        return new ResponseEntity<>(eventRequest , HttpStatus.CREATED);
    }
    @PutMapping("update/{id}")
    public ResponseEntity<String> updateEventByOrganisation(@RequestHeader(AUTHORIZATION)String authHeader  ,@PathVariable("id") Long id,
                                              @Valid @RequestBody EventRequest eventRequest) {
        eventService.updateEvent(id, eventRequest);
        return new ResponseEntity<>("Всички промени са извършени успешно", HttpStatus.OK);
    }

}
