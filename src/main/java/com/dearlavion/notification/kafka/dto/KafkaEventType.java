package com.dearlavion.notification.kafka.dto;

public enum KafkaEventType {
    //auth events
    RESET_PASSWORD,
    NEW_USER,

    // core events
    NEW_WISH,
    REQUEST,

    // booking-engine events
    BOOKING_CREATED,
    BOOKING_CONFIRMED,
    BOOKING_CANCELLED,
    BOOKING_RESCHEDULED,
    BOOKING_REMINDER_DUE
}
