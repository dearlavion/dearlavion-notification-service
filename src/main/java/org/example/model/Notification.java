package org.example.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.List;
import java.util.Map;

@Document("notifications")
public class Notification {

    @Id
    private String id; // UUID

    private String userId; // recipient

    private String wishId; // source of the notification
    private String eventType; // e.g. WishCreated

    private List<String> channels; // EMAIL, PUSH, SMS, INAPP

    private NotificationStatus status = NotificationStatus.PENDING; // PENDING, SENT, FAILED

    private Map<String, Object> payload; // flexible, e.g., wish title, location, etc.

    private Instant createdAt = Instant.now();
    private Instant sentAt; // set when sent
    private int retryCount = 0; // retries attempted
}

