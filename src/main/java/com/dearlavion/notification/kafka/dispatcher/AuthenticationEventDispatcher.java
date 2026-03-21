package com.dearlavion.notification.kafka.dispatcher;

import com.dearlavion.notification.kafka.dto.KafkaEventType;
import com.dearlavion.notification.kafka.handler.EventHandler;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AuthenticationEventDispatcher {
    private final Map<KafkaEventType, EventHandler<?>> handlers = new HashMap<>();

    public AuthenticationEventDispatcher(List<EventHandler<?>> handlerList) {
        handlerList.forEach(handler -> handlers.put(handler.getEventType(), handler));
    }

    public EventHandler<?> getHandler(KafkaEventType type) {
        return handlers.get(type);
    }
}
