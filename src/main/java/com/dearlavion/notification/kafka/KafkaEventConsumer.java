package com.dearlavion.notification.kafka;

import com.dearlavion.notification.channels.ChannelService;
import com.dearlavion.notification.email.EmailService;
import com.dearlavion.notification.kafka.dto.CoreServiceEvent;
import com.dearlavion.notification.kafka.dto.EventType;
import com.dearlavion.notification.kafka.dto.WishEvent;
import com.dearlavion.notification.subscription.SubscriptionMatcherService;
import com.dearlavion.notification.subscription.dto.CopilotSubscription;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class KafkaEventConsumer {
    private final SubscriptionMatcherService matcherService;
    private final EmailService emailService;
    private final ChannelService channelService;
    private final ObjectMapper objectMapper;


    @KafkaListener(topics = "core-service-event", groupId = "dearlavion-notification-group")
    public void wishConsumer(CoreServiceEvent event) {

        if (event.getType() != EventType.NEW_WISH) return;

        WishEvent wishEvent =
            objectMapper.convertValue(event.getPayload(), WishEvent.class);

        System.out.println("Received event: " + wishEvent);

        // 1️⃣ Find matching copilots
        List<CopilotSubscription> matches = matcherService.match(wishEvent);

        // 2️⃣ Send notifications
        for (CopilotSubscription subscriber : matches) {
            channelService.send(subscriber, wishEvent);
        }
    }
}
