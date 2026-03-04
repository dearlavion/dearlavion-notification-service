package com.dearlavion.notification.kafka.dto;

import lombok.Data;

@Data
public class KafkaEvent {
    private EventType type;
    private Object payload;
}
