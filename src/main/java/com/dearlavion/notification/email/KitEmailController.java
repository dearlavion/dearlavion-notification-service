package com.dearlavion.notification.email;

import com.dearlavion.notification.email.model.KitEmailRequest;
import com.dearlavion.notification.security.AuthServiceClient;
import com.dearlavion.notification.security.AuthVerificationResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * "Email my kit" (Travel Besty's /my-kit page). The installed TokenVerificationFilter already
 * gated this route down to a valid bearer token (see SecurityConfig's "/notification/email/**"
 * matcher) and put the username on the SecurityContext — but not the email, so this controller
 * re-verifies the same token itself to get it. That's a deliberate, accepted extra network hop:
 * simpler than threading the full AuthVerificationResponse through the shared filter for this one
 * caller. The recipient is always this resolved email — the request body carries no `to` field —
 * so an authenticated caller can only ever email themselves.
 */
@RestController
@RequiredArgsConstructor
public class KitEmailController {

    private final AuthServiceClient authClient;
    private final EmailTemplateService templateService;
    private final EmailService emailService;

    @Value("${email.sender}")
    private String sender;

    @PostMapping("/notification/email/kit")
    public ResponseEntity<?> emailKit(HttpServletRequest request, @RequestBody KitEmailRequest body) {
        String header = request.getHeader("Authorization");
        if (header == null || !header.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        AuthVerificationResponse verification = authClient.verify(header.substring(7));
        if (verification == null || !verification.isValid() || verification.getEmail() == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String html = templateService.buildKitEmailTemplate(verification.getUsername(), body);
        String subject = "🧳 " + (body.getKitTitle() != null && !body.getKitTitle().isBlank() ? body.getKitTitle() : "Your Travel Kit");
        emailService.sendEmail(sender, verification.getEmail(), subject, html);

        return ResponseEntity.ok(Map.of("sent", true));
    }
}
