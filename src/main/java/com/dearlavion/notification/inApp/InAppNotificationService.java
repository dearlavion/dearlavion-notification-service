package com.dearlavion.notification.inApp;

import com.dearlavion.notification.inApp.dto.InAppNotification;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InAppNotificationService {

    private final SimpMessagingTemplate messagingTemplate;

    public void sendNotification(String username, InAppNotification notification) {

        System.out.println("Sending notification to: " + username);
        // 🔥 Sends to specific user
        messagingTemplate.convertAndSendToUser(
                username,
                "/queue/notifications",
                notification
        );
    }
}
