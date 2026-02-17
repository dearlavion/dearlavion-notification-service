package org.example.kafka;

import lombok.RequiredArgsConstructor;
import org.example.email.EmailService;
import org.example.kafka.dto.WishEvent;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WishConsumer {

    private final EmailService emailService;

    @KafkaListener(topics = "wish-event", groupId = "dearlavion-notification-group")
    public void consume(WishEvent event) {

        System.out.println("Received event: " + event);
        emailService.sendWelcomeEmail("ayamalysson@gmail.com", "DEAR");

        // 1. Find matching copilots
        // 2. Send notifications
    }
}
