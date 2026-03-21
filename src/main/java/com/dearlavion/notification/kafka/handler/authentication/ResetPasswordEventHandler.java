package com.dearlavion.notification.kafka.handler.authentication;

import com.dearlavion.notification.channels.EmailChannelImpl;
import com.dearlavion.notification.kafka.dto.KafkaEventType;
import com.dearlavion.notification.kafka.dto.authentication.ResetPasswordEvent;
import com.dearlavion.notification.kafka.handler.EventHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ResetPasswordEventHandler implements EventHandler<ResetPasswordEvent> {

    private final EmailChannelImpl emailChannel;

    @Override
    public KafkaEventType getEventType() {
        return KafkaEventType.RESET_PASSWORD;
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
