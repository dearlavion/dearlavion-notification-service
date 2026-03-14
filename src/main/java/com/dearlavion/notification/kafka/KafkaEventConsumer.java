package com.dearlavion.notification.kafka;

import com.dearlavion.notification.channels.ChannelService;
import com.dearlavion.notification.channels.EmailChannelImpl;
import com.dearlavion.notification.email.EmailService;
import com.dearlavion.notification.kafka.dispatcher.AuthenticationEventDispatcher;
import com.dearlavion.notification.kafka.dto.*;
import com.dearlavion.notification.kafka.handler.AuthenticationEventHandler;
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
    private final ChannelService channelService;
    private final ObjectMapper objectMapper;
    private final EmailChannelImpl emailChannel;
    private final AuthenticationEventDispatcher authenticationEventDispatcher;

    @KafkaListener(topics = "core-service-event", groupId = "dearlavion-notification-group")
    public void wishConsumer(KafkaEvent event) {

        if (event.getType() != EventType.NEW_WISH) return;

        WishEvent wishEvent =
            objectMapper.convertValue(event.getPayload(), WishEvent.class);

        System.out.println("Received event: " + wishEvent);

        // 1️⃣ Find matching copilots
        List<CopilotSubscription> matches = matcherService.match(wishEvent);

        // 2️⃣ Send notifications
        for (CopilotSubscription subscriber : matches) {
            channelService.sendWishSubscriptionNotification(subscriber, wishEvent);
        }
    }

    @KafkaListener(topics = "authentication-service-event", groupId = "dearlavion-notification-group")
    public void authenticationEvents(KafkaEvent event) {

        AuthenticationEventHandler handler = authenticationEventDispatcher.getHandler(event.getType());
        if (handler == null) return;

        Object payload = objectMapper.convertValue(event.getPayload(), handler.payloadType());
        handler.handle(payload);
    }
}
