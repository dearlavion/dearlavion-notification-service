package org.example.repository;

import org.example.model.Notification;
import org.example.model.NotificationStatus;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface NotificationRepository extends MongoRepository<Notification, String> {
    List<Notification> findByStatus(NotificationStatus status);
}

