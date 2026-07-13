package com.kressin.fitness_notification_service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class WorkoutNotifier {
    private static final Logger log = LoggerFactory.getLogger(WorkoutNotifier.class);
    private final WorkoutDetailsCache cache;

    public WorkoutNotifier(WorkoutDetailsCache cache) {
        this.cache = cache;
    }

    public void notifyWorkoutScheduled(WorkoutScheduledEvent event) {
        WorkoutDetails details = cache.getWorkoutDetails(event.workoutId());
        log.info("Lembrete: treino '{}' agendado para {} (plano #`{}", details.name(), event.scheduledAt(),
                event.workoutPlanId());
    }
}
