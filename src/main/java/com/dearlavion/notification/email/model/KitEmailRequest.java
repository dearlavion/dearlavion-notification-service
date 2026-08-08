package com.dearlavion.notification.email.model;

import lombok.Data;

import java.util.List;

/**
 * Body for POST /notification/email/kit. Deliberately has no `to` field — the recipient is
 * always the caller's own verified email from AuthServiceClient.verify(), never client-supplied,
 * so an authenticated request can only ever email itself.
 */
@Data
public class KitEmailRequest {

    private String kitTitle;
    private String summary;
    private List<Item> items;

    @Data
    public static class Item {
        private String label;
        private String productName;
        private Double price;
    }
}
