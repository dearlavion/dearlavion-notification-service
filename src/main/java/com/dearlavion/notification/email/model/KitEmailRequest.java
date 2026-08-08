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
    private String destination;

    @Data
    public static class Item {
        private String label;
        private String kitCategory;
        private String productName;
        private Double price;
        // Up to 3 clickable suggestions rendered as a bullet list under this item (see
        // EmailTemplateService.buildKitItemsHtml) — never trusted as a full product record, just
        // an id pair used to build a /product/:productId/items/:itemId link plus a display name.
        private List<Suggestion> suggestions;
    }

    @Data
    public static class Suggestion {
        private String productId;
        private String itemId;
        private String name;
    }
}
