package com.kressin.fitness_notification_service.messaging.mapper;

import java.time.ZonedDateTime;

import com.kressin.fitness_notification_service.client.dto.WorkoutPlanResponse;
import com.kressin.fitness_notification_service.notification.dto.WorkoutReminder;

public class WorkoutReminderMapper {
    public static WorkoutReminder toReminder(WorkoutPlanResponse workoutPlan, ZonedDateTime scheduledFor) {
        return new WorkoutReminder(
                workoutPlan.id(),
                workoutPlan.workout().name(),
                scheduledFor);
    }
}
