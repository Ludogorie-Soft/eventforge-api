package com.eventforge.security.jwt;

import com.eventforge.dto.AuthenticationResponse;
import com.eventforge.dto.RegistrationRequest;
import com.eventforge.email.CreateApplicationUrl;
import com.eventforge.email.RegistrationCompleteEvent;
import com.eventforge.model.User;
import com.eventforge.model.VerificationToken;
import com.eventforge.repository.VerificationTokenRepository;
import com.eventforge.service.AuthenticationService;
import com.eventforge.service.UserService;
import io.jsonwebtoken.io.IOException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

;


@RequiredArgsConstructor
@RestController
public class JWTController {

    private final JWTService jwtService;
    private final AuthenticationService authenticationService;
    private final ApplicationEventPublisher publisher;
    private final CreateApplicationUrl url;
    private final VerificationTokenRepository verificationTokenRepository;
    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<String> register(
            @RequestBody RegistrationRequest request , final HttpServletRequest httpServletRequest) {
        User user = authenticationService.register(request);
        publisher.publishEvent(new RegistrationCompleteEvent(user ,url.applicationUrl(httpServletRequest)));
        return new ResponseEntity<>("Успешна регистрация. Моля потвърдете имейла си." , HttpStatus.CREATED);
    }
    @GetMapping("/verifyEmail")
    public ResponseEntity<String> verifyEmail(@RequestParam("verificationToken") String verificationToken){
        VerificationToken verifyToken =verificationTokenRepository.findByToken(verificationToken);
        if(verifyToken.getUser().isEnabled()){
            return new ResponseEntity<>("Аканутът е вече потвърден, моля впишете се." , HttpStatus.IM_USED);
        }
        String verificationResult = userService.validateVarificationToken(verificationToken);
        return new ResponseEntity<>(verificationResult,HttpStatus.ACCEPTED);
    }



    @PostMapping("/authenticate")
    public ResponseEntity<String> getTokenForAuthenticatedUser(@RequestBody JWTAuthenticationRequest authRequest){
        AuthenticationResponse authentication = authenticationService.authenticate(authRequest);
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
    public ResponseEntity<String> logout(@RequestHeader("Authorization") String authorization){
        return ResponseEntity.ok("Logged out successfully");
    }


}

