package com.dearlavion.notification.email;

import com.dearlavion.notification.kafka.dto.booking.BookingEvent;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

/**
 * Sends booking notification emails.
 *
 * <p>Unlike {@code EmailChannelImpl}, which resolves a recipient by looking up a username in
 * auth-service, this sends to a <b>raw email address</b> carried on the {@link BookingEvent}. That
 * is essential because guest bookers have no Dearlavion account and therefore no username to resolve.
 */
@Service
@RequiredArgsConstructor
public class BookingEmailService {

    private static final Logger log = LoggerFactory.getLogger(BookingEmailService.class);

    private static final DateTimeFormatter WHEN =
            DateTimeFormatter.ofPattern("EEE, d MMM yyyy 'at' HH:mm 'UTC'").withZone(ZoneId.of("UTC"));

    private final EmailService emailService;

    @Value("${email.sender}")
    private String sender;

    /** New booking awaiting approval — notify the business. */
    public void sendBookingCreatedToBusiness(BookingEvent event) {
        send(event.getBusinessEmail(),
                "🗓️ New booking request — " + safe(event.getOfferingName()),
                "<p>You have a new booking request from <b>" + safe(event.customerDisplayName())
                        + "</b> for <b>" + safe(event.getOfferingName()) + "</b> on "
                        + when(event) + ".</p><p>It is awaiting your approval.</p>");
    }

    /** Booking confirmed — notify the customer/guest. */
    public void sendBookingConfirmedToCustomer(BookingEvent event) {
        send(event.customerRecipientEmail(),
                "✅ Your booking is confirmed — " + safe(event.getBusinessName()),
                "<p>Hi " + safe(event.customerDisplayName()) + ",</p>"
                        + "<p>Your booking for <b>" + safe(event.getOfferingName()) + "</b> at <b>"
                        + safe(event.getBusinessName()) + "</b> on " + when(event) + " is confirmed.</p>"
                        + codeLine(event));
    }

    /** Booking cancelled — notify the customer/guest (owner-initiated) or business (customer-initiated). */
    public void sendBookingCancelled(BookingEvent event) {
        String reason = event.getCancellationReason() != null
                ? "<p>Reason: " + safe(event.getCancellationReason()) + "</p>" : "";
        // Notify the customer side; a copy to the business keeps both parties informed.
        send(event.customerRecipientEmail(),
                "❌ Your booking was cancelled — " + safe(event.getBusinessName()),
                "<p>Hi " + safe(event.customerDisplayName()) + ",</p>"
                        + "<p>Your booking for <b>" + safe(event.getOfferingName()) + "</b> on "
                        + when(event) + " has been cancelled.</p>" + reason);
        send(event.getBusinessEmail(),
                "Booking cancelled — " + safe(event.getOfferingName()),
                "<p>The booking for <b>" + safe(event.customerDisplayName()) + "</b> on "
                        + when(event) + " has been cancelled.</p>" + reason);
    }

    /** Booking rescheduled — notify both parties. */
    public void sendBookingRescheduled(BookingEvent event) {
        String body = "<p>The booking for <b>" + safe(event.getOfferingName())
                + "</b> has been moved to " + when(event) + ".</p>";
        send(event.customerRecipientEmail(),
                "🔁 Your booking was rescheduled — " + safe(event.getBusinessName()), body);
        send(event.getBusinessEmail(),
                "Booking rescheduled — " + safe(event.getOfferingName()), body);
    }

    /** 24h reminder — notify the customer/guest. */
    public void sendBookingReminderToCustomer(BookingEvent event) {
        send(event.customerRecipientEmail(),
                "⏰ Reminder: your booking is tomorrow — " + safe(event.getBusinessName()),
                "<p>Hi " + safe(event.customerDisplayName()) + ",</p>"
                        + "<p>This is a reminder of your booking for <b>" + safe(event.getOfferingName())
                        + "</b> at <b>" + safe(event.getBusinessName()) + "</b> on " + when(event) + ".</p>"
                        + codeLine(event));
    }

    private void send(String to, String subject, String body) {
        if (to == null || to.isBlank()) {
            log.warn("Skipping booking email '{}' — no recipient address on event", subject);
            return;
        }
        emailService.sendEmail(sender, to, subject, body);
    }

    private String when(BookingEvent event) {
        return event.getStartTime() != null ? WHEN.format(event.getStartTime()) : "the scheduled time";
    }

    private String codeLine(BookingEvent event) {
        return event.getConfirmationCode() != null
                ? "<p>Confirmation code: <b>" + event.getConfirmationCode() + "</b></p>" : "";
    }

    private String safe(String s) {
        return s != null ? s : "";
    }
}
