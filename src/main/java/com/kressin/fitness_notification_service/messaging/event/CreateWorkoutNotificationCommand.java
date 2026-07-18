package com.kressin.fitness_notification_service.messaging.event;

import java.time.ZonedDateTime;
import java.util.UUID;

import com.kressin.fitness_notification_service.notification.enums.NotificationStatus;
import com.kressin.fitness_notification_service.notification.enums.NotificationType;

public record CreateWorkoutNotificationCommand(
        Long workoutPlanId,
        UUID messageId,
        NotificationType type,
        NotificationStatus status,
        ZonedDateTime createdAt) {

}
