package com.eventforge.controller;

import com.eventforge.dto.response.OrganisationResponseForAdmin;
import com.eventforge.service.Impl.EventServiceImpl;
import com.eventforge.service.OrganisationService;
import com.eventforge.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;

    private final OrganisationService organisationService;

    private final EventServiceImpl eventService;


    @GetMapping("/organisation-management")
    public ResponseEntity<List<OrganisationResponseForAdmin>> getAllOrganisationsForAdminByApprovedOrNot(@RequestHeader("Authorization")String authHeader , @RequestParam("isApproved") boolean isApproved ){
        return new ResponseEntity<>(organisationService.getAllOrganisationsForAdminByApprovedOrNot(isApproved) ,HttpStatus.OK);
    }

//    @GetMapping("/organisation-management/unapproved-accounts")
//    public ResponseEntity<List<OrganisationResponseForAdmin>> getAllUnapprovedOrganisationsForAdmin(@RequestHeader("Authorization")String authHeader){
//        return new ResponseEntity<>(organisationService.getAllUnapprovedOrganisationForAdmin() , HttpStatus.OK);
//    }
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
    public ResponseEntity<String> approveUserAccount(@RequestHeader("Authorization")String authHeader , @PathVariable("id")Long userId){
        userService.setApproveByAdminToTrue(userId);
        return new ResponseEntity<>("Успешно одобрихте регистрацията!" , HttpStatus.ACCEPTED);
    }
}
