package com.dearlavion.notification.email.model;

import lombok.Data;

/** Body for POST /notification/internal/newsletter-thanks — a server-to-server call from
 * store-engine's NewsletterService right after a genuinely new subscriber is inserted (never
 * re-sent for a repeat/already-subscribed email). */
@Data
public class NewsletterThanksRequest {
    private String email;
}
