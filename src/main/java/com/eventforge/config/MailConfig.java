package com.eventforge.config;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
public class MailConfig {
    @Value("${spring.mail.username}")
    private String SPRING_MAIL_USERNAME;
    @Value("${spring.mail.password}")
    private String SPRING_MAIL_PASSWORD;

    @Bean
    public JavaMailSender javaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        // Configure the mail properties
        mailSender.setHost("smtp.gmail.com");
        mailSender.setPort(587);
        mailSender.setUsername(SPRING_MAIL_USERNAME);
        mailSender.setPassword(SPRING_MAIL_PASSWORD);

        Properties properties = new Properties();
        properties.put("mail.smtp.auth", true);
        properties.put("mail.smtp.starttls.enable", true);
        mailSender.setJavaMailProperties(properties);


        return mailSender;
    }
}