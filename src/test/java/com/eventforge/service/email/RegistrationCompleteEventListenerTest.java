package com.eventforge.service.email;

import com.eventforge.email.listener.RegistrationCompleteEventListener;
import com.eventforge.model.User;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.Multipart;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSender;

import java.io.UnsupportedEncodingException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RegistrationCompleteEventListenerTest {
    @Mock
    private JavaMailSender mailSender;

    @InjectMocks
    private RegistrationCompleteEventListener registrationCompleteEventListener;


    @Test
    public void testSendVerificationEmail() throws MessagingException, UnsupportedEncodingException {
        // Create a mock MimeMessage
        MimeMessage mockMimeMessage = mock(MimeMessage.class);

        mockMimeMessage.setFrom(new InternetAddress("sender@example.com" ,"EventForge-Varna"));


        // Mock the JavaMailSender behavior
        when(mailSender.createMimeMessage()).thenReturn(mockMimeMessage);

        // Invoke the sendVerificationEmail method
        String url = "https://example.com/verification";
        User user = new User();
        user.setUsername("example@example.com");
        registrationCompleteEventListener.sendEmail(url, user);

        // Verify that the JavaMailSender's createMimeMessage method was called once
        verify(mailSender, times(1)).createMimeMessage();

        // Verify that the MimeMessage's setters were called with the correct values
        verify(mockMimeMessage).setSubject(eq("Потвърждение на акаунт"));

        // Verify that the MimeMessage's setFrom method was called with the correct value
        verify(mockMimeMessage).setFrom(eq(new InternetAddress("sender@example.com", "EventForge-Varna")));

        // Verify that the MimeMessage's addRecipient method was called with the correct value
        verify(mockMimeMessage).addRecipient(eq(Message.RecipientType.TO), eq(new InternetAddress("example@example.com")));

        // Verify that the MimeMessage's setContent method was called with the correct multipart content
        verify(mockMimeMessage).setContent(any(Multipart.class));

        // Verify that the JavaMailSender's send method was called once with the MimeMessage
        verify(mailSender, times(1)).send(eq(mockMimeMessage));
    }




}
