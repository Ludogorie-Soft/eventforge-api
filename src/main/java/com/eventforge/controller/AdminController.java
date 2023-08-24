package com.eventforge.controller;

import com.eventforge.dto.request.ChangePasswordRequest;
import com.eventforge.dto.response.CommonEventResponse;
import com.eventforge.dto.response.OrganisationResponse;
import com.eventforge.dto.response.OrganisationResponseForAdmin;
import com.eventforge.email.AdminContactEvent;
import com.eventforge.email.RegistrationCompleteEvent;
import com.eventforge.model.Contact;
import com.eventforge.repository.ContactRepository;
import com.eventforge.service.EventService;
import com.eventforge.service.OrganisationService;
import com.eventforge.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;

    private final OrganisationService organisationService;

    private final EventService eventService;

    private final ContactRepository contactRepository;

    private final ApplicationEventPublisher publisher;

    @GetMapping("/settings")
    public ResponseEntity<ChangePasswordRequest> adminSettings(@RequestHeader("Authorization")String authHeader){
        ChangePasswordRequest changePasswordRequest = new ChangePasswordRequest();
        return new ResponseEntity<>(changePasswordRequest,HttpStatus.OK);
    }

    @PutMapping("update-profile")
    public ResponseEntity<String> updateAdminProfile(@RequestHeader("Authorization")String authHeader , @Validated @RequestBody ChangePasswordRequest passwordRequest){
        String result = userService.changeAccountPassword(authHeader , passwordRequest);
        return new ResponseEntity<>(result ,HttpStatus.OK);
    }
    @GetMapping("/organisation-management")
    public ResponseEntity<List<OrganisationResponseForAdmin>> getAllOrganisationsForAdminByApprovedOrNot(@RequestHeader("Authorization")String authHeader ){
        return new ResponseEntity<>(organisationService.getAllOrganisationsForAdminByApprovedOrNot() ,HttpStatus.OK);
    }
    @GetMapping("/organisation/details/{id}")
    public ResponseEntity<OrganisationResponse> showOrganisationDetailsForAdmin(@RequestHeader("Authorization")String authHeader ,@PathVariable("id")Long orgId){
        return new ResponseEntity<>(organisationService.getOrganisationDetailsByIdWithoutCondition(orgId) , HttpStatus.OK);
    }

    @GetMapping("/event/details/{id}")
    public ResponseEntity<CommonEventResponse>showEventDetailsForAdmin(@RequestHeader("Authorization")String authHeader ,@PathVariable("id")Long eventId){
        return new ResponseEntity<>(eventService.getEventDetailsWithoutConditionsById(eventId) , HttpStatus.OK);
    }
    @PutMapping("/organisation-management/ban-account/{id}/{email}")
    public ResponseEntity<String> banAccountById(@RequestHeader("Authorization")String authHeader , @PathVariable("id")Long id , @PathVariable("email")String email){
        userService.lockAccountById(id);
        return new ResponseEntity<>("Наложихте забрана на потребител с електронна поща:  "+email ,HttpStatus.OK);
    }

    @PutMapping("/organisation-management/unban-account/{id}/{email}")
    public ResponseEntity<String> unbanAccountById(@RequestHeader("Authorization") String authHeader , @PathVariable("id")Long id ,@PathVariable("email")String email){
        userService.unlockAccountById(id);
        return new ResponseEntity<>("Премахнахте забраната на потребител с електронна поща : "+email , HttpStatus.OK);
    }
    @PutMapping("/organisation-management/approve-account/{id}")
    public ResponseEntity<String> approveUserAccount(@RequestHeader("Authorization")String authHeader , @PathVariable("id")Long userId , @RequestParam("email")String email){
        userService.setApproveByAdminToTrue(userId);
        return new ResponseEntity<>("Одобрихте потребител с електронна поща: "+email , HttpStatus.ACCEPTED);
    }

    @DeleteMapping("delete-event/{id}")
    public ResponseEntity<String> deleteEventById(@RequestHeader("Authorization")String authHeader ,@PathVariable("id")Long eventId){
        eventService.deleteEventByIdForAdmin(eventId);
        return new ResponseEntity<>("Успешно изтрихте събитие с номер " +eventId ,HttpStatus.OK);
    }

    @GetMapping("/contacts")
    public ResponseEntity<List<Contact>> contacts(@RequestHeader("Authorization")String authHeader){
        return new ResponseEntity<>(contactRepository.findAll() , HttpStatus.OK);
    }

    @PostMapping("/contact/send-email/{id}")
    public ResponseEntity<String> sendEmail(@RequestHeader("Authorization")String authHeader,@PathVariable("id")Long id ,@RequestParam("answer")String answer){
        publisher.publishEvent(new AdminContactEvent(id,answer));
        return new ResponseEntity<>("Изпратихте успешно отговора",HttpStatus.OK);
    }
    @DeleteMapping("/delete-contact/{id}")
    public ResponseEntity<Void> deleteContact(@RequestHeader("Authorization")String authHeader ,@PathVariable("id")Long contactId){
        contactRepository.deleteById(contactId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
