package com.eventforge.controller;

import com.eventforge.dto.RegistrationRequest;
import com.eventforge.email.CreateApplicationUrl;
import com.eventforge.email.RegistrationCompleteEvent;
import com.eventforge.email.listener.RegistrationCompleteEventListener;
import com.eventforge.model.User;
import com.eventforge.model.VerificationToken;
import com.eventforge.security.jwt.JWTAuthenticationRequest;
import com.eventforge.security.jwt.JWTService;
import com.eventforge.service.AuthenticationService;
import com.eventforge.service.EmailVerificationTokenService;
import com.eventforge.service.OrganisationPriorityService;
import com.eventforge.service.UserService;
import io.jsonwebtoken.io.IOException;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpHeaders;
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

    @GetMapping("/registration")
    public ResponseEntity<Set<String>>registrationForm(){
        return new ResponseEntity<>(organisationPriorityService.getAllPriorityCategories(), HttpStatus.OK);
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(
            @RequestBody RegistrationRequest request, final HttpServletRequest httpServletRequest) {
        User user = authenticationService.register(request);
        publisher.publishEvent(new RegistrationCompleteEvent(user, url.applicationUrl(httpServletRequest)));
        return new ResponseEntity<>("Успешна регистрация. Моля потвърдете имейла си.", HttpStatus.CREATED);
    }

    @GetMapping("/verifyEmail")
    public ResponseEntity<String> verifyEmail(@RequestParam("verificationToken") String verificationToken) {
        String appUrl = url.applicationUrl(servletRequest) + "/auth/resend-verification-token?verificationToken=" + verificationToken;
        VerificationToken verifyToken = emailVerificationTokenService.getVerificationTokenByToken(verificationToken);
        if (verifyToken.getUser().getIsEnabled()) {
            return new ResponseEntity<>("Аканутът е вече потвърден, моля впишете се.", HttpStatus.IM_USED);
        }
        String verificationResult = userService.validateVerificationToken(verificationToken, appUrl);
        return new ResponseEntity<>(verificationResult, HttpStatus.ACCEPTED);
    }

    @GetMapping("/resend-verification-token")
    public String resendVerificationToken(@RequestParam("verificationToken") String verificationToken, final HttpServletRequest request) throws MessagingException, UnsupportedEncodingException {
        VerificationToken verificationTokenDb = userService.generateNewVerificationToken(verificationToken);
        User user = verificationTokenDb.getUser();
        eventListener.resendVerificationTokenEmail(user, url.applicationUrl(request), verificationTokenDb);
        return "test";
    }


    @PostMapping("/authenticate")
    public ResponseEntity<String> getTokenForAuthenticatedUser(@RequestBody JWTAuthenticationRequest authRequest) {
         authenticationService.authenticate(authRequest);
        String token = jwtService.getGeneratedToken(authRequest.getUserName());
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.SET_COOKIE, "sessionToken=" + token + "; Path=/");
        return ResponseEntity.ok().headers(headers).body(token);
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
        return ResponseEntity.ok("Logged out successfully");
    }
}
