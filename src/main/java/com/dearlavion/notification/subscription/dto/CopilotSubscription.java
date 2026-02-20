package com.dearlavion.notification.subscription.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.data.mongodb.core.index.GeoSpatialIndexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "copilot_subscriptions")
public class CopilotSubscription extends BaseSubscription {

    private String coverageType;

    private String countryCode;
    private String cityName;

    @GeoSpatialIndexed
    private GeoJsonPoint centerLocation;

    private Double radiusKm;

    private List<String> categories;

    private Boolean isPaid;
    //init block for default type
    {
        setType(SubscriptionType.COPILOT);
    }
}
