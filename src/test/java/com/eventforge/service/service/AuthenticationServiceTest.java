package com.eventforge.service.service;

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
import com.eventforge.service.AuthenticationService;
import com.eventforge.service.UserService;
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
    @Mock
    private HttpServletResponse response;

    @Mock
    private HttpServletRequest request;
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
//    void testYourMethod() throws Exception {
//        // Arrange
//        String userEmail = "test@example.com";
//        String refreshToken = "refresh_token";
//        User user = new User(); // Create a user object for testing
//        user.setUsername("testUser");
//        user.setRole("ROLE_USER");
//        when(userService.getUserByEmail(userEmail)).thenReturn(user);
//        when(jwtService.validateToken(eq(refreshToken), any(MyUserDetails.class))).thenReturn(true);
//        when(jwtService.getGeneratedToken(user.getUsername())).thenReturn("access_token");
//
//        // Act
//        authenticationService.refreshToken( request, response);
//
//        // Assert
//        verify(jwtService).validateToken(any(String.class), any(MyUserDetails.class));
//        verify(userService).getUserByEmail(user.getUsername());
//        verify(jwtService).getGeneratedToken(user.getUsername());
//        verify(response).getOutputStream(); // Add relevant assertions for the response
//        // Add additional assertions as needed
//    }


}
