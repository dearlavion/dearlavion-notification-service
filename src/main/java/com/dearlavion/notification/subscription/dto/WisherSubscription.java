package com.dearlavion.notification.subscription.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "wisher_subscriptions")
public class WisherSubscription extends BaseSubscription {

    private String country;
    private String city;

    private List<String> categories;

    private List<String> alertTypes;

    // init block for default type
    {
        setType(SubscriptionType.WISHER);
    }
}

