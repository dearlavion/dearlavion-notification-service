package com.dearlavion.notification.kafka;

import com.dearlavion.notification.kafka.dispatcher.AuthenticationEventDispatcher;
import com.dearlavion.notification.kafka.dispatcher.CoreEventDispatcher;
import com.dearlavion.notification.kafka.dto.KafkaEvent;
import com.dearlavion.notification.kafka.handler.EventHandler;
import com.dearlavion.notification.subscription.dto.CopilotSubscription;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class KafkaEventConsumer {
    private final ObjectMapper objectMapper;
    private final AuthenticationEventDispatcher authenticationEventDispatcher;
    private final CoreEventDispatcher coreEventDispatcher;

    @KafkaListener(topics = "core-service-event", groupId = "dearlavion-notification-group")
    public void coreEvents(KafkaEvent event) {
        EventHandler handler = coreEventDispatcher.getHandler(event.getType());
        if (handler == null) return;

        Object payload = objectMapper.convertValue(event.getPayload(), handler.payloadType());
        handler.handle(payload);
    }

    @KafkaListener(topics = "authentication-service-event", groupId = "dearlavion-notification-group")
    public void authenticationEvents(KafkaEvent event) {

        EventHandler handler = authenticationEventDispatcher.getHandler(event.getType());
        if (handler == null) return;

        Object payload = objectMapper.convertValue(event.getPayload(), handler.payloadType());
        handler.handle(payload);
    }
}
