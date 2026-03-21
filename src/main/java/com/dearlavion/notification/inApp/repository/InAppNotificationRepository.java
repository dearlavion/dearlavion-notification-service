package com.dearlavion.notification.inApp.repository;

import com.dearlavion.notification.inApp.dto.InAppNotification;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface InAppNotificationRepository extends MongoRepository<InAppNotification, String> {
}
