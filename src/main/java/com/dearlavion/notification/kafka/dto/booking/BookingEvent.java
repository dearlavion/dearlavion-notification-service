package com.dearlavion.notification.kafka.dto.booking;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

/**
 * Booking snapshot published by dearlavion-booking-engine on the {@code booking-engine-event} topic.
 * Hand-duplicated from booking-engine's DTO (this backend shares no common library). Carries
 * recipient addresses for both parties so no callback into booking-engine is needed:
 * {@code businessEmail} for the owner/staff side, {@code customerEmail}/{@code guestEmail} for the
 * customer side. Guests have no account, so their email is the only way to reach them.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookingEvent {

    private String bookingId;
    private String businessId;
    private String businessName;
    private String businessEmail;

    private String offeringName;
    private String staffName;

    private Instant startTime;
    private Instant endTime;
    private String status;
    private String confirmationCode;

    /** GUEST or CUSTOMER. */
    private String channel;

    private String customerId;
    private String customerEmail;
    private String customerName;

    private String guestName;
    private String guestEmail;
    private String guestPhone;

    private String cancellationReason;

    /** The customer-side recipient: a logged-in customer's email, else the guest's email. */
    public String customerRecipientEmail() {
        return customerEmail != null ? customerEmail : guestEmail;
    }

    /** The customer-side display name: a logged-in customer's name, else the guest's name. */
    public String customerDisplayName() {
        if (customerName != null) {
            return customerName;
        }
        return guestName != null ? guestName : "there";
    }
}
