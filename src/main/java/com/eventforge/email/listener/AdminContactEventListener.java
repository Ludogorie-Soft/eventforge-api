package com.eventforge.email.listener;

import com.eventforge.email.AdminContactEvent;
import com.eventforge.model.Contact;
import com.eventforge.repository.ContactRepository;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;

@Component
@RequiredArgsConstructor
public class AdminContactEventListener implements ApplicationListener<AdminContactEvent> {
    private final ContactRepository contactRepository;

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String senderEmail;

    private static final String [] TR_TD_TAG = {"<tr><td>" , "</td></tr>"};

    @Override
    public void onApplicationEvent(AdminContactEvent event) {
        Contact contact = contactRepository.findById(event.getContactId()).orElse(null);
        String adminAnswer = event.getAdminAnswer();
        try {
            sendEmail(contact, adminAnswer);
            contact.setIsAnswered(true);
            contactRepository.save(contact);
        } catch (MessagingException | UnsupportedEncodingException ignored) {
        }
    }

    public void sendEmail(Contact contact , String adminAnswer) throws MessagingException, UnsupportedEncodingException {
        String subject = String.format("Обратна връзка по тема %s",contact.getSubject());

        MimeMessage message = mailSender.createMimeMessage();
        message.setSubject(subject);
        message.setFrom(new InternetAddress(senderEmail ,"Активна Варна"));
        message.setReplyTo(new InternetAddress[]{new InternetAddress("noreply@active-varna.com")});
        message.addRecipient(Message.RecipientType.TO, new InternetAddress(contact.getEmail()));

        MimeBodyPart messageBodyPart = new MimeBodyPart();

        String htmlContent = "<html><body>" +
                "<table style='width:100%; text-align:left;'>" +
                TR_TD_TAG[0] +
                TR_TD_TAG[1] +
                TR_TD_TAG[0] +
                adminAnswer+
                "\uD83D\uDC4B Екипът на Активна Варна</p>" +
                TR_TD_TAG[1] +
                TR_TD_TAG[0] +
                "<p style='font-size:14px; font-weight: bold;'>" +
                "Моля не отговаряйте на този имейл. " +
                "Ако имате въпроси или нужда от помощ, свържете се с нас чрез подходящия метод за контакт, предоставен от нашата услуга." +
                "</p>" +
                TR_TD_TAG[1] +
                "</table>" +
                "</body></html>";


        messageBodyPart.setContent(htmlContent, "text/html; charset=utf-8");

        // Create a related multi-part to combine the parts
        MimeMultipart multipart = new MimeMultipart("related");

        // Add body part to multipart
        multipart.addBodyPart(messageBodyPart);

        // Associate multi-part with message
        message.setContent(multipart);


        mailSender.send(message);

    }
}
