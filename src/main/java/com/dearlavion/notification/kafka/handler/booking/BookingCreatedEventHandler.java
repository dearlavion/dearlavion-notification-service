package com.dearlavion.notification.kafka.handler.booking;

import com.dearlavion.notification.email.BookingEmailService;
import com.dearlavion.notification.kafka.dto.KafkaEventType;
import com.dearlavion.notification.kafka.dto.booking.BookingEvent;
import com.dearlavion.notification.kafka.handler.EventHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookingCreatedEventHandler implements EventHandler<BookingEvent> {

    private final BookingEmailService bookingEmailService;

    @Override
    public KafkaEventType getEventType() {
        return KafkaEventType.BOOKING_CREATED;
    }

    @Override
    public Class<BookingEvent> payloadType() {
        return BookingEvent.class;
    }

    @Override
    public void handle(BookingEvent event) {
        bookingEmailService.sendBookingCreatedToBusiness(event);
    }
}
