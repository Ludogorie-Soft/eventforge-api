package com.eventforge.service;

import com.eventforge.dto.AuthenticationResponse;
import com.eventforge.dto.RegistrationRequest;
import com.eventforge.enums.TokenType;
import com.eventforge.exception.GlobalException;
import com.eventforge.factory.OrganisationBuilder;
import com.eventforge.model.Token;
import com.eventforge.model.User;
import com.eventforge.repository.TokenRepository;
import com.eventforge.repository.UserRepository;
import com.eventforge.security.jwt.JWTAuthenticationRequest;
import com.eventforge.security.jwt.JWTService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.io.IOException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final OrganisationBuilder organisationBuilder;
    private final TokenRepository tokenRepository;
    private final JWTService jwtService;

    public User register(RegistrationRequest registrationRequest){
        return organisationBuilder.createOrganisation(registrationRequest);
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
           throw new GlobalException("Невалидна потребителска поща или парола");
       } catch (DisabledException ex){
           throw new GlobalException("Моля потвърдете имейла си");
       }
        var user = userRepository.findByUsername(request.getUserName())
                .orElseThrow();

        var jwtToken = jwtService.getGeneratedToken(user.getUsername());
        var refreshToken = jwtService.generateRefreshToken(user.getUsername());
        saveUserToken(user, jwtToken);
        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .build();
    }
    private void revokeAllUserTokens(User user) {
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
            var user = userRepository.findByUsername(userEmail)
                    .orElseThrow();
            if (jwtService.validateToken(refreshToken, (UserDetails) user)) {
                var accessToken = jwtService.getGeneratedToken(user.getUsername());
                revokeAllUserTokens(user);
                saveUserToken(user, accessToken);
                var authResponse = AuthenticationResponse.builder()
                        .accessToken(accessToken)
                        .refreshToken(refreshToken)
                        .build();
                new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
            }
        }
    }

}
