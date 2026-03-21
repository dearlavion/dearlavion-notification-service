package com.dearlavion.notification.channels;

import com.dearlavion.notification.inApp.InAppNotificationService;
import com.dearlavion.notification.inApp.dto.InAppNotification;
import com.dearlavion.notification.inApp.repository.InAppNotificationRepository;
import com.dearlavion.notification.kafka.dto.core.RequestEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class InAppChannelImpl implements ChannelService<RequestEvent> {

    private final InAppNotificationService notificationService;
    private final InAppNotificationRepository repository;

    @Override
    public ChannelType getChannel() {
        return ChannelType.IN_APP;
    }

    @Override
    public void sendNotification(String sender, String receiver, RequestEvent event) {
        if (sender == null || receiver == null || event == null) {
            return;
        }

        InAppNotification notification = InAppNotification.builder()
                .sender(event.getSender())
                .receiver(event.getReceiver())
                .title("New Join Request")
                .message(event.getSender() + " requested to join your event")
                .type(event.getType())
                .read(false)
                .link(event.getId())
                .createdAt(Instant.now())
                .build();

        repository.save(notification);

        notificationService.sendNotification(receiver, notification);
    }



}