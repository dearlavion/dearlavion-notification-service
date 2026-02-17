package com.dearlavion.notification.kafka;

import com.dearlavion.notification.email.EmailService;
import com.dearlavion.notification.kafka.dto.WishEvent;
import com.dearlavion.notification.subscription.SubscriptionMatcherService;
import com.dearlavion.notification.subscription.dto.CopilotSubscription;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WishConsumer {
    private final SubscriptionMatcherService matcherService;
    private final EmailService emailService;

    @KafkaListener(topics = "wish-event", groupId = "dearlavion-notification-group")
    public void consume(WishEvent wishEvent) {

        System.out.println("Received event: " + wishEvent);

        // 1️⃣ Find matching copilots
        List<CopilotSubscription> matches = matcherService.match(wishEvent);

        // 2️⃣ Send notifications
        for (CopilotSubscription sub : matches) {
            //notificationService.send(sub, wishEvent);
            emailService.sendWelcomeEmail("ayamalysson@gmail.com", "DEAR");

        }
    }
}
