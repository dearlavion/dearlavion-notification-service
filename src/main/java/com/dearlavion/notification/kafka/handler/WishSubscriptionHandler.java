package com.dearlavion.notification.kafka.handler;

import com.dearlavion.notification.channels.ChannelService;
import com.dearlavion.notification.kafka.dto.EventType;
import com.dearlavion.notification.kafka.dto.WishEvent;
import com.dearlavion.notification.subscription.SubscriptionMatcherService;
import com.dearlavion.notification.subscription.dto.CopilotSubscription;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WishSubscriptionHandler implements EventHandler<WishEvent> {

    private final SubscriptionMatcherService matcherService;
    private final ChannelService channelService;


    @Override
    public EventType getEventType() {
        return EventType.NEW_WISH;
    }

    @Override
    public Class<WishEvent> payloadType() {
        return WishEvent.class;
    }

    @Override
    public void handle(WishEvent wishEvent) {
        // 1️⃣ Find matching copilots
        List<CopilotSubscription> matches = matcherService.match(wishEvent);

        // 2️⃣ Send notifications
        for (CopilotSubscription subscriber : matches) {
            channelService.sendWishSubscriptionNotification(subscriber, wishEvent);
        }
    }
}
