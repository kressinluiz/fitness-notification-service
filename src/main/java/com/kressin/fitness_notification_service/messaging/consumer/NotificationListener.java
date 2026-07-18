package com.kressin.fitness_notification_service.messaging.consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import com.kressin.fitness_notification_service.config.RabbitConfig;
import com.kressin.fitness_notification_service.messaging.event.WorkoutScheduledEvent;
import com.kressin.fitness_notification_service.notification.service.NotificationService;

@Component
public class NotificationListener {
    private static final Logger log = LoggerFactory.getLogger(NotificationListener.class);
    private final NotificationService notifier;

    public NotificationListener(NotificationService notifier) {
        this.notifier = notifier;
    }

    @RabbitListener(queues = RabbitConfig.QUEUE_WORKOUT_SCHEDULED)
    void handle(WorkoutScheduledEvent event) {
        log.info(
                "Received WorkoutScheduledEvent. workoutPlanId={}, messageId={}, scheduledAt={}",
                event.workoutPlanId(),
                event.messageId(),
                event.scheduledAt());
        notifier.notifyWorkoutScheduled(event);
    }
}
