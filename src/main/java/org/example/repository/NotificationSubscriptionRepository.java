package org.example.repository;

import org.example.model.NotificationSubscription;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface NotificationSubscriptionRepository extends MongoRepository<NotificationSubscription, String> {

    List<NotificationSubscription> findByCountryCodeAndCityAndActive(String countryCode, String city, boolean active);
}

