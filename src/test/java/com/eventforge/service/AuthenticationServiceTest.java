package com.eventforge.service;

import com.eventforge.dto.request.JWTAuthenticationRequest;
import com.eventforge.dto.request.RegistrationRequest;
import com.eventforge.dto.response.AuthenticationResponse;
import com.eventforge.exception.InvalidCredentialsException;
import com.eventforge.exception.UserDisabledException;
import com.eventforge.exception.UserLockedException;
import com.eventforge.factory.EntityFactory;
import com.eventforge.model.Event;
import com.eventforge.model.Token;
import com.eventforge.model.User;
import com.eventforge.repository.TokenRepository;
import com.eventforge.security.MyUserDetails;
import com.eventforge.security.jwt.JWTService;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.*;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthenticationServiceTest {
    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private UserService userService;
    @Mock
    private EntityFactory entityFactory;
    @Mock
    private TokenRepository tokenRepository;
    @Mock
    private JWTService jwtService;
    private AuthenticationService authenticationService;

    @BeforeEach
    public void setUp() {
        authenticationService = new AuthenticationService(authenticationManager, userService, entityFactory, tokenRepository, jwtService);
    }

    @Test
    void testRevokeAllUserTokens_NoValidTokens() {
        User user = new User();

        when(tokenRepository.findAllValidTokenByUser(user.getId())).thenReturn(new ArrayList<>());

        authenticationService.revokeAllUserTokens(user);

        verify(tokenRepository).findAllValidTokenByUser(user.getId());
        verifyNoMoreInteractions(tokenRepository);
    }

    @Test
    void testRevokeAllUserTokens_ValidTokensExist() {
        User user = new User();

        Token token1 = new Token();
        Token token2 = new Token();
        List<Token> validUserTokens = List.of(token1, token2);

        when(tokenRepository.findAllValidTokenByUser(user.getId())).thenReturn(validUserTokens);
        authenticationService.revokeAllUserTokens(user);
        verify(tokenRepository).findAllValidTokenByUser(user.getId());
        verify(tokenRepository).saveAll(validUserTokens);
        assertThat(token1.isExpired()).isTrue();
        assertThat(token1.isRevoked()).isTrue();
        assertThat(token2.isExpired()).isTrue();
        assertThat(token2.isRevoked()).isTrue();
    }

    @Test
    void testRegister() {
        RegistrationRequest registrationRequest = new RegistrationRequest();
        User expectedUser = new User();

        when(entityFactory.createOrganisation(registrationRequest)).thenReturn(expectedUser);
        User actualUser = authenticationService.register(registrationRequest);
        verify(entityFactory).createOrganisation(registrationRequest);

        assertThat(actualUser).isEqualTo(expectedUser);
    }

    @Test
    void testSaveUserToken() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        User user = new User();
        String jwtToken = "jwtToken";

        Method saveUserTokenMethod = AuthenticationService.class.getDeclaredMethod("saveUserToken", User.class, String.class);
        saveUserTokenMethod.setAccessible(true);

        saveUserTokenMethod.invoke(authenticationService, user, jwtToken);

        verify(tokenRepository).save(org.mockito.ArgumentMatchers.any(Token.class));
    }

    @Test
    void testAuthenticate_InvalidCredentials() {
        JWTAuthenticationRequest request = new JWTAuthenticationRequest("username", "password");

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new BadCredentialsException("Invalid credentials"));

        assertThrows(InvalidCredentialsException.class, () -> authenticationService.authenticate(request));

        verify(authenticationManager).authenticate(
                new UsernamePasswordAuthenticationToken(request.getUserName(), request.getPassword())
        );
    }

    @Test
    void testAuthenticate_DisabledUser() {
        JWTAuthenticationRequest request = new JWTAuthenticationRequest("username", "password");


        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new DisabledException("User is disabled"));

        assertThrows(UserDisabledException.class, () -> authenticationService.authenticate(request));

        verify(authenticationManager).authenticate(
                new UsernamePasswordAuthenticationToken(request.getUserName(), request.getPassword())
        );
    }

    @Test
    void testAuthenticate_LockedUser() {
        JWTAuthenticationRequest request = new JWTAuthenticationRequest("username", "password");

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new LockedException("User is locked"));

        assertThrows(UserLockedException.class, () -> authenticationService.authenticate(request));

        verify(authenticationManager).authenticate(
                new UsernamePasswordAuthenticationToken(request.getUserName(), request.getPassword())
        );
    }

    @Test
    void testAuthenticate_ValidCredentials() {
        JWTAuthenticationRequest request = new JWTAuthenticationRequest("username", "password");
        User user = mock(User.class);
        user.setUsername(request.getUserName());
        Token token = mock(Token.class);
        String tokenValue = "token";

        when(userService.getUserByEmail(request.getUserName())).thenReturn(user);
        when(jwtService.getGeneratedToken(user.getUsername())).thenReturn(tokenValue);
        when(jwtService.generateRefreshToken(user.getUsername())).thenReturn(tokenValue);
        when(user.getRole()).thenReturn("test"); // Mocking the getRole() method to return "test"

        AuthenticationResponse authenticationResponse = authenticationService.authenticate(request);

        assertEquals(tokenValue, authenticationResponse.getAccessToken());
        assertEquals(tokenValue, authenticationResponse.getRefreshToken());
        assertEquals("test", authenticationResponse.getUserRole());
    }


//    @Test
//    void testRefreshToken_ValidToken() throws IOException {
//        // Mock the HttpServletRequest and HttpServletResponse
//        HttpServletRequest request = mock(HttpServletRequest.class);
//        HttpServletResponse response = mock(HttpServletResponse.class);
//
//        // Mock the necessary method calls and return values
//        String token = "valid-refresh-token";
//        String userEmail = "test@example.com";
//        String userRole = "ROLE_USER";
//        User user = mock(User.class);
//        when(user.getRole()).thenReturn(userRole); // Mocking the getRole() method
//        MyUserDetails userDetails = new MyUserDetails(user);
//        String accessToken = "new-access-token";
//        AuthenticationResponse authResponse = AuthenticationResponse.builder()
//                .accessToken(accessToken)
//                .refreshToken(accessToken)
//                .userRole(userRole)
//                .build();
//
//        when(request.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn(accessToken);
//        when(jwtService.extractUsernameFromToken(accessToken)).thenReturn(userEmail);
//        when(userService.getUserByEmail(userEmail)).thenReturn(user);
//        when(jwtService.validateToken(accessToken, userDetails)).thenReturn(true);
//        when(jwtService.getGeneratedToken(user.getUsername())).thenReturn(accessToken);
//
//        // Capture the response.getOutputStream() call
//        ServletOutputStream outputStream = mock(ServletOutputStream.class);
//        when(response.getOutputStream()).thenReturn(outputStream);
//
//        // Call the method to be tested
//        authenticationService.refreshToken(request, response);
//
//        // Verify the expected method calls and response
//        verify(request).getHeader(HttpHeaders.AUTHORIZATION);
////        verify(jwtService).validateToken(accessToken, userDetails);
//        verify(jwtService, times(1)).validateToken(any(String.class) , userDetails);
//
//
//
//        verify(jwtService).getGeneratedToken(user.getUsername());
//
//        verify(response).getOutputStream();
//
//
//        verify(outputStream).close();
//    }



}
