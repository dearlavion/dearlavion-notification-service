package com.dearlavion.notification.email;

import com.dearlavion.notification.email.model.NewsletterThanksRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * Server-to-server only — store-engine's NewsletterService calls this right after inserting a
 * genuinely new subscriber (never for a repeat email). There's no end-user bearer token here
 * (newsletter subscribe is a public, unauthenticated storefront action), so this can't use the
 * same AuthServiceClient-verification pattern as KitEmailController — instead it's gated by a
 * shared secret header, checked manually since SecurityConfig permits this path through.
 */
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
}
