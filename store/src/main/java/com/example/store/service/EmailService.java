package com.example.store.service;

import com.example.store.dto.MailBody;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private static final Logger log = LoggerFactory.getLogger(EmailService.class);
    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendSimpleMessage(MailBody mailBody) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(mailBody.to());
        message.setSubject(mailBody.subject());
        message.setFrom(fromEmail);
//        log.info("Email service: " + message.getFrom());
        message.setText(mailBody.text());
        try {
            mailSender.send(message);
//            log.info("Email sent successfully to {}", mailBody.to());
        } catch (Exception e) {
            log.error("Authentication email failed: {}",e.getMessage());
            e.printStackTrace();
        }
    }
}
