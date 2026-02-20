package com.dearlavion.notification.security;

import lombok.Data;

@Data
public class AuthVerificationResponse {
    private boolean valid;
    private String username;
    private String email;
    private String userId;

}
