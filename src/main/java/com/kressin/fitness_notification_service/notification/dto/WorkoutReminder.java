package com.kressin.fitness_notification_service.notification.dto;

import java.time.ZonedDateTime;

public record WorkoutReminder(
        Long workoutPlanId,
        String workoutName,
        ZonedDateTime scheduledFor) {
}
