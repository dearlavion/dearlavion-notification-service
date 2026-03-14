package com.dearlavion.notification.kafka.handler;

import com.dearlavion.notification.kafka.dto.EventType;

public interface AuthenticationEventHandler<T> {
    EventType getEventType();   // Which event this handler handles
    Class<T> payloadType();     // Payload class
    void handle(T payload);     // Actual handling logic
}
