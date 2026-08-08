package com.dearlavion.notification.email;

import com.dearlavion.notification.email.model.NewsletterThanksRequest;
import com.dearlavion.notification.email.model.PopularKitAnnouncementRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * Server-to-server only — store-engine's NewsletterService calls this right after inserting a
 * genuinely new subscriber (never for a repeat email). There's no end-user bearer token here
 * (newsletter subscribe is a public, unauthenticated storefront action), so this can't use the
 * same AuthServiceClient-verification pattern as KitEmailController — instead it's gated by a
 * shared secret header, checked manually since SecurityConfig permits this path through.
 */
@Slf4j
@RestController
@RequiredArgsConstructor
public class NewsletterEmailController {

    // Travel Besty's own sender identity — see KitEmailController's comment on why this is
    // deliberately not the shared "${email.sender}" DearLavion property.
    private static final String SENDER = "travelbesty.ph@gmail.com";

    private final EmailTemplateService templateService;
    private final EmailService emailService;

    @Value("${internal.api-key}")
    private String internalApiKey;

    @PostMapping("/notification/internal/newsletter-thanks")
    public ResponseEntity<?> newsletterThanks(
            @RequestHeader(value = "X-Internal-Api-Key", required = false) String apiKey,
            @RequestBody NewsletterThanksRequest body) {
        if (apiKey == null || !apiKey.equals(internalApiKey)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        if (body.getEmail() == null || body.getEmail().isBlank()) {
            return ResponseEntity.badRequest().build();
        }

        String html = templateService.buildNewsletterThanksTemplate();
        emailService.sendEmail(SENDER, body.getEmail(), "Thanks for subscribing to Travel Besty!", html);

        return ResponseEntity.ok(Map.of("sent", true));
    }

    /** store-engine calls this once, right after an admin creates a new Popular Kit — never for
     * edits or deactivations. `emails` is that request's whole subscriber list; one bad address
     * shouldn't stop the rest of the batch from sending, so failures are caught per-recipient. */
    @PostMapping("/notification/internal/popular-kit-announcement")
    public ResponseEntity<?> popularKitAnnouncement(
            @RequestHeader(value = "X-Internal-Api-Key", required = false) String apiKey,
            @RequestBody PopularKitAnnouncementRequest body) {
        if (apiKey == null || !apiKey.equals(internalApiKey)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        List<String> emails = body.getEmails();
        if (emails == null || emails.isEmpty() || body.getKitName() == null || body.getKitName().isBlank()) {
            return ResponseEntity.badRequest().build();
        }

        String html = templateService.buildPopularKitAnnouncementTemplate(body.getKitName(), body.getKitSlug(), body.getImage(), body.getTag());
        String subject = "New Travel Besty Kit: " + body.getKitName();

        int sent = 0;
        for (String email : emails) {
            if (email == null || email.isBlank()) continue;
            try {
                emailService.sendEmail(SENDER, email, subject, html);
                sent++;
            } catch (Exception e) {
                log.error("popular-kit announcement failed for {}: {}", email, e.getMessage());
            }
        }

        return ResponseEntity.ok(Map.of("sent", true, "count", sent));
    }
}
