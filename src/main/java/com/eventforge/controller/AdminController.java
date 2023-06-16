package com.eventforge.controller;

import com.eventforge.service.Impl.EventServiceImpl;
import com.eventforge.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;

    private final EventServiceImpl eventService;

    @PutMapping("/ban-account/{id}/{email}")
    public ResponseEntity<String> banAccountById(@RequestHeader("Authorization")String authHeader , @PathVariable("id")Long id , @PathVariable("email")String email){
        userService.lockAccountById(id);
        return new ResponseEntity<>("Успешно заключихте акаунта на потребител с електронна поща : "+email ,HttpStatus.OK);
    }

    @PutMapping("/unban-account/{id}/{email}")
    public ResponseEntity<String> unbanAccountById(@RequestHeader("Authorization") String authHeader , @PathVariable("id")Long id ,@PathVariable("email")String email){
        userService.unlockAccountById(id);
        return new ResponseEntity<>("Успешно отключихте акаунта на потребител с електронна поща : "+email , HttpStatus.OK);
    }
    @PutMapping("/approve-account/{id}")
    public ResponseEntity<String> approveUserAccount(@RequestHeader("Authorization")String authHeader , @PathVariable("id")Long userId){
        userService.setApproveByAdminToTrue(userId);
        return new ResponseEntity<>("Успешно одобрихте регистрацията!" , HttpStatus.ACCEPTED);
    }

    @DeleteMapping("/delete-event/{id}")
    public ResponseEntity<String> deleteEventByUserId(@RequestHeader("Authorization") String authHeader , @PathVariable("id")Long id){
        eventService.deleteEventById(id);
        return new ResponseEntity<>("Успешно изтрихте събитието" , HttpStatus.OK);
    }

}
