package com.kressin.fitness_notification_service.client.dto;

public record WorkoutResponse(
        Long id,
        String name,
        String description) {
}
