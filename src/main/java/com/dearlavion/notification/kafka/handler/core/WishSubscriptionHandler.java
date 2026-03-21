package com.dearlavion.notification.kafka.handler.core;

import com.dearlavion.notification.channels.ChannelService;
import com.dearlavion.notification.kafka.dto.KafkaEventType;
import com.dearlavion.notification.kafka.dto.core.WishEvent;
import com.dearlavion.notification.kafka.handler.EventHandler;
import com.dearlavion.notification.subscription.SubscriptionMatcherService;
import com.dearlavion.notification.subscription.dto.CopilotSubscription;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WishSubscriptionHandler implements EventHandler<WishEvent> {

    private final SubscriptionMatcherService matcherService;
    private final List<ChannelService<?>> channelServices;


    @Override
    public KafkaEventType getEventType() {
        return KafkaEventType.NEW_WISH;
    }

    @Override
    public Class<WishEvent> payloadType() {
        return WishEvent.class;
    }

    @Override
    public void handle(WishEvent event) {
        // 1️⃣ Find matching copilots
        List<CopilotSubscription> matches = matcherService.match(event);

        // 2️⃣ Send notifications
        for (CopilotSubscription subscriber : matches) {
            for (ChannelService<?> service : channelServices) {
                if (event.getChannels().contains(service.getChannel())) {
                    ((ChannelService<WishEvent>) service)
                            .sendNotification(null, subscriber.getUsername(), event);
                }
            }
        }
    }
}
