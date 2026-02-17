package com.dearlavion.notification.kafka.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WishEvent {
    // --- Event metadata ---
    private String eventId;        // Kafka event id (optional but good practice)
    private String wishId;         // Domain id
    private Instant createdAt;    // When wish was created

    // --- Wisher info ---
    private String userId;
    private String username;

    // --- Wish details ---
    private String title;
    private String description;
    private List<String> categories;

    // --- Location (for geo matching) ---
    private String countryCode;
    private String cityName;
    private Double latitude;
    private Double longitude;

    // --- Payment / rate filters ---
    private Boolean isPaid;
    private String rateType;      // FIXED | HOURLY | NEGOTIABLE
    private BigDecimal amount;
}
