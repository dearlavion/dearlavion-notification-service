package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.model.NotificationSubscription;
import org.example.service.NotificationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    // Subscribe
    @PostMapping("/subscribe")
    public ResponseEntity<?> subscribe(@RequestParam String userId,
                                       @RequestParam String countryCode,
                                       @RequestParam String city) {
        NotificationSubscription sub = notificationService.subscribe(userId, countryCode, city);
        return ResponseEntity.ok(sub);
    }

    // Unsubscribe
    @PostMapping("/unsubscribe")
    public ResponseEntity<?> unsubscribe(@RequestParam String userId,
                                         @RequestParam String countryCode,
                                         @RequestParam String city) {
        notificationService.unsubscribe(userId, countryCode, city);
        return ResponseEntity.ok(Map.of("message", "Unsubscribed successfully"));
    }

    // Mock trigger WishCreated event
    @PostMapping("/wish-created")
    public ResponseEntity<?> wishCreated(@RequestParam String wishId,
                                         @RequestParam String title,
                                         @RequestParam String countryCode,
                                         @RequestParam String city) {
        notificationService.handleWishCreated(wishId, title, countryCode, city);
        return ResponseEntity.ok(Map.of("message", "Notifications created"));
    }

    // Trigger sending pending notifications (for testing)
    @PostMapping("/send-pending")
    public ResponseEntity<?> sendPending() {
        notificationService.sendPendingNotifications();
        return ResponseEntity.ok(Map.of("message", "Pending notifications sent"));
    }
}

