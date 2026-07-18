package com.kressin.fitness_notification_service.messaging.event;

import java.time.ZonedDateTime;
import java.util.UUID;

public record WorkoutScheduledEvent(Long workoutPlanId, UUID messageId, ZonedDateTime scheduledAt) {
}
