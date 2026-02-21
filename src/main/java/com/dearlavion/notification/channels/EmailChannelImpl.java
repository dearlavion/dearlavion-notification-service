package com.dearlavion.notification.channels;

import com.dearlavion.notification.email.EmailService;
import com.dearlavion.notification.kafka.dto.WishEvent;
import com.dearlavion.notification.security.AuthServiceClient;
import com.dearlavion.notification.security.UserDto;
import com.dearlavion.notification.subscription.dto.CopilotSubscription;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailChannelImpl implements ChannelService {

    private final EmailService emailService;

    @Value("${email.sender}")
    private String sender;

    @Autowired
    private AuthServiceClient authService;


    @Override
    public String getChannel() {
        return "EMAIL";
    }

    @Override
    public void send(CopilotSubscription subscriber, WishEvent wish) {
        if (sender == null || subscriber.getUsername() == null) {
            return;
        }

        String to = getRegisteredEmail(subscriber.getUsername()); // lookup email later

        String subject = "New Wish in " + wish.getCityName();

        String body = """
                Hi Copilot %s,

                %s

                Amount: %s
                """
                .formatted(
                        wish.getUsername(),
                        wish.getTitle(),
                        wish.getAmount()
                );

        emailService.sendEmail(sender, to, subject, body);
    }

    private String getRegisteredEmail(String username) {
        UserDto user = authService.getUserDetails(username);
        return user != null ? user.getEmail() : null;
    }
}
