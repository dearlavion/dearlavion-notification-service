package org.example.kafka;

import org.example.kafka.dto.WishEvent;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class WishConsumer {

    @KafkaListener(topics = "wish-event", groupId = "dearlavion-notification-group")
    public void consume(WishEvent event) {

        System.out.println("Received event: " + event);

        // 1. Find matching copilots
        // 2. Send notifications
    }
}
