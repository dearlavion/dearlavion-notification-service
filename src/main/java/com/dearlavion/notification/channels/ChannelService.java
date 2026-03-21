package com.dearlavion.notification.channels;

public interface ChannelService<T> {
    ChannelType getChannel(); // EMAIL, IN-APP, SMS, etc.
    void sendNotification(String sender, String receiver, T event);
}
