package com.dearlavion.notification.kafka.dto.authentication;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResetPasswordEvent {
    private String username;      // Optional, for logging / personalization
    private String email;         // Required, where to send email
    private String token;         // Required, JWT reset token
}
