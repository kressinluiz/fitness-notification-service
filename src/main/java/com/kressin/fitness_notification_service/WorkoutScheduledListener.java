package com.kressin.fitness_notification_service;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class WorkoutScheduledListener {
    private final WorkoutNotifier notifier;

    public WorkoutScheduledListener(WorkoutNotifier notifier) {
        this.notifier = notifier;
    }

    @RabbitListener(queues = RabbitConfig.QUEUE_WORKOUT_SCHEDULED)
    void handle(WorkoutScheduledEvent event) {
        notifier.notifyWorkoutScheduled(event);
    }
}
