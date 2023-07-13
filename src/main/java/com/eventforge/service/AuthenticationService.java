package com.eventforge.service;

import com.eventforge.constants.TokenType;
import com.eventforge.dto.request.JWTAuthenticationRequest;
import com.eventforge.dto.request.RegistrationRequest;
import com.eventforge.dto.response.AuthenticationResponse;
import com.eventforge.exception.InvalidCredentialsException;
import com.eventforge.exception.UserDisabledException;
import com.eventforge.exception.UserLockedException;
import com.eventforge.factory.EntityFactory;
import com.eventforge.model.Token;
import com.eventforge.model.User;
import com.eventforge.repository.TokenRepository;
import com.eventforge.security.MyUserDetails;
import com.eventforge.security.jwt.JWTService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.io.IOException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.*;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    
    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final EntityFactory entityFactory;
    private final TokenRepository tokenRepository;
    private final JWTService jwtService;


    public User register(RegistrationRequest registrationRequest){
        return entityFactory.createOrganisation(registrationRequest);
    }
    private void saveUserToken(User user, String jwtToken) {
        var token = Token.builder()
                .user(user)
                .tokenValue(jwtToken)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .build();
        tokenRepository.save(token);
    }


    public AuthenticationResponse authenticate(JWTAuthenticationRequest request) {
       try{
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUserName(),
                        request.getPassword()
                )
        );}
       catch (BadCredentialsException ex){
           throw new InvalidCredentialsException();
       } catch (DisabledException ex){
           throw new UserDisabledException();
       } catch (LockedException ex){
           throw new UserLockedException();
       }

        User user = userService.getUserByEmail(request.getUserName());
        var jwtToken = jwtService.getGeneratedToken(user.getUsername());
        var refreshToken = jwtService.generateRefreshToken(user.getUsername());
        saveUserToken(user, jwtToken);
        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .userRole(user.getRole())
                .build();
    }
    public void revokeAllUserTokens(User user) {
        var validUserTokens = tokenRepository.findAllValidTokenByUser(user.getId());
        if (validUserTokens.isEmpty())
            return;
        validUserTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });
        tokenRepository.saveAll(validUserTokens);
    }
    public void refreshToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException, java.io.IOException {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String refreshToken;
        final String userEmail;
        if (authHeader == null ||!authHeader.startsWith("Bearer ")) {
            return;
        }
        refreshToken = authHeader.substring(7);
        userEmail = jwtService.extractUsernameFromToken(refreshToken);
        if (userEmail != null) {

      
         User user = userService.getUserByEmail(userEmail);
         MyUserDetails userDetails = new MyUserDetails(user);
            
          if (jwtService.validateToken(refreshToken, userDetails)) {
                String accessToken = jwtService.getGeneratedToken(user.getUsername());
                revokeAllUserTokens(user);
                saveUserToken(user, accessToken);
                var authResponse = AuthenticationResponse.builder()
                        .accessToken(accessToken)
                        .refreshToken(refreshToken)
                        .userRole(user.getRole())
                        .build();
                new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
            }
        }
    }

}
