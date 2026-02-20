package com.dearlavion.notification.subscription.repository;

import com.dearlavion.notification.subscription.dto.CopilotSubscription;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface CopilotSubscriptionRepository
        extends MongoRepository<CopilotSubscription, String> {
    List<CopilotSubscription> findByUsername(String username);

    List<CopilotSubscription> findByCountryCodeAndCityNameAndIsActive(
            String countryCode,
            String cityName,
            Boolean isActive
    );
}

