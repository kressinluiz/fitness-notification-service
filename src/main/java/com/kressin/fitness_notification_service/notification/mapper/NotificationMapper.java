package com.kressin.fitness_notification_service.notification.mapper;

import java.time.ZonedDateTime;

import com.kressin.fitness_notification_service.messaging.event.CreateWorkoutNotificationCommand;
import com.kressin.fitness_notification_service.notification.entity.Notification;
import com.kressin.fitness_notification_service.notification.enums.NotificationStatus;
import com.kressin.fitness_notification_service.notification.enums.NotificationType;

public class NotificationMapper {
    public static Notification toWorkoutNotificationEntity(CreateWorkoutNotificationCommand command) {
        return new Notification(
                command.workoutPlanId(),
                command.messageId(),
                NotificationType.WORKOUT_REMINDER,
                NotificationStatus.ACTIVE,
                ZonedDateTime.now());
    }
}
