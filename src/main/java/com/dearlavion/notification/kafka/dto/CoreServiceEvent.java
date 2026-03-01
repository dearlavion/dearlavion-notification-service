package com.dearlavion.notification.kafka.dto;

import lombok.Data;

@Data
public class CoreServiceEvent {
    private EventType type;
    private Object payload;
}
