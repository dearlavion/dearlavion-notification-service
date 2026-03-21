package com.dearlavion.notification.kafka.handler.core;

import com.dearlavion.notification.channels.ChannelService;
import com.dearlavion.notification.kafka.dto.KafkaEventType;
import com.dearlavion.notification.kafka.dto.core.RequestEvent;
import com.dearlavion.notification.kafka.handler.EventHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class RequestEventHandler implements EventHandler<RequestEvent> {
    private final List<ChannelService<?>> channelServices;

    @Override
    public KafkaEventType getEventType() {
        return KafkaEventType.REQUEST;
    }

    @Override
    public Class<RequestEvent> payloadType() {
        return RequestEvent.class;
    }

    @Override
    public void handle(RequestEvent event) {
        for (ChannelService<?> service : channelServices) {
            if (event.getChannels().contains(service.getChannel())) {
                ((ChannelService<RequestEvent>) service)
                        .sendNotification(event.getSender(), event.getReceiver(), event);
            }
        }
    }
}
