package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.model.Notification;
import org.example.model.NotificationSubscription;
import org.example.repository.NotificationRepository;
import org.example.repository.NotificationSubscriptionRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationSubscriptionRepository subscriptionRepository;
    private final NotificationRepository notificationRepository;

    // Subscribe user
    public NotificationSubscription subscribe(String userId, String countryCode, String city) {
        NotificationSubscription sub = new NotificationSubscription();
        sub.setUserId(userId);
        sub.setCountryCode(countryCode.toUpperCase());
        sub.setCity(city);
        sub.setActive(true);
        sub.setCreatedAt(Instant.now());
        return subscriptionRepository.save(sub);
    }

    // Unsubscribe user
    public void unsubscribe(String userId, String countryCode, String city) {
        List<NotificationSubscription> subs = subscriptionRepository
                .findByCountryCodeAndCityAndActive(countryCode.toUpperCase(), city, true)
                .stream()
                .filter(s -> s.getUserId().equals(userId))
                .toList();

        subs.forEach(s -> {
            s.setActive(false);
            subscriptionRepository.save(s);
        });
    }

    // Create notifications from WishCreated event
    public void handleWishCreated(String wishId, String title, String countryCode, String city) {
        List<NotificationSubscription> subscribers = subscriptionRepository
                .findByCountryCodeAndCityAndActive(countryCode.toUpperCase(), city, true);

        for (NotificationSubscription sub : subscribers) {
            Notification notification = new Notification();
            notification.setUserId(sub.getUserId());
            notification.setWishId(wishId);
            notification.setEventType("WishCreated");
            notification.setPayload(Map.of(
                    "wishId", wishId,
                    "title", title,
                    "countryCode", countryCode,
                    "city", city
            ));
            notificationRepository.save(notification);
        }
    }

    // Mock sending notifications (later can integrate real email/push)
    public void sendPendingNotifications() {
        List<Notification> pending = notificationRepository.findByStatus(NotificationStatus.PENDING);
        for (Notification notif : pending) {
            try {
                // Here you would call email/push/sms services
                System.out.println("Sending notification to user: " + notif.getUserId()
                        + " for wish: " + notif.getWishId());

                notif.setStatus(NotificationStatus.SENT);
                notif.setSentAt(Instant.now());
                notificationRepository.save(notif);
            } catch (Exception e) {
                notif.setStatus(NotificationStatus.FAILED);
                notif.setRetryCount(notif.getRetryCount() + 1);
                notificationRepository.save(notif);
            }
        }
    }
}

