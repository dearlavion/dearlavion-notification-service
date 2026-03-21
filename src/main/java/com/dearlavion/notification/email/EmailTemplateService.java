package com.dearlavion.notification.email;

import com.dearlavion.notification.kafka.dto.core.WishEvent;
import org.springframework.stereotype.Service;

@Service
public class EmailTemplateService {

    public String buildWishSubscriberTemplate(String username, WishEvent wish, String wishUrl) {

        String wishLink = wishUrl + "/wish/" + wish.getId();
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
                        safe(username),
                        safe(wish.getCityName()),
                        wishLink,
                        safe(wish.getTitle()),
                        budgetSection,
                        wishLink
                );
    }

    public String buildResetPasswordTemplate(String username, String resetLink) {
        return """
            <div style="font-family: Arial; padding: 20px;">
                <h2 style="color:#ce5886;">Reset Your Password</h2>

                <p>Hi <strong>%s</strong>,</p>

                <p>Click the button below to reset your password:</p>

                <a href="%s"
                   style="background:#ce5886;
                          color:white;
                          padding:12px 20px;
                          border-radius:8px;
                          text-decoration:none;">
                    Reset Password
                </a>

                <p style="margin-top:20px; font-size:12px; color:#888;">
                    If you did not request this, you can ignore this email.
                </p>
            </div>
            """.formatted(
                username != null ? username : "there",
                resetLink
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

    public String buildWelcomeUserTemplate(String username, String appUrl) {

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
                Welcome <strong>%s</strong>! 🎉
            </p>

            <!-- Message -->
            <p style="font-size:15px; color:#555; margin-bottom:25px;">
                Your account has been successfully created and you're now part of the 
                <strong>DearLavion</strong> community.
            </p>

            <p style="font-size:15px; color:#555; margin-bottom:25px;">
                DearLavion helps people share wishes and connect with others who can help make them happen.
                Whether you're posting a wish or helping someone else's take flight, you're in the right place.
            </p>

            <!-- Feature Card -->
            <div style="border:1px solid #eee; border-radius:12px; padding:25px; margin-bottom:30px; background:#fff0f5;">

                <h2 style="margin:0 0 12px 0; font-size:18px; color:#ce5886;">
                    What you can do next
                </h2>

                <ul style="padding-left:18px; margin:0; color:#555; font-size:14px;">
                    <li style="margin-bottom:8px;">✨ Post your first wish</li>
                    <li style="margin-bottom:8px;">🌍 Explore wishes from your city</li>
                    <li style="margin-bottom:8px;">🤝 Become a Copilot and help others</li>
                </ul>

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
                    Start Exploring
                </a>
            </div>

            <!-- Footer -->
            <hr style="border:none; border-top:1px solid #eee; margin:30px 0;" />

            <p style="font-size:12px; color:#999; text-align:center; line-height:1.6;">
                We're excited to have you with us.<br/>
                — <span style="color:#ce5886;">DearLavion Team</span><br/>
                <em>We help your wishes take flight ✈️</em>
            </p>

        </div>
    </div>
    """
                .formatted(
                        username != null ? username : "there",
                        appUrl
                );
    }
}