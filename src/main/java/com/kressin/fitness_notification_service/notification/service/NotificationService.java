package com.kressin.fitness_notification_service.notification.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.kressin.fitness_notification_service.messaging.event.CreateWorkoutNotificationCommand;
import com.kressin.fitness_notification_service.messaging.event.WorkoutScheduledEvent;
import com.kressin.fitness_notification_service.notification.entity.Notification;
import com.kressin.fitness_notification_service.notification.enums.NotificationStatus;
import com.kressin.fitness_notification_service.notification.enums.NotificationType;
import com.kressin.fitness_notification_service.notification.mapper.NotificationMapper;
import com.kressin.fitness_notification_service.notification.repository.NotificationRepository;

import jakarta.transaction.Transactional;

@Component
public class NotificationService {
    private final NotificationRepository notificationRepository;
    private static final Logger log = LoggerFactory.getLogger(NotificationService.class);

    public NotificationService(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    @Transactional
    public void notifyWorkoutScheduled(WorkoutScheduledEvent event) {
        Notification notification = NotificationMapper.toWorkoutNotificationEntity(
                new CreateWorkoutNotificationCommand(
                        event.workoutPlanId(),
                        event.messageId(),
                        NotificationType.WORKOUT_REMINDER,
                        NotificationStatus.ACTIVE,
                        event.scheduledAt()));

        try {
            notification = notificationRepository.save(notification);

            log.info(
                    "Notification created. Id={}, workoutPlanId={}, Type={}, Status={}, ScheduledAt={}",
                    notification.getId(),
                    notification.getOwnerId(),
                    notification.getType(),
                    notification.getStatus(),
                    notification.getScheduledAt());
        } catch (Exception e) {
            log.info("Ignoring duplicated message. messageId={}", event.messageId(), e);
        }

    }
}
