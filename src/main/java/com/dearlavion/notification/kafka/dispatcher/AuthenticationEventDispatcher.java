package com.dearlavion.notification.kafka.dispatcher;

import com.dearlavion.notification.kafka.dto.EventType;
import com.dearlavion.notification.kafka.handler.AuthenticationEventHandler;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AuthenticationEventDispatcher {
    private final Map<EventType, AuthenticationEventHandler<?>> handlers = new HashMap<>();

    public AuthenticationEventDispatcher(List<AuthenticationEventHandler<?>> handlerList) {
        handlerList.forEach(handler -> handlers.put(handler.getEventType(), handler));
    }

    public AuthenticationEventHandler<?> getHandler(EventType type) {
        return handlers.get(type);
    }
}
