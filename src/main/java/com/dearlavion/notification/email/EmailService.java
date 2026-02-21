package com.dearlavion.notification.email;

import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    public void sendWelcomeEmail(String toEmail, String name) {

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("dearlavion@gmail.com");
        message.setTo(toEmail);
        message.setSubject("Welcome to DearLavion 🎉");
        message.setText(
                "Hi " + name + ",\n\n" +
                        "Thank you for registering!\n\n" +
                        "— DearLavion Team"
        );

        mailSender.send(message);
    }

    /*public void sendEmail(String sender, String receiver, String subject, String body) {

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(sender);
        message.setTo(receiver);
        message.setSubject(subject);
        message.setText(body);

        mailSender.send(message);
    }*/

    public void sendEmail(String sender, String receiver, String subject, String body) {

        try {
            MimeMessage message = mailSender.createMimeMessage();

            MimeMessageHelper helper =
                    new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(sender);
            helper.setTo(receiver);
            helper.setSubject(subject);

            // IMPORTANT: second parameter = true enables HTML
            helper.setText(body, true);

            mailSender.send(message);

        } catch (Exception e) {
            throw new RuntimeException("Failed to send email", e);
        }
    }
}

