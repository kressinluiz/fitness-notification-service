package com.kressin.fitness_notification_service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class FitnessApiClient {
    private final RestClient restClient;

    public FitnessApiClient(@Value("${fitness-api.base-url:http://localhost:8080}") String baseUrl) {
        this.restClient = RestClient.builder()
                .baseUrl(baseUrl)
                .build();
    }

    public WorkoutDetails getWorkoutDetails(Long workoutId) {
        return restClient.get()
                .uri("/workouts/{id}", workoutId)
                .retrieve()
                .body(WorkoutDetails.class);
    }
}
