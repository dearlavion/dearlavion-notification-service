package com.dearlavion.notification.subscription.dto;

import lombok.Data;
import org.springframework.data.annotation.Id;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Data
public abstract class BaseSubscription {

    @Id
    private String id;

    private SubscriptionType type;

    private String username;

    private List<String> notificationChannels;

    private Boolean isActive;

    private Instant createdAt;

    private BigDecimal minAmount;
}

