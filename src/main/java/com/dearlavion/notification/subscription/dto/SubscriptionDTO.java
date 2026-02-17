package com.dearlavion.notification.subscription.dto;

import lombok.Data;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;

import java.util.List;
import java.util.Map;

@Data
public class SubscriptionDTO {

    private SubscriptionType type;

    private String userId;

    private List<String> notificationChannels;
    private Boolean isActive;

    // GEO / Copilot fields
    private String coverageType;
    private String country;
    private String city;
    private GeoJsonPoint centerLocation;
    private Double radiusKm;
    private List<String> categories;
    private Map<String, Object> filters;
    private Boolean isPaid;

    // Wisher fields
    private List<String> alertTypes;
}

