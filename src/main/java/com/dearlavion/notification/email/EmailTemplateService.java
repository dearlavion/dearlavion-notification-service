package com.dearlavion.notification.email;

import com.dearlavion.notification.email.model.KitEmailRequest;
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

    /** Travel Besty branding (not "DearLavion" — the other templates in this file are for the
     * separate marketplace product this service was originally built for). */
    public String buildKitEmailTemplate(String displayName, KitEmailRequest req) {
        String title = req.getKitTitle() != null && !req.getKitTitle().isBlank() ? req.getKitTitle() : "Your Travel Kit";
        String summarySection = req.getSummary() != null && !req.getSummary().isBlank()
                ? "<p style=\"font-size:15px; color:#555; margin-bottom:25px;\">%s</p>".formatted(safe(req.getSummary()))
                : "";
        String itemsHtml = buildKitItemsHtml(req.getItems());

        // Table-based layout (not the div/p structure this had before) — Gmail's "quote"/
        // "signature" collapse heuristic (the "..." a recipient has to click to reveal hidden
        // content) reliably misfires on div-based email bodies, which is why every major ESP
        // (Mailchimp, SendGrid, Postmark) builds transactional emails on nested <table>s instead.
        // Removing just the footer's divider line (an earlier attempt) did not stop the footer
        // from being collapsed — this structural change is the actual fix.
        return """
        <table role="presentation" width="100%%" cellpadding="0" cellspacing="0" style="background-color:#f4f6f8; font-family:-apple-system,BlinkMacSystemFont,'Segoe UI',Roboto,Arial,sans-serif;">
        <tr><td align="center" style="padding:40px 20px;">
        <table role="presentation" width="600" cellpadding="0" cellspacing="0" style="max-width:600px; width:100%%; background:#ffffff; border-radius:14px; box-shadow:0 8px 24px rgba(0,0,0,0.06);">
        <tr><td style="padding:40px;">

            <!-- Greeting -->
            <p style="font-size:16px; margin:0 0 20px 0;">
                Hi <strong>%s</strong>,
            </p>

            <p style="font-size:15px; color:#555; margin-bottom:10px;">
                Here’s the travel kit we curated together.\s
                Nothing extra, nothing forgotten.
            </p>

            <!-- Kit Card -->
            <table role="presentation" width="100%%" cellpadding="0" cellspacing="0" style="border:1px solid #eee; border-radius:12px; background:#fff0f5; margin-bottom:30px;">
            <tr><td style="padding:25px;">

                <h2 style="margin:0 0 10px 0; font-size:18px; color:#ce5886;">
                    %s
                </h2>

                %s

                %s

            </td></tr>
            </table>

            <!-- CTA -->
            <table role="presentation" width="100%%" cellpadding="0" cellspacing="0">
            <tr><td align="center" style="padding:8px 10px 28px;">
                <p style="margin:0 0 16px 0; font-size:16px; font-weight:700; color:#2d2d2d;">
                    Stop forgetting travel essentials.
                </p>
                <a href="%s/travel" style="display:inline-block; background:#f2994a; color:#fff; font-size:14px; font-weight:700; text-decoration:none; padding:13px 32px; border-radius:999px;">
                    Build a Kit
                </a>
            </td></tr>
            </table>

            <!-- Footer -->
            <table role="presentation" width="100%%" cellpadding="0" cellspacing="0" style="background:#fdf5f8; border-radius:12px;">
            <tr><td align="center" style="padding:28px 20px 24px;">
                <p style="margin:0 0 8px 0; font-size:13px; color:#8a8a8a;">
                    Built with 💛 by <span style="font-weight:700; color:#ce5886;">TravelBesty.ph</span>
                </p>
                <p style="margin:0; font-size:12.5px; color:#c98aa2; font-style:italic;">
                    Personalized travel essentials for every trip.
                </p>
            </td></tr>
            </table>

        </td></tr>
        </table>
        </td></tr>
        </table>
        """
                .formatted(
                        displayName != null && !displayName.isBlank() ? displayName : "there",
                        safe(title),
                        summarySection,
                        itemsHtml,
                        TRAVEL_BESTY_FRONTEND_URL
                );
    }

    // Travel Besty's eventual production domain — this app has no live public URL yet, so
    // clicking a suggestion link or the "Build a Kit" CTA before launch won't resolve. Update
    // this once the site is actually deployed there.
    private static final String TRAVEL_BESTY_FRONTEND_URL = "https://travelbesty.ph";

    /** Groups items by kitCategory (mirrors my-kit.component.ts's own groupedDisplayItems —
     * items with no category fall under "Other"), rendering each as a checkbox-style row with a
     * bullet list of up to 3 clickable product-suggestion links underneath. */
    private String buildKitItemsHtml(java.util.List<KitEmailRequest.Item> items) {
        if (items == null || items.isEmpty()) {
            return "<p style=\"margin:0; color:#555; font-size:14px;\">No items yet.</p>";
        }

        java.util.LinkedHashMap<String, java.util.List<KitEmailRequest.Item>> byCategory = new java.util.LinkedHashMap<>();
        for (KitEmailRequest.Item item : items) {
            String category = item.getKitCategory() != null && !item.getKitCategory().isBlank() ? item.getKitCategory() : "Other";
            byCategory.computeIfAbsent(category, k -> new java.util.ArrayList<>()).add(item);
        }

        // Table rows, not p/ul/li — see buildKitEmailTemplate's comment on why this whole email
        // is table-based.
        StringBuilder sb = new StringBuilder("<table role=\"presentation\" width=\"100%\" cellpadding=\"0\" cellspacing=\"0\">");
        for (var entry : byCategory.entrySet()) {
            sb.append("""
                    <tr><td style="padding:16px 0 8px 0; font-size:12.5px; font-weight:700; color:#ce5886; text-transform:uppercase; letter-spacing:0.5px;">%s</td></tr>
                    """.formatted(safe(entry.getKey())));
            for (KitEmailRequest.Item item : entry.getValue()) {
                sb.append("""
                        <tr><td style="padding:0 0 4px 0; font-size:14px; color:#333;"><span style="font-size:20px; vertical-align:-3px;">&#9744;</span>&nbsp;%s</td></tr>
                        """.formatted(safe(item.getLabel())));
                sb.append(buildSuggestionsRows(item.getSuggestions()));
            }
        }
        sb.append("</table>");
        return sb.toString();
    }

    private String buildSuggestionsRows(java.util.List<KitEmailRequest.Suggestion> suggestions) {
        if (suggestions == null || suggestions.isEmpty()) return "";
        StringBuilder sb = new StringBuilder();
        for (KitEmailRequest.Suggestion s : suggestions) {
            String url = TRAVEL_BESTY_FRONTEND_URL + "/product/" + safe(s.getProductId()) + "/items/" + safe(s.getItemId());
            sb.append("""
                    <tr><td style="padding:0 0 4px 24px; font-size:13px; color:#555;">&#8226;&nbsp;<a href="%s" style="color:#ce5886; text-decoration:none;">%s</a></td></tr>
                    """.formatted(url, safe(s.getName())));
        }
        sb.append("<tr><td style=\"padding:0 0 6px 0; font-size:1px; line-height:1px;\">&nbsp;</td></tr>");
        return sb.toString();
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