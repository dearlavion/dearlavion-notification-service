package com.dearlavion.notification.kafka.dto.core;

import com.dearlavion.notification.channels.ChannelType;
import com.dearlavion.notification.inApp.dto.RequestType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RequestEvent {
    private String id;
    private String sender;
    private String receiver;
    private RequestType type;
    private boolean read;
    private Instant createdAt;
    @Builder.Default
    private List<ChannelType> channels = List.of(ChannelType.IN_APP);
}
