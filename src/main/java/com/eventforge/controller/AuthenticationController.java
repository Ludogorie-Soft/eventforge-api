package com.eventforge.controller;

import com.eventforge.dto.request.JWTAuthenticationRequest;
import com.eventforge.dto.request.RegistrationRequest;
import com.eventforge.dto.response.AuthenticationResponse;
import com.eventforge.email.ForgottenPasswordEvent;
import com.eventforge.email.RegistrationCompleteEvent;
import com.eventforge.email.listener.RegistrationCompleteEventListener;
import com.eventforge.model.User;
import com.eventforge.model.VerificationToken;
import com.eventforge.service.AuthenticationService;
import com.eventforge.service.EmailVerificationTokenService;
import com.eventforge.service.OrganisationPriorityService;
import com.eventforge.service.UserService;
import io.jsonwebtoken.io.IOException;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.util.Set;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthenticationController {

    private final AuthenticationService authenticationService;
    private final ApplicationEventPublisher publisher;
    private final EmailVerificationTokenService emailVerificationTokenService;
    private final UserService userService;
    private final RegistrationCompleteEventListener eventListener;
    private final OrganisationPriorityService organisationPriorityService;

    @GetMapping("/getAllPriorityCategories")
    public ResponseEntity<Set<String>> getAllPriorityCategories() {
        return new ResponseEntity<>(organisationPriorityService.getAllPriorityCategories(), HttpStatus.OK);
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@Valid @RequestBody RegistrationRequest request,@RequestParam("appUrl") String appUrl) {
        User user = authenticationService.register(request);
        publisher.publishEvent(new RegistrationCompleteEvent(user,null ,appUrl));
        return new ResponseEntity<>("Успешна регистрация. Моля потвърдете имейла си.", HttpStatus.CREATED);
    }

    @GetMapping("/verifyEmail")
    public ResponseEntity<String> verifyEmail(@RequestParam("verificationToken") String verificationToken)  {
        String verificationResult = userService.updateUserIsEnabledFieldAfterConfirmedEmail(verificationToken);
        return new ResponseEntity<>(verificationResult ,HttpStatus.OK);
    }

    @PostMapping("/resend-verification-token")
    public String resendVerificationToken(@RequestParam(value = "email" ,required = false) String email,@RequestParam("appUrl")String appUrl)  {

        publisher.publishEvent(new RegistrationCompleteEvent(null , email , appUrl));

        return "Изпратихме Ви нов линк за потвърждение на профил. Моля посетете електронната си поща";
    }


    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> getTokenForAuthenticatedUser(@RequestBody JWTAuthenticationRequest authRequest) {
        AuthenticationResponse authenticationResponse = authenticationService.authenticate(authRequest);
        return ResponseEntity.ok().body(authenticationResponse);
    }

    @PostMapping("/forgotten/password")
    public ResponseEntity<String> forgottenPassword(@RequestParam("email")String email ,@RequestParam("appUrl")String appUrl){
        publisher.publishEvent(new ForgottenPasswordEvent(email ,appUrl , null));
        return new ResponseEntity<>("Изпратихме Ви имейл за смяна на парола.Моля посете електронната си поща" , HttpStatus.OK);
    }

    @GetMapping("/reset/password")
    public void resetPassword(@RequestParam("verificationToken")String verificationToken){
        VerificationToken token = emailVerificationTokenService.getVerificationTokenByToken(verificationToken);
        User user = token.getUser();
        String newGeneratedPassword = userService.generateNewRandomPasswordForUserViaVerificationToken(token ,user);
        publisher.publishEvent(new ForgottenPasswordEvent(user.getUsername() ,null ,newGeneratedPassword));

    }


    @PostMapping("/refresh-token")
    public void refreshToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException, java.io.IOException {
        authenticationService.refreshToken(request, response);
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(@RequestHeader("Authorization") String authorization) {

        return ResponseEntity.ok("Успещно се отписахте");
    }
}
