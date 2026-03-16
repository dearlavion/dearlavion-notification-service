package com.dearlavion.notification.kafka.dispatcher;

import com.dearlavion.notification.kafka.dto.EventType;
import com.dearlavion.notification.kafka.handler.EventHandler;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CoreEventDispatcher {
    private final Map<EventType, EventHandler<?>> handlers = new HashMap<>();

    public CoreEventDispatcher(List<EventHandler<?>> handlerList) {
        handlerList.forEach(handler -> handlers.put(handler.getEventType(), handler));
    }

    public EventHandler<?> getHandler(EventType type) {
        return handlers.get(type);
    }
}
