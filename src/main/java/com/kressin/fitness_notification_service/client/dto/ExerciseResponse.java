package com.kressin.fitness_notification_service.client.dto;

public record ExerciseResponse(
        Long id,
        String name,
        String description,
        String category,
        String muscleGroup) {

}
