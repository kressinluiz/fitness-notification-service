package com.kressin.fitness_notification_service.notification.repository;

import java.time.ZonedDateTime;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kressin.fitness_notification_service.notification.entity.Notification;
import com.kressin.fitness_notification_service.notification.entity.NotificationHistory;

public interface NotificationHistoryRepository extends JpaRepository<NotificationHistory, UUID> {

    boolean existsByNotification(Notification notification);

    boolean existsByNotificationIdAndScheduledFor(
            UUID notificationId,
            ZonedDateTime scheduledFor);

}
