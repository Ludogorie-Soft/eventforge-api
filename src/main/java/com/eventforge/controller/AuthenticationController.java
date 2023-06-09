package com.eventforge.controller;

import com.eventforge.dto.response.AuthenticationResponse;
import com.eventforge.dto.request.RegistrationRequest;
import com.eventforge.email.CreateApplicationUrl;
import com.eventforge.email.RegistrationCompleteEvent;
import com.eventforge.email.listener.RegistrationCompleteEventListener;
import com.eventforge.model.User;
import com.eventforge.model.VerificationToken;
import com.eventforge.dto.request.JWTAuthenticationRequest;
import com.eventforge.security.jwt.JWTService;
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
    private final JWTService jwtService;
    private final AuthenticationService authenticationService;
    private final ApplicationEventPublisher publisher;
    private final CreateApplicationUrl url;
    private final EmailVerificationTokenService emailVerificationTokenService;
    private final UserService userService;

    private final HttpServletRequest servletRequest;

    private final RegistrationCompleteEventListener eventListener;

    private final OrganisationPriorityService organisationPriorityService;

    @GetMapping("/getAllPriorityCategories")
    public ResponseEntity<Set<String>> getAllPriorityCategories() {
        return new ResponseEntity<>(organisationPriorityService.getAllPriorityCategories(), HttpStatus.OK);
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@Valid @RequestBody RegistrationRequest request,@RequestParam("appUrl") String appUrl) {
        User user = authenticationService.register(request);
        publisher.publishEvent(new RegistrationCompleteEvent(user, appUrl));
        return new ResponseEntity<>("Успешна регистрация. Моля потвърдете имейла си.", HttpStatus.CREATED);
    }

    @GetMapping("/verifyEmail")
    public ResponseEntity<String> verifyEmail(@RequestParam("verificationToken") String verificationToken ,@RequestParam("appUrl")String appUrl)  {
        VerificationToken verifyToken = emailVerificationTokenService.getVerificationTokenByToken(verificationToken);
        String redirectUrl = "";
        if(verifyToken!=null){
            if (Boolean.TRUE.equals(verifyToken.getUser().getIsEnabled())) {
                return new ResponseEntity<>("Акантът Ви е вече потвърден. Моля впишете се." , HttpStatus.ALREADY_REPORTED);
            }
        }

        String verificationResult = userService.validateVerificationToken(verificationToken, appUrl);
        return new ResponseEntity<>(verificationResult ,HttpStatus.OK);
    }

    @GetMapping("/resend-verification-token")
    public String resendVerificationToken(@RequestParam("verificationToken") String verificationToken,@RequestParam("appUrl")String appUrl) throws MessagingException, UnsupportedEncodingException {
        VerificationToken verificationTokenDb = userService.generateNewVerificationToken(verificationToken);
        User user = verificationTokenDb.getUser();
        eventListener.resendVerificationTokenEmail(user, appUrl, verificationTokenDb);
        return "Пратихме Ви нов линк за потвърждение. Моля посетете електронната си поща.";
    }


    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> getTokenForAuthenticatedUser(@RequestBody JWTAuthenticationRequest authRequest) {
        AuthenticationResponse authenticationResponse = authenticationService.authenticate(authRequest);
        return ResponseEntity.ok().body(authenticationResponse);
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
