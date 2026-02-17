package com.dearlavion.notification.subscription.repository;

import com.dearlavion.notification.subscription.dto.WisherSubscription;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface WisherSubscriptionRepository
        extends MongoRepository<WisherSubscription, String> {
    List<WisherSubscription> findByUserId(String userId);
}

