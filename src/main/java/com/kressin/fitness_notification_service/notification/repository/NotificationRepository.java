package com.kressin.fitness_notification_service.notification.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kressin.fitness_notification_service.notification.entity.Notification;
import com.kressin.fitness_notification_service.notification.enums.NotificationStatus;

public interface NotificationRepository extends JpaRepository<Notification, UUID> {

    boolean existsByMessageId(UUID messageId);

    List<Notification> findByStatus(NotificationStatus status);

}
