package com.dearlavion.notification.inApp.dto;

import com.dearlavion.notification.kafka.dto.KafkaEventType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "inApp_notification")
public class InAppNotification {
    @Id
    private String id;
    private String sender;
    private String receiver;
    private RequestType type;
    private String title;
    private String message;
    private boolean read;
    private String link;
    private Instant createdAt;
}
