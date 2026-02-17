package org.example.email;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
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
}

