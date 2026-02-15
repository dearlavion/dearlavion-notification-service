package org.example.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Document("notification_subscriptions")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotificationSubscription {

    @Id
    private String id;

    private String userId;

    private String countryCode; // e.g. PH
    private String city;        // e.g. Manila

    private boolean active = true;

    private Instant createdAt = Instant.now();
}
