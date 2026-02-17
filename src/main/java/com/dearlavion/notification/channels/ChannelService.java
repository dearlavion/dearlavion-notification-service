package com.dearlavion.notification.channels;

import com.dearlavion.notification.kafka.dto.WishEvent;
import com.dearlavion.notification.subscription.dto.CopilotSubscription;

public interface ChannelService {
    String getChannel(); // EMAIL, SMS, etc.

    void send(CopilotSubscription sub, WishEvent wish);

}
