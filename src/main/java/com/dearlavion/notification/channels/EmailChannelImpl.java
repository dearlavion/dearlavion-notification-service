package com.dearlavion.notification.channels;

import com.dearlavion.notification.email.EmailService;
import com.dearlavion.notification.kafka.dto.WishEvent;
import com.dearlavion.notification.subscription.dto.CopilotSubscription;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailChannelImpl implements ChannelService {

    private final EmailService emailService;

    @Override
    public String getChannel() {
        return "EMAIL";
    }

    @Override
    public void send(CopilotSubscription sub, WishEvent wish) {
        String to = sub.getUserId(); // lookup email later

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

        emailService.sendEmail(to, subject, body);
    }
}
