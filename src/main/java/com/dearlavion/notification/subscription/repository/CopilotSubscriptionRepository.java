package com.dearlavion.notification.subscription.repository;

import com.dearlavion.notification.subscription.dto.CopilotSubscription;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface CopilotSubscriptionRepository
        extends MongoRepository<CopilotSubscription, String> {
    List<CopilotSubscription> findByUserId(String userId);

    List<CopilotSubscription> findByCountryAndCityAndIsActive(
            String country,
            String city,
            Boolean isActive
    );
}

