package com.kressin.fitness_notification_service.client.dto;

public record WorkoutPlanResponse(
        Long id,
        WorkoutResponse workout,
        WorkoutDateResponse workoutDate) {
}
