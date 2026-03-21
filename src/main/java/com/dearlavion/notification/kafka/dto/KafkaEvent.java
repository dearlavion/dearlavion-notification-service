package com.dearlavion.notification.kafka.dto;

import lombok.Data;

@Data
public class KafkaEvent {
    private KafkaEventType type;
    private Object payload;
}
