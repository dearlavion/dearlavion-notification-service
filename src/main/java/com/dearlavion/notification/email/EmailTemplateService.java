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
            <table role="presentation" width="100%%" cellpadding="0" cellspacing="0">
            <tr><td align="center" style="padding:28px 20px 24px;">
                <p style="margin:0 0 8px 0; font-size:13px; color:#8a8a8a;">
                    Built with 💛 by <a href="%s" style="font-weight:700; color:#ce5886; text-decoration:none;">TravelBesty.ph</a>
                </p>
                <p style="margin:0; font-size:12.5px; color:#c98aa2; font-style:italic;">
                    Personalized travel essentials for every trip.
                </p>
                <!-- Invisible per-send marker — Gmail hides repeated identical content behind a
                     "..." toggle for a recipient who's received the same exact HTML block many
                     times before (this footer text hadn't changed across many test sends today).
                     Restructuring to <table> didn't stop it, which is itself evidence this is
                     that repetition-based collapsing, not a markup pattern — this token makes
                     each send byte-different so Gmail can't fingerprint-match it. -->
                <span style="font-size:0; line-height:0; color:transparent; display:block; height:0; overflow:hidden;">%s</span>
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
                        TRAVEL_BESTY_FRONTEND_URL,
                        TRAVEL_BESTY_FRONTEND_URL,
                        java.util.UUID.randomUUID()
                );
    }

    // Travel Besty's eventual production domain — this app has no live public URL yet, so
    // clicking a suggestion link or the "Build a Kit" CTA before launch won't resolve. Update
    // this once the site is actually deployed there.
    private static final String TRAVEL_BESTY_FRONTEND_URL = "https://travelbesty.ph";

    /** Newsletter opt-in thank-you — sent once, only for a genuinely new subscriber (see
     * NewsletterEmailController). Same table-based layout/brand shell as buildKitEmailTemplate,
     * just a shorter message with no kit-card section. */
    public String buildNewsletterThanksTemplate() {
        return """
        <table role="presentation" width="100%%" cellpadding="0" cellspacing="0" style="background-color:#f4f6f8; font-family:-apple-system,BlinkMacSystemFont,'Segoe UI',Roboto,Arial,sans-serif;">
        <tr><td align="center" style="padding:40px 20px;">
        <table role="presentation" width="600" cellpadding="0" cellspacing="0" style="max-width:600px; width:100%%; background:#ffffff; border-radius:14px; box-shadow:0 8px 24px rgba(0,0,0,0.06);">
        <tr><td style="padding:40px;">

            <p style="font-size:16px; margin:0 0 20px 0;">
                Hi there,
            </p>

            <p style="font-size:15px; color:#555; margin-bottom:30px;">
                Thanks for subscribing to <strong>Travel Besty</strong>! You'll hear from us when we
                add new packing essentials and travel kit ideas — nothing extra, nothing spammy.
            </p>

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
            <table role="presentation" width="100%%" cellpadding="0" cellspacing="0">
            <tr><td align="center" style="padding:28px 20px 24px;">
                <p style="margin:0 0 8px 0; font-size:13px; color:#8a8a8a;">
                    Built with 💛 by <a href="%s" style="font-weight:700; color:#ce5886; text-decoration:none;">TravelBesty.ph</a>
                </p>
                <p style="margin:0; font-size:12.5px; color:#c98aa2; font-style:italic;">
                    Personalized travel essentials for every trip.
                </p>
                <!-- Same anti-collapse marker as buildKitEmailTemplate — see that method's comment. -->
                <span style="font-size:0; line-height:0; color:transparent; display:block; height:0; overflow:hidden;">%s</span>
            </td></tr>
            </table>

        </td></tr>
        </table>
        </td></tr>
        </table>
        """
                .formatted(
                        TRAVEL_BESTY_FRONTEND_URL,
                        TRAVEL_BESTY_FRONTEND_URL,
                        java.util.UUID.randomUUID()
                );
    }

    /** New Popular Kit announcement — sent once per newly-created kit to every current newsletter
     * subscriber (see PopularKitService.create() on the store-engine side). Same brand shell as
     * the other templates; adds an optional kit image banner + tag badge above the message. */
    public String buildPopularKitAnnouncementTemplate(String kitName, String kitSlug, String image, String tag) {
        String kitUrl = TRAVEL_BESTY_FRONTEND_URL + "/popular/" + (kitSlug != null ? kitSlug : "");
        String imageHtml = image != null && !image.isBlank()
                ? """
                <tr><td style="padding:0 0 24px 0;">
                    <img src="%s" width="600" alt="%s" style="display:block; width:100%%; max-width:600px; border-radius:12px 12px 0 0;" />
                </td></tr>
                """.formatted(safe(image), safe(kitName))
                : "";
        String tagHtml = tag != null && !tag.isBlank()
                ? """
                <span style="display:inline-block; background:#fff0f5; color:#ce5886; font-size:11.5px; font-weight:700; text-transform:uppercase; letter-spacing:0.5px; padding:4px 12px; border-radius:999px; margin-bottom:14px;">%s</span><br/>
                """.formatted(safe(tag))
                : "";

        return """
        <table role="presentation" width="100%%" cellpadding="0" cellspacing="0" style="background-color:#f4f6f8; font-family:-apple-system,BlinkMacSystemFont,'Segoe UI',Roboto,Arial,sans-serif;">
        <tr><td align="center" style="padding:40px 20px;">
        <table role="presentation" width="600" cellpadding="0" cellspacing="0" style="max-width:600px; width:100%%; background:#ffffff; border-radius:14px; box-shadow:0 8px 24px rgba(0,0,0,0.06); overflow:hidden;">
        %s
        <tr><td style="padding:40px;">

            %s
            <h1 style="margin:0 0 14px 0; font-size:22px; color:#2d2d2d;">
                New Kit: %s
            </h1>

            <p style="font-size:15px; color:#555; margin-bottom:30px;">
                We just added a new Popular Kit to Travel Besty — take a look and see if it fits
                your next trip.
            </p>

            <!-- CTA -->
            <table role="presentation" width="100%%" cellpadding="0" cellspacing="0">
            <tr><td align="center" style="padding:8px 10px 28px;">
                <a href="%s" style="display:inline-block; background:#f2994a; color:#fff; font-size:14px; font-weight:700; text-decoration:none; padding:13px 32px; border-radius:999px;">
                    View Kit
                </a>
            </td></tr>
            </table>

            <!-- Footer -->
            <table role="presentation" width="100%%" cellpadding="0" cellspacing="0">
            <tr><td align="center" style="padding:28px 20px 24px;">
                <p style="margin:0 0 8px 0; font-size:13px; color:#8a8a8a;">
                    Built with 💛 by <a href="%s" style="font-weight:700; color:#ce5886; text-decoration:none;">TravelBesty.ph</a>
                </p>
                <p style="margin:0; font-size:12.5px; color:#c98aa2; font-style:italic;">
                    Personalized travel essentials for every trip.
                </p>
                <!-- Same anti-collapse marker as the other templates — see buildKitEmailTemplate's
                     comment. Especially relevant here since this exact HTML goes out to every
                     subscriber at once, so every recipient's inbox sees byte-identical content
                     unless this token varies per send. -->
                <span style="font-size:0; line-height:0; color:transparent; display:block; height:0; overflow:hidden;">%s</span>
            </td></tr>
            </table>

        </td></tr>
        </table>
        </td></tr>
        </table>
        """
                .formatted(
                        imageHtml,
                        tagHtml,
                        safe(kitName),
                        kitUrl,
                        TRAVEL_BESTY_FRONTEND_URL,
                        java.util.UUID.randomUUID()
                );
    }

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
                        <tr><td style="padding:0 0 4px 0; font-size:14px; color:#333;"><span style="display:inline-block; width:13px; height:13px; line-height:13px; font-size:1px; overflow:hidden; border:1.5px solid #ce5886; border-radius:3px; vertical-align:-2px;">&nbsp;</span>&nbsp;<strong>%s</strong></td></tr>
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