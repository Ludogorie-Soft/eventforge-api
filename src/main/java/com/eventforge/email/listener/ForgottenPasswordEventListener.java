package com.eventforge.email.listener;

import com.eventforge.constants.TokenType;
import com.eventforge.email.ForgottenPasswordEvent;
import com.eventforge.exception.EmailConfirmationNotSentException;
import com.eventforge.exception.UserDisabledException;
import com.eventforge.exception.UserLockedException;
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
public class ForgottenPasswordEventListener implements ApplicationListener<ForgottenPasswordEvent> {

    private final JavaMailSender mailSender;

    private final UserService userService;

    private static final String CLOSE_TAG_TD_TR = "</td></tr>";
    private static final String OPEN_TAG_TR_TD = "<tr><td>"; //because of Sonar , i had to define this constant..

    @Value("${spring.mail.username}")
    private String senderEmail;

    @Override
    public void onApplicationEvent(ForgottenPasswordEvent event) {

        User user = userService.getUserByEmail(event.getEmail());
        if (user == null) {
            throw new UsernameNotFoundException("Нямаме регистриран потребител в базата с предоставената от вас електронна поща");
        }
        if (!user.getIsNonLocked()) {
            throw new UserLockedException();
        }
        if (!user.getIsEnabled()) {
            throw new UserDisabledException();
        }
        String verificationToken = UUID.randomUUID().toString();
        if (event.getGeneratedPassword() == null) {
            userService.saveUserVerificationToken(user, verificationToken, TokenType.FORGOTTEN_PASSWORD.toString());
        }

        String url = event.getApplicationUrl() + verificationToken;
        log.info("Линк за смяна на парола : {} ", url);

        try {
            sendResetPasswordRequest(url, user, event.getGeneratedPassword());
        } catch (MessagingException | UnsupportedEncodingException e) {
            throw new EmailConfirmationNotSentException(user.getUsername());
        }

    }

    public void sendResetPasswordRequest(String url, User user, String generatedPassword) throws MessagingException, UnsupportedEncodingException {
        String htmlContent;
        String subject;
        if (generatedPassword == null) {
            subject = "Забравена парола - Възстановяване на достъпа до профила ви";
            htmlContent = fetchContentForForgottenPasswordRequest(user.getFullName(), url);
        } else {
            subject = "Нова генерирана парола";
            htmlContent = fetchContentForNewlyRandomGeneratedPassword(generatedPassword);
        }

        MimeMessage message = mailSender.createMimeMessage();

        message.setSubject(subject);
        message.setFrom(new InternetAddress(senderEmail, "Активна Варна"));
        message.setReplyTo(new InternetAddress[]{new InternetAddress("noreply@active-varna.com")});
        message.addRecipient(Message.RecipientType.TO, new InternetAddress(user.getUsername()));

        MimeBodyPart messageBodyPart = new MimeBodyPart();

        messageBodyPart.setContent(htmlContent, "text/html; charset=utf-8");

        // Create a related multi-part to combine the parts
        MimeMultipart multipart = new MimeMultipart("related");

        // Add body part to multipart
        multipart.addBodyPart(messageBodyPart);

        // Associate multi-part with message
        message.setContent(multipart);

        mailSender.send(message);
    }

    private String fetchContentForForgottenPasswordRequest(String userFullName, String url) {
        return "<html><body>" +
                "<table style='width:100%; text-align:left;'>" +
                OPEN_TAG_TR_TD +
                "<p style='font-size:18px;'>Здравей, " + userFullName + "!</p>" +
                "<p>Получавате това съобщение, защото сте поискали възстановяване на достъпа до вашия профил.</p>" +
                "<p>Ако не сте поискали това възстановяване, може да игнорирате това съобщение.</p>" +
                "<p>За да възстановите достъпа до профила си и да генерирате нова парола, моля, последвайте следния линк:</p>" +
                "<p><a href='" + url + "'>Генериране на нова парола</a></p>" +
                CLOSE_TAG_TD_TR +
                OPEN_TAG_TR_TD +
                "<p style='font-size:14px;'>Благодарим ви!</p>" +
                "<p style='font-size:14px;'>С най-добри пожелания,<br>" +
                "\uD83D\uDC4B Екипът на Активна Варна </p>" +
                OPEN_TAG_TR_TD +
                "<p style='font-size:14px; font-weight: bold;'>" +
                "Това е автоматично съобщение, генерирано от нашата система. " +
                "Моля не отговаряйте на този имейл. " +
                "Ако имате въпроси или нужда от помощ, свържете се с нас чрез предоставената контактна форма на сайта." +
                "</p>" +
                CLOSE_TAG_TD_TR +
                "</table>" +
                "</body></html>";
    }

    private String fetchContentForNewlyRandomGeneratedPassword(String generatedPassword) {
        return "<html><body>" +
                "<table style='width:100%; text-align:left;'>" +
                OPEN_TAG_TR_TD +
                "<p style='font-size:18px;'>Нова генерирана парола!</p>" +
                "<p>Получавате това съобщение, защото сте потвърдили възстановяване на достъпа до вашият профил.</p>" +
                "<p>Моля не излагайте публично вашата парола.</p>" +
                "<p>Препоръчваме Ви, веднага след като се впишете, да си смените паролата.</p>" +
                "<p>Нова парола - <span style='font-size:20px; font-weight:bold;'>" + generatedPassword + "</span></p>" +
                CLOSE_TAG_TD_TR +
                OPEN_TAG_TR_TD +
                "<p style='font-size:14px;'>Благодарим ви!</p>" +
                "<p style='font-size:14px;'>С най-добри пожелания,<br>" +
                "\uD83D\uDC4B Екипът на Активна Варна </p>" +
                OPEN_TAG_TR_TD +
                "<p style='font-size:14px; font-weight: bold;'>" +
                "Това е автоматично съобщение, генерирано от нашата система. " +
                "Моля не отговаряйте на този имейл. " +
                "Ако имате въпроси или нужда от помощ, свържете се с нас чрез предоставената контактна форма на сайта." +
                "</p>" +
                CLOSE_TAG_TD_TR +
                "</table>" +
                "</body></html>";
    }
}
