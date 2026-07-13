package com.kressin.fitness_notification_service;

import java.time.ZonedDateTime;

public record WorkoutScheduledEvent(Long workoutPlanId, Long workoutId, ZonedDateTime scheduledAt) {
}
