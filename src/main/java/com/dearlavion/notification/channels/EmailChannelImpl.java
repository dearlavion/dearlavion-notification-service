package com.dearlavion.notification.channels;

import com.dearlavion.notification.email.EmailService;
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

    @Value("${email.sender}")
    private String sender;

    @Value("${email.wish.url}")
    private String wishUrl;

    @Override
    public String getChannel() {
        return "EMAIL";
    }

    @Override
    public void send(CopilotSubscription subscriber, WishEvent wish) {

        if (sender == null || subscriber == null || wish == null) {
            return;
        }

        String to = getRegisteredEmail(subscriber.getUsername());

        if (to == null || to.isBlank()) {
            return;
        }

        String subject = "✈️ Dear lavion, New Wish Alert in " + wish.getCityName();

        String body = composeBody(subscriber, wish);

        emailService.sendEmail(sender, to, subject, body);
    }

    private String getRegisteredEmail(String username) {
        if (username == null) return null;

        UserDto user = authService.getUserDetails(username);
        return user != null ? user.getEmail() : null;
    }

    private String composeBody(CopilotSubscription subscriber, WishEvent wish) {

        String wishLink = wishUrl + wish.getId();
        String budgetSection = buildBudgetSection(wish);

        return """
        <div style="background-color:#f4f6f8; padding:40px 20px; font-family:-apple-system,BlinkMacSystemFont,'Segoe UI',Roboto,Arial,sans-serif;">

            <div style="max-width:600px; margin:0 auto; background:#ffffff; border-radius:14px; padding:40px; box-shadow:0 8px 24px rgba(0,0,0,0.06);">

                <!-- Header -->
                <div style="text-align:center; margin-bottom:30px;">
                    <h1 style="margin:0; font-size:22px; letter-spacing:1px; color:#ce5886;">
                        ✈️ DearLavion
                    </h1>
                </div>

                <!-- Greeting -->
                <p style="font-size:16px; margin:0 0 20px 0;">
                    Hi <strong>%s</strong>,
                </p>

                <!-- Message -->
                <p style="font-size:15px; color:#555; margin-bottom:30px;">
                    A new wish has been posted in <strong>%s</strong> that might match your interest.
                </p>

                <!-- Wish Card -->
                <div style="border:1px solid #eee; border-radius:12px; padding:25px; margin-bottom:30px; background:#fff0f5;">

                    <h2 style="margin:0 0 10px 0; font-size:18px;">
                        <a href="%s" style="color:#ce5886; text-decoration:none;">
                            %s
                        </a>
                    </h2>

                    %s

                </div>

                <!-- CTA Button -->
                <div style="text-align:center; margin-bottom:35px;">
                    <a href="%s"
                       style="background-color:#ce5886;
                              color:#ffffff;
                              padding:14px 28px;
                              border-radius:10px;
                              text-decoration:none;
                              font-weight:600;
                              font-size:14px;
                              display:inline-block;">
                        View Wish
                    </a>
                </div>

                <!-- Footer -->
                <hr style="border:none; border-top:1px solid #eee; margin:30px 0;" />

                <p style="font-size:12px; color:#999; text-align:center; line-height:1.6;">
                    You're receiving this because you're subscribed as a Copilot.<br/>
                    — <span style="color:#ce5886;">DearLavion Team</span><br/>
                    <em>We help your wishes take flight ✈️</em>
                </p>

            </div>
        </div>
        """
                .formatted(
                        safe(subscriber.getUsername()),
                        safe(wish.getCityName()),
                        wishLink,
                        safe(wish.getTitle()),
                        budgetSection,
                        wishLink
                );
    }

    private String buildBudgetSection(WishEvent wish) {
        if (wish.getAmount() == null) {
            return "";
        }

        return """
        <p style="margin:10px 0 0 0; color:#ce5886; font-weight:600;">
            💰 Reward: %s
        </p>
        """.formatted(wish.getAmount());
    }


    private String safe(String value) {
        return value == null ? "" : value;
    }
}