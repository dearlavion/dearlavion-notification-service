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
    private String id; //wish id
    private String username;

    // --- Wish details ---
    private String title;
    private String countryCode;
    private String cityName;
    private Double latitude;
    private Double longitude;

    // --- Payment / rate filters ---
    private Boolean isPaid;
    private BigDecimal amount;
}
