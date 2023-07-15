package com.eventforge.service;

import com.eventforge.constants.TokenType;
import com.eventforge.exception.InvalidEmailConfirmationLinkException;
import com.eventforge.model.VerificationToken;
import com.eventforge.repository.VerificationTokenRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class EmailVerificationTokenServiceTest {
    @Mock
    private VerificationTokenRepository verificationTokenRepository;
    @InjectMocks
    private EmailVerificationTokenService emailVerificationTokenService;

    @Captor
    private ArgumentCaptor<VerificationToken> verificationTokenCaptor;


    @Test
    public void testGetVerificationTokenByToken_ExistingToken_ReturnsToken() {
        // Arrange
        String tokenValue = "token123";
        VerificationToken expectedToken = new VerificationToken();
        when(verificationTokenRepository.findByToken(tokenValue)).thenReturn(Optional.of(expectedToken));

        // Act
        VerificationToken resultToken = emailVerificationTokenService.getVerificationTokenByToken(tokenValue);

        // Assert
        assertSame(expectedToken, resultToken, "Returned token should be the same as the expected token");
        verify(verificationTokenRepository).findByToken(tokenValue);
    }

    @Test
    public void testGetVerificationTokenByToken_NonExistingToken_ThrowsException() {
        // Arrange
        String tokenValue = "nonExistingToken";
        when(verificationTokenRepository.findByToken(tokenValue)).thenReturn(Optional.empty());

        // Act and Assert
        assertThrows(InvalidEmailConfirmationLinkException.class,
                () -> emailVerificationTokenService.getVerificationTokenByToken(tokenValue),
                "Should throw InvalidEmailConfirmationLinkException for non-existing token");
        verify(verificationTokenRepository).findByToken(tokenValue);
    }



    @Test
    public void testValidateTokenExpirationTime_tokenExpired_confirmEmail() {
        VerificationToken token = new VerificationToken();
        LocalDateTime updatedAt = LocalDateTime.now().minusDays(6); // Assuming token expired 6 days ago
        token.setUpdatedAt(updatedAt);
        token.setType(TokenType.CONFIRM_EMAIL.toString());



        assertThrows(InvalidEmailConfirmationLinkException.class,
                () -> emailVerificationTokenService.validateTokenExpirationTime(token),
                "Линкът за потвърждение на акаунт е изтекъл. Моля, генерирайте нов.");
    }

    @Test
    public void testValidateTokenExpirationTime_tokenExpired_resetPassword() {
        VerificationToken token = new VerificationToken();
        LocalDateTime updatedAt = LocalDateTime.now().minusDays(5); // Assuming token expired 5 days ago
        token.setUpdatedAt(updatedAt);
        token.setType(TokenType.FORGOTTEN_PASSWORD.toString());


        assertThrows(InvalidEmailConfirmationLinkException.class,
                () -> emailVerificationTokenService.validateTokenExpirationTime(token),
                "Expected InvalidEmailConfirmationLinkException with appropriate error message.");
    }

    @Test
    public void testValidateTokenExpirationTime_tokenNotExpired() {
        VerificationToken token = new VerificationToken();
        LocalDateTime updatedAt = LocalDateTime.now().minusDays(1); // Assuming token was updated 1 day ago
        token.setUpdatedAt(updatedAt);
        token.setType(TokenType.CONFIRM_EMAIL.toString());


        // No exception should be thrown as the token is not expired
        emailVerificationTokenService.validateTokenExpirationTime(token);
    }


    @Test
    public void testDeleteVerificationToken() {
        // Arrange
        VerificationToken token = new VerificationToken();

        // Act
        emailVerificationTokenService.deleteVerificationToken(token);

        // Assert
        verify(verificationTokenRepository).delete(token);
    }

    @Test
    public void testSaveVerificationToken() {
        // Arrange
        VerificationToken token = new VerificationToken();

        // Act
        emailVerificationTokenService.saveVerificationToken(token);

        // Assert
        verify(verificationTokenRepository).save(token);
    }
}
