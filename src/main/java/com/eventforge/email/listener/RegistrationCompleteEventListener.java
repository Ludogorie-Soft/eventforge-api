package com.eventforge.email.listener;

import com.eventforge.email.RegistrationCompleteEvent;
import com.eventforge.exception.EmailConfirmationNotSentException;
import com.eventforge.model.User;
import com.eventforge.model.VerificationToken;
import com.eventforge.service.UserService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class RegistrationCompleteEventListener implements ApplicationListener<RegistrationCompleteEvent> {



    private final   UserService userService;

    private final JavaMailSender mailSender;
    @Value("${spring.mail.username}")
    private String senderEmail;



    @Override
    public void onApplicationEvent(RegistrationCompleteEvent event) {
        //1. Get the newly registered user
        User theUser = event.getUser();
        //2. Create a verification token for the user
        String verificationToken = UUID.randomUUID().toString();
        //3. Save the verification token for the user
        userService.saveUserVerificationToken(theUser , verificationToken);

        //4. Build the verification url to be sent to the user.
        String url = event.getApplicationUrl()+verificationToken;
        log.info("Линк за потвърждение на регистрация : {} ",url);
        //5. Send  the email.
        try {
            sendVerificationEmail(url , theUser);
        } catch (MessagingException | UnsupportedEncodingException e) {
            throw new EmailConfirmationNotSentException(theUser.getUsername());
        }
    }

    public void sendVerificationEmail(String url , User user) throws MessagingException, UnsupportedEncodingException {
        String subject = "Потвърждение на акаунт";
        String senderName = "EventForge";
        String content = "<p> Привет, "+ user.getFullName()+ ", </p>"+
                "<p>Благодарим Ви за създадената регистрация,"+"" +
                "Моля, посетете долният линк за да потвърдите регистрацията си.</p>"+
                "<a href=\"" +url+ "\">Потвърждаване на електронна поща за активиране на акаунт</a>"+
                "<p> Благодарим <br> ЧАЙНА ТАААУННН";
        MimeMessage message =mailSender.createMimeMessage();
        var messageHelper = new MimeMessageHelper(message ,MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, StandardCharsets.UTF_8.name());
        messageHelper.setFrom(senderEmail ,senderName);
        messageHelper.setTo(user.getUsername());
        messageHelper.setSubject(subject);
        messageHelper.setText(content , true);
        mailSender.send(message);
    }
    public void resendVerificationTokenEmail(User user, String applicationUrl, VerificationToken verificationToken) throws MessagingException, UnsupportedEncodingException {
        String url = applicationUrl+verificationToken.getToken();
        sendVerificationEmail(url , user);
    }

}
