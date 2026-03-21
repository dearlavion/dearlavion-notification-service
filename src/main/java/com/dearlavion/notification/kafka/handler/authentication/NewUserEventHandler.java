package com.dearlavion.notification.kafka.handler.authentication;

import com.dearlavion.notification.channels.EmailChannelImpl;
import com.dearlavion.notification.kafka.dto.KafkaEventType;
import com.dearlavion.notification.kafka.dto.authentication.NewUserEvent;
import com.dearlavion.notification.kafka.handler.EventHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class NewUserEventHandler implements EventHandler<NewUserEvent> {

    private final EmailChannelImpl emailChannel;

    @Override
    public KafkaEventType getEventType() {
        return KafkaEventType.NEW_USER;
    }

    @Override
    public Class<NewUserEvent> payloadType() {
        return NewUserEvent.class;
    }

    @Override
    public void handle(NewUserEvent payload) {
        emailChannel.sendWelcomeUserNotification(payload.getUsername());
    }
}
