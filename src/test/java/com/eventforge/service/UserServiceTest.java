package com.eventforge.service;

import com.eventforge.dto.request.ChangePasswordRequest;
import com.eventforge.exception.InvalidEmailConfirmationLinkException;
import com.eventforge.model.User;
import com.eventforge.model.VerificationToken;
import com.eventforge.repository.UserRepository;
import com.eventforge.security.jwt.JWTService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @InjectMocks
    private UserService userService;
    @Mock
    private UserRepository userRepository;
    @Mock
    private EmailVerificationTokenService emailVerificationTokenService;
    @Mock
    private JWTService jwtService;
    @Mock
    private Utils utils;

    @Test
    void testGetUserByEmail_ExistingEmail() {
        String email = "test@example.com";
        User user = User.builder().username(email).build();
        when(userRepository.findByEmail(email)).thenReturn(user);

        User result = userService.getUserByEmail(email);

        assertEquals(user, result);
        verify(userRepository).findByEmail(email);
    }

    @Test
    void testGetUserByEmail_NonExistingEmail() {
        String email = "nonexisting@example.com";
        when(userRepository.findByEmail(email)).thenReturn(null);

        User result = userService.getUserByEmail(email);

        assertNull(result);
        verify(userRepository).findByEmail(email);
    }

    @Test
    void testGetLoggedUserByToken_ValidToken() {
        String token = "valid-token";
        String username = "test";
        User user = User.builder().username(username).build();

        String extractedTokenFromHeader = "extracted-token";
        when(jwtService.extractTokenValueFromHeader(token)).thenReturn(extractedTokenFromHeader);
        when(jwtService.extractUsernameFromToken(extractedTokenFromHeader)).thenReturn(username);

        when(userRepository.findByEmail(username)).thenReturn(user);

        User result = userService.getLoggedUserByToken(token);

        assertEquals(user, result);
        verify(jwtService).extractTokenValueFromHeader(token);
        verify(jwtService).extractUsernameFromToken(extractedTokenFromHeader);
        verify(userRepository).findByEmail(username);

        verifyNoMoreInteractions(jwtService, userRepository);
    }

    @Test
    void testSaveUserInDb() {
        User user = User.builder().username("ivan").build();
        userService.saveUserInDb(user);

        verify(userRepository).save(user);
    }

    @Test
    void testUpdateUserIsEnabledFieldAfterConfirmedEmail() {
        User user = User.builder().username("ivan").isEnabled(false).build();

        userService.updateUserIsEnabledFieldAfterConfirmedEmail(user);

        assertThat(user.getIsEnabled()).isTrue();

        verify(userRepository).save(user);
    }

    @Test
    void testUpdateUserIsEnabledFieldAfterConfirmedEmail_UserNull() {
        userService.updateUserIsEnabledFieldAfterConfirmedEmail(null);

        verifyNoInteractions(userRepository);
    }

    @Test
    void testSaveUserVerificationToken() {
        User user = User.builder().username("ivan").isEnabled(false).build();
        String token = "abc123";

        userService.saveUserVerificationToken(user, token);

        verify(emailVerificationTokenService).saveVerificationToken(argThat(verificationToken -> verificationToken.getToken().equals(token) && verificationToken.getUser().equals(user)));
    }

    @Test
    void testValidateVerificationToken_ValidToken() {
        String verificationToken = "validToken";
        VerificationToken verificationTokenDb = new VerificationToken();

        Date expirationTime = new Date();
        verificationTokenDb.setExpirationTime(expirationTime);

        when(emailVerificationTokenService.getVerificationTokenByToken(verificationToken))
                .thenReturn(verificationTokenDb);
        userService.validateVerificationToken(verificationToken, "url");

        verify(emailVerificationTokenService).getVerificationTokenByToken(verificationToken);
    }

    @Test
    void testValidateVerificationToken_InvalidToken() {
        String verificationToken = "invalidToken";
        when(emailVerificationTokenService.getVerificationTokenByToken(verificationToken))
                .thenReturn(null);

        assertThatThrownBy(() -> userService.validateVerificationToken(verificationToken, "url"))
                .isInstanceOf(InvalidEmailConfirmationLinkException.class);
        verify(emailVerificationTokenService).getVerificationTokenByToken(verificationToken);
    }

    @Test
    void testChangeAccountPassword_ShouldBeSuccessfullyChanged() {
        String token = "validToken";
        String oldPassword = "oldPassword";
        String newPassword = "newPassword";
        String confirmNewPassword = "newPassword";

        User user = User.builder().username("test").password("encodedPassword").build();
        when(userService.getLoggedUserByToken(token)).thenReturn(user);
        when(utils.isPasswordValid(oldPassword, user.getPassword())).thenReturn(true);
        when(utils.encodePassword(newPassword)).thenReturn("encodedNewPassword");

        String result = userService.changeAccountPassword(token, new ChangePasswordRequest(oldPassword, newPassword, confirmNewPassword));

        verify(utils).encodePassword(newPassword);
        assertThat(result).isEqualTo("Успешно променихте паролата си.");
    }

    @Test
    void testChangeAccountPassword_InvalidToken() {
        String token = "invalidToken";
        String oldPassword = "oldPassword";
        String newPassword = "newPassword";
        String confirmNewPassword = "newPassword";

        when(userService.getLoggedUserByToken(token)).thenReturn(null);
        String result = userService.changeAccountPassword(token, new ChangePasswordRequest(oldPassword, newPassword, confirmNewPassword));

        assertThat(result).isNull();
    }

    @Test
    void testGenerateNewVerificationToken() {
        String oldToken = "oldToken";
        VerificationToken oldVerificationToken = new VerificationToken();

        when(emailVerificationTokenService.getVerificationTokenByToken(oldToken)).thenReturn(oldVerificationToken);

        userService.generateNewVerificationToken(oldToken);

        verify(emailVerificationTokenService).getVerificationTokenByToken(oldToken);
        verify(emailVerificationTokenService).saveVerificationToken(any(VerificationToken.class));
    }
}