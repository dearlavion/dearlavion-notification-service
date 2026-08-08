package com.dearlavion.notification.email.model;

import lombok.Data;

import java.util.List;

/** Body for POST /notification/internal/popular-kit-announcement — a server-to-server call from
 * store-engine's PopularKitService right after an admin creates a new Popular Kit. `emails` is
 * the current newsletter subscriber list at the time of creation (store-engine's own list), sent
 * once per new kit — never re-sent for edits or deactivations. */
@Data
public class PopularKitAnnouncementRequest {
    private List<String> emails;
    private String kitName;
    private String kitSlug;
    private String image;
    private String tag;
}
