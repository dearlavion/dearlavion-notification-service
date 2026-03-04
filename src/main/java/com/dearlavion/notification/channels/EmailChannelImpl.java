package com.dearlavion.notification.channels;

import com.dearlavion.notification.email.EmailService;
import com.dearlavion.notification.email.EmailTemplateService;
import com.dearlavion.notification.kafka.dto.WishEvent;
import com.dearlavion.notification.security.AuthServiceClient;
import com.dearlavion.notification.security.UserDto;
import com.dearlavion.notification.subscription.dto.CopilotSubscription;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailChannelImpl implements ChannelService {

    private final EmailService emailService;
    private final AuthServiceClient authService;
    private final EmailTemplateService templateService;

    @Value("${email.sender}")
    private String sender;

    @Value("${email.frontend.url}")
    private String frontendUrl;

    @Override
    public String getChannel() {
        return "EMAIL";
    }

    @Override
    public void sendWishSubscriptionNotification(CopilotSubscription subscriber, WishEvent wish) {

        if (sender == null || subscriber == null || wish == null) {
            return;
        }

        String to = getRegisteredEmail(subscriber.getUsername());

        if (to == null || to.isBlank()) {
            return;
        }

        String subject = "✈️ Dear lavion, New Wish Alert in " + wish.getCityName();

        String body = templateService.buildWishSubscriberTemplate(subscriber.getUsername(), wish, frontendUrl);

        emailService.sendEmail(sender, to, subject, body);
    }

    public void sendResetPasswordNotification(String username, String token) {
        String resetLink = frontendUrl + "/reset?token=" + token;

        String to = getRegisteredEmail(username);

        if (to == null || to.isBlank()) {
            return;
        }

        String subject = "✈️ Dear lavion, You can now Reset Your Password";

        String body = templateService.buildResetPasswordTemplate(
                username,
                resetLink
        );

        emailService.sendEmail(sender, to, subject, body);
    }

    private String getRegisteredEmail(String username) {
        if (username == null) return null;

        UserDto user = authService.getUserDetails(username);
        return user != null ? user.getEmail() : null;
    }


}