package com.eventforge.email.listener;

import com.eventforge.email.RegistrationCompleteEvent;
import com.eventforge.exception.EmailConfirmationNotSentException;
import com.eventforge.model.User;
import com.eventforge.model.VerificationToken;
import com.eventforge.service.UserService;
import jakarta.activation.DataHandler;
import jakarta.activation.DataSource;
import jakarta.activation.FileDataSource;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class RegistrationCompleteEventListener implements ApplicationListener<RegistrationCompleteEvent> {


    private final UserService userService;

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
        userService.saveUserVerificationToken(theUser, verificationToken);

        //4. Build the verification url to be sent to the user.
        String url = event.getApplicationUrl() + "/auth/verifyEmail?verificationToken=" + verificationToken;
        log.info("Линк за потвърждение на регистрация : {} ", url);

        //5. Send  the email.
        try {
            sendVerificationEmail(url, theUser);
        } catch (MessagingException | UnsupportedEncodingException e) {
            throw new EmailConfirmationNotSentException(theUser.getUsername());
        }
    }

    public void sendVerificationEmail(String url, User user) throws MessagingException, UnsupportedEncodingException {
        String subject = "Потвърждение на акаунт";

        MimeMessage message = mailSender.createMimeMessage();

        message.setSubject(subject);
        message.setFrom(new InternetAddress(senderEmail));
        message.addRecipient(Message.RecipientType.TO, new InternetAddress(user.getUsername()));

        MimeBodyPart messageBodyPart = new MimeBodyPart();

        // Set the HTML content, be sure it references the attachment
        String htmlContent = "<html><body>" +
                "<table style='width:100%; text-align:left;'>" +
                "<tr><td>" +
                "<img src='cid:image' style='max-width:100px;' />" +
                "</td></tr>" +
                "<tr><td>" +
                "<p style='font-size:18px;'>Здравей, " + user.getFullName() + "!</p>" +
                "<p>Благодарим ти за създадената регистрация! Моля, посетете долния линк, за да потвърдите регистрацията си.</p>" +
                "<p><a href='" + url + "'>Потвърждаване на електронна поща за активиране на акаунт</a></p>" +
                "</td></tr>" +
                "<tr><td>" +
                "<p style='font-size:14px;'>Благодарим ти!</p>" +
                "<p style='font-size:14px;'>С най-добри пожелания,<br>" +
                "\uD83D\uDC4B Екипът на Eventforge </p>" +
                "</td></tr>" +
                "</table>" +
                "</body></html>";


        messageBodyPart.setContent(htmlContent, "text/html; charset=utf-8");

        // Create a related multi-part to combine the parts
        MimeMultipart multipart = new MimeMultipart("related");

        // Add body part to multipart
        multipart.addBodyPart(messageBodyPart);

        // Create part for the image
        MimeBodyPart imageBodyPart = new MimeBodyPart();

        // Fetch the image and associate it with the part
        DataSource ds = new FileDataSource("src/main/resources/static/images/img_1.png");
        imageBodyPart.setDataHandler(new DataHandler(ds));
        // Add a header to connect to the HTML
        imageBodyPart.setHeader("Content-ID", "<image>");

        // Add part to multi-part
        multipart.addBodyPart(imageBodyPart);

        // Associate multi-part with message
        message.setContent(multipart);


        mailSender.send(message);
    }


    public void resendVerificationTokenEmail(User user, String applicationUrl, VerificationToken verificationToken) throws MessagingException, UnsupportedEncodingException {
        String url = applicationUrl + "/auth/verifyEmail?verificationToken=" + verificationToken.getToken();
        sendVerificationEmail(url, user);
    }

}
