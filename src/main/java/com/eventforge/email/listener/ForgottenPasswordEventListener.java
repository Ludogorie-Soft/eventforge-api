package com.eventforge.email.listener;

import com.eventforge.email.ForgottenPasswordEvent;
import com.eventforge.exception.EmailConfirmationNotSentException;
import com.eventforge.model.User;
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
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class ForgottenPasswordEventListener implements ApplicationListener <ForgottenPasswordEvent> {

    private final JavaMailSender mailSender;

    private final UserService userService;

    @Value("${spring.mail.username}")
    private String senderEmail;
    @Override
    public void onApplicationEvent(ForgottenPasswordEvent event) {

        User user = userService.getUserByEmail(event.getEmail());
        if(user==null){
            throw new UsernameNotFoundException("Нямаме регистриран потребител в базата с предоставената от вас електронна поща");
        }
        String verificationToken = UUID.randomUUID().toString();

        userService.saveUserVerificationToken(user , verificationToken);

        String url = event.getApplicationUrl()+verificationToken;
        log.info("Линк за потвърждение на регистрация : {} ",url);

        try {
            sendResetPasswordRequest(url, user , event.getGeneratedPassword());
        } catch (MessagingException | UnsupportedEncodingException e) {
            throw new EmailConfirmationNotSentException(user.getUsername());
        }

    }

    public void sendResetPasswordRequest(String url , User user , String generatedPassword) throws MessagingException, UnsupportedEncodingException {
        String subject = "Забравена парола - Възстановяване на достъпа до профила ви";

        MimeMessage message = mailSender.createMimeMessage();

        message.setSubject(subject);
        message.setFrom(new InternetAddress(senderEmail ,"EventForge-Varna"));
        message.addRecipient(Message.RecipientType.TO, new InternetAddress(user.getUsername()));

        MimeBodyPart messageBodyPart = new MimeBodyPart();

        String htmlContent = "<html><body>" +
                "<table style='width:100%; text-align:left;'>" +
                "<tr><td>" +
                "<img src='cid:image' style='max-width:100px;' />" +
                "</td></tr>" +
                "<tr><td>" +
                "<p style='font-size:18px;'>Здравей, " + user.getFullName() + "!</p>" +
                "<p>Получавате това съобщение, защото сте поискали възстановяване на достъпа до вашия профил.</p>" +
                "<p>Ако не сте поискали това възстановяване, може да игнорирате това съобщение.</p>" +
                "<p>За да възстановите достъпа до профила си и да създадете нова парола, моля, последвайте следния линк:</p>" +
                "<p><a href='" + url + "'>Смяна на забравена парола</a></p>" +
                "</td></tr>" +
                "<tr><td>" +
                "<p style='font-size:14px;'>Благодарим ви!</p>" +
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
}
