package com.dearlavion.notification.email.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EmailRequest {

    private String to;
    private String subject;
    private String body;
}
