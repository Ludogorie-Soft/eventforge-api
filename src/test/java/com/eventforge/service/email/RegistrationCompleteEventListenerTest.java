//package com.eventforge.service.email;
//
//import com.eventforge.email.RegistrationCompleteEvent;
//import com.eventforge.email.listener.RegistrationCompleteEventListener;
//import com.eventforge.exception.EmailConfirmationNotSentException;
//import com.eventforge.model.User;
//import com.eventforge.model.VerificationToken;
//import com.eventforge.service.UserService;
//import jakarta.mail.Address;
//import jakarta.mail.Message;
//import jakarta.mail.MessagingException;
//import jakarta.mail.internet.InternetAddress;
//import jakarta.mail.internet.MimeMessage;
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.ArgumentCaptor;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.mail.javamail.JavaMailSender;
//
//import java.io.UnsupportedEncodingException;
//
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.*;
//
//@ExtendWith(MockitoExtension.class)
//public class RegistrationCompleteEventListenerTest {
//    @Mock
//    private UserService userService;
//
//    @Mock
//    private JavaMailSender mailSender;
//
//    @InjectMocks
//    private RegistrationCompleteEventListener listener;
//
//
//    @Test
//    public void onApplicationEvent_EmailSentSuccessfully() throws MessagingException, UnsupportedEncodingException {
//        // Arrange
//        User user = new User();
//        user.setUsername("test@example.com");
//        user.setFullName("John Doe");
//
//        RegistrationCompleteEvent event = new RegistrationCompleteEvent(user, "https://example.com/");
//
//        MimeMessage mockMessage = mock(MimeMessage.class);
//        when(mailSender.createMimeMessage()).thenReturn(mockMessage);
//
//        ArgumentCaptor<Address[]> captor = ArgumentCaptor.forClass(Address[].class);
//
//        // Act
//        listener.onApplicationEvent(event);
//
//        // Assert
//        verify(userService, times(1)).saveUserVerificationToken(any(User.class), anyString());
//        verify(mailSender, times(1)).createMimeMessage();
//        verify(mockMessage, times(1)).setSubject(eq("Потвърждение на акаунт"));
//        verify(mockMessage, times(1)).setFrom(any(InternetAddress.class));
//        verify(mockMessage, times(1)).addRecipients(Message.RecipientType.TO, captor.capture());
//        verify(mailSender, times(1)).send(mockMessage);
//
//        Address[] recipients = captor.getValue();
//        Assertions.assertEquals(1, recipients.length);
//        Assertions.assertEquals(user.getUsername(), ((InternetAddress) recipients[0]).getAddress());
//    }
//
//
//    @Test
//    public void onApplicationEvent_EmailSendingError_ThrowsEmailConfirmationNotSentException() throws MessagingException, UnsupportedEncodingException {
//        // Arrange
//        User user = new User();
//        user.setUsername("test@example.com");
//        user.setFullName("John Doe");
//
//        RegistrationCompleteEvent event = new RegistrationCompleteEvent(user, "https://example.com/");
//
//        MimeMessage mockMessage = mock(MimeMessage.class);
//        when(mailSender.createMimeMessage()).thenReturn(mockMessage);
//        doThrow(new MessagingException("Failed to send email")).when(mailSender).send(mockMessage);
//
//        // Act and Assert
//        Assertions.assertThrows(EmailConfirmationNotSentException.class, () -> {
//            listener.onApplicationEvent(event);
//        });
//
//        verify(userService, times(1)).saveUserVerificationToken(eq(user), anyString());
//        verify(mailSender, times(1)).createMimeMessage();
//        verify(mockMessage, times(1)).setSubject("Потвърждение на акаунт");
//        verify(mockMessage, times(1)).setFrom(any(InternetAddress.class));
//        verify(mockMessage, times(1)).addRecipients(Message.RecipientType.TO, any(Address[].class));
//        verify(mailSender, times(1)).send(mockMessage);
//    }
//}
