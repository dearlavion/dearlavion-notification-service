package com.dearlavion.notification.kafka.handler;

import com.dearlavion.notification.channels.EmailChannelImpl;
import com.dearlavion.notification.kafka.dto.EventType;
import com.dearlavion.notification.kafka.dto.NewUserEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class NewUserEventHandler implements EventHandler<NewUserEvent> {

    private final EmailChannelImpl emailChannel;

    @Override
    public EventType getEventType() {
        return EventType.NEW_USER;
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
