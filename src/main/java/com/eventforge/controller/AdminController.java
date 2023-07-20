package com.eventforge.controller;

import com.eventforge.dto.request.ChangePasswordRequest;
import com.eventforge.dto.response.CommonEventResponse;
import com.eventforge.dto.response.OrganisationResponse;
import com.eventforge.dto.response.OrganisationResponseForAdmin;
import com.eventforge.model.User;
import com.eventforge.service.EventService;
import com.eventforge.service.OrganisationService;
import com.eventforge.service.UserService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;

    private final OrganisationService organisationService;

    private final EventService eventService;

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
        return new ResponseEntity<>("Успешно заключихте акаунта на потребител с електронна поща : "+email ,HttpStatus.OK);
    }

    @PutMapping("/organisation-management/unban-account/{id}/{email}")
    public ResponseEntity<String> unbanAccountById(@RequestHeader("Authorization") String authHeader , @PathVariable("id")Long id ,@PathVariable("email")String email){
        userService.unlockAccountById(id);
        return new ResponseEntity<>("Успешно отключихте акаунта на потребител с електронна поща : "+email , HttpStatus.OK);
    }
    @PutMapping("/organisation-management/approve-account/{id}")
    public ResponseEntity<String> approveUserAccount(@RequestHeader("Authorization")String authHeader , @PathVariable("id")Long userId , @RequestParam("email")String email){
        userService.setApproveByAdminToTrue(userId);
        return new ResponseEntity<>("Успешно одобрихте регистрацията на акаунт с електронна поща: "+email , HttpStatus.ACCEPTED);
    }
}
