package com.dearlavion.notification.kafka.handler;

import com.dearlavion.notification.channels.EmailChannelImpl;
import com.dearlavion.notification.kafka.dto.EventType;
import com.dearlavion.notification.kafka.dto.ResetPasswordEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ResetPasswordEventHandler implements EventHandler<ResetPasswordEvent> {

    private final EmailChannelImpl emailChannel;

    @Override
    public EventType getEventType() {
        return EventType.RESET_PASSWORD;
    }

    @Override
    public Class<ResetPasswordEvent> payloadType() {
        return ResetPasswordEvent.class;
    }

    @Override
    public void handle(ResetPasswordEvent payload) {
        emailChannel.sendResetPasswordNotification(payload.getUsername(), payload.getToken());
    }

}
