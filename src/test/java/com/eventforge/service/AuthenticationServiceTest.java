package com.eventforge.service;

import com.eventforge.dto.request.JWTAuthenticationRequest;
import com.eventforge.dto.request.RegistrationRequest;
import com.eventforge.exception.InvalidCredentialsException;
import com.eventforge.exception.UserDisabledException;
import com.eventforge.exception.UserLockedException;
import com.eventforge.factory.EntityFactory;
import com.eventforge.model.Token;
import com.eventforge.model.User;
import com.eventforge.repository.TokenRepository;
import com.eventforge.security.jwt.JWTService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
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
    void testAuthenticate_ValidCredentials() {
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

}
